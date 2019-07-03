package com.pokoidev.camerapreviewtest;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.ImageFormat;
import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements TextureView.SurfaceTextureListener, ActivityCompat.OnRequestPermissionsResultCallback {


    private com.pokoidev.camerapreviewtest.AutoFitTextureView camera_texture_view;
    private TextView text;
    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private static final int MAX_PREVIEW_WIDTH = 1920;
    private static final int MAX_PREVIEW_HEIGHT = 1080;
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }
    private Semaphore camera_open_close_semaphore = new Semaphore(1);
    private String camera_id;
    private CameraDevice camera_device;
    private CameraCaptureSession capture_session;
    private CaptureRequest.Builder preview_request_builder;
    private CaptureRequest preview_request;
    private int sensor_orientation;
    private Size preview_size;

    /**
     * Method that manages the close of the camera
     */
    private void closeCamera() {
        try {
            camera_open_close_semaphore.acquire();
            if (null != capture_session) {
                capture_session.close();
                capture_session = null;
            }
            if (null != camera_device) {
                camera_device.close();
                camera_device = null;
            }
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted while trying to lock camera closing.", e);
        } finally {
            camera_open_close_semaphore.release();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Check if the texture view of the camera is still available
        if (camera_texture_view.isAvailable()) {
            openCamera(camera_texture_view.getWidth(), camera_texture_view.getHeight());
        } else {
            camera_texture_view.setSurfaceTextureListener(this);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RelativeLayout _layout = (RelativeLayout) findViewById(R.id.layout);

        //TODO: Save all the views reference of the layout

        camera_texture_view = new com.pokoidev.camerapreviewtest.AutoFitTextureView(this);
        camera_texture_view.setSurfaceTextureListener(this);

        _layout.addView(camera_texture_view);

        //TODO: Remove all the views from the layout
        _layout.removeView(text);

        //TODO: Add the views to the layout
        _layout.addView(text);



    }

    static class CompareSizesByArea implements Comparator<Size> {

        @Override
        public int compare(Size lhs, Size rhs) {
            return Long.signum((long) lhs.getWidth() * lhs.getHeight() -
                    (long) rhs.getWidth() * rhs.getHeight());
        }
    }

    private static Size chooseOptimalSize(Size[] choices, int textureViewWidth,
                                          int textureViewHeight, int maxWidth, int maxHeight, Size aspectRatio) {
        List<Size> bigEnough = new ArrayList<>();
        List<Size> notBigEnough = new ArrayList<>();
        int w = aspectRatio.getWidth();
        int h = aspectRatio.getHeight();
        for (Size option : choices) {
            if (option.getWidth() <= maxWidth && option.getHeight() <= maxHeight &&
                    option.getHeight() == option.getWidth() * h / w) {
                if (option.getWidth() >= textureViewWidth &&
                        option.getHeight() >= textureViewHeight) {
                    bigEnough.add(option);
                } else {
                    notBigEnough.add(option);
                }
            }
        }

        if (bigEnough.size() > 0) {
            return Collections.min(bigEnough, new CompareSizesByArea());
        } else if (notBigEnough.size() > 0) {
            return Collections.max(notBigEnough, new CompareSizesByArea());
        } else {
            return choices[0];
        }
    }

    private void setUpCameraOutputs(int width, int height) {
        Activity activity = this;
        CameraManager manager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);
        try {
            for (String cameraId : manager.getCameraIdList()) {
                CameraCharacteristics characteristics
                        = manager.getCameraCharacteristics(cameraId);

                Integer facing = characteristics.get(CameraCharacteristics.LENS_FACING);
                if (facing != null && facing == CameraCharacteristics.LENS_FACING_FRONT) {
                    continue;
                }

                camera_id = cameraId;

                StreamConfigurationMap map = characteristics.get(
                        CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                if (map == null) {
                    continue;
                }

                Size largest = Collections.max(
                        Arrays.asList(map.getOutputSizes(ImageFormat.JPEG)),
                        new CompareSizesByArea());

                int displayRotation = activity.getWindowManager().getDefaultDisplay().getRotation();
                sensor_orientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
                boolean swappedDimensions = false;
                switch (displayRotation) {
                    case Surface.ROTATION_0:
                    case Surface.ROTATION_180:
                        if (sensor_orientation == 90 || sensor_orientation == 270) {
                            swappedDimensions = true;
                        }
                        break;
                    case Surface.ROTATION_90:
                    case Surface.ROTATION_270:
                        if (sensor_orientation == 0 || sensor_orientation == 180) {
                            swappedDimensions = true;
                        }
                        break;
                }

                Point displaySize = new Point();
                activity.getWindowManager().getDefaultDisplay().getSize(displaySize);
                int rotatedPreviewWidth = width;
                int rotatedPreviewHeight = height;
                int maxPreviewWidth = displaySize.x;
                int maxPreviewHeight = displaySize.y;

                if (swappedDimensions) {
                    rotatedPreviewWidth = height;
                    rotatedPreviewHeight = width;
                    maxPreviewWidth = displaySize.y;
                    maxPreviewHeight = displaySize.x;
                }

                if (maxPreviewWidth > MAX_PREVIEW_WIDTH) {
                    maxPreviewWidth = MAX_PREVIEW_WIDTH;
                }

                if (maxPreviewHeight > MAX_PREVIEW_HEIGHT) {
                    maxPreviewHeight = MAX_PREVIEW_HEIGHT;
                }

                preview_size = chooseOptimalSize(map.getOutputSizes(SurfaceTexture.class),
                        rotatedPreviewWidth, rotatedPreviewHeight, maxPreviewWidth,
                        maxPreviewHeight, largest);

                int orientation = getResources().getConfiguration().orientation;
                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    camera_texture_view.setAspectRatio(
                            preview_size.getWidth(), preview_size.getHeight());
                } else {
                    camera_texture_view.setAspectRatio(
                            preview_size.getHeight(), preview_size.getWidth());
                }

                return;
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
        }
    }

    /**
     * Method called when the camera is open to create the preview
     */
    private void createCameraPreviewSession() {
        try {
            SurfaceTexture texture = camera_texture_view.getSurfaceTexture();
            assert texture != null;
            Surface surface = new Surface(texture);

            preview_request_builder = camera_device.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            preview_request_builder.addTarget(surface);
            camera_device.createCaptureSession(Arrays.asList(surface),
                    new CameraCaptureSession.StateCallback() {

                        @Override
                        public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                            if (null == camera_device) {
                                return;
                            }

                            capture_session = cameraCaptureSession;
                            try {
                                preview_request_builder.set(CaptureRequest.CONTROL_AF_MODE,
                                        CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);

                                preview_request = preview_request_builder.build();
                                capture_session.setRepeatingRequest(preview_request,
                                        null, null);
                            } catch (CameraAccessException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onConfigureFailed(
                                @NonNull CameraCaptureSession cameraCaptureSession) {
                        }
                    }, null
            );
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }


    /**
     * Camera device events calls in relation to the camera device state
     */
    private final CameraDevice.StateCallback mStateCallback = new CameraDevice.StateCallback() {

        /**
         * Method called when the camera is opened
         * @param cameraDevice
         */
        @Override
        public void onOpened(@NonNull CameraDevice cameraDevice) {
            camera_open_close_semaphore.release();
            camera_device = cameraDevice;
            createCameraPreviewSession();
        }

        /**
         * Method called when the camera is disconnected
         * @param cameraDevice
         */
        @Override
        public void onDisconnected(@NonNull CameraDevice cameraDevice) {
            camera_open_close_semaphore.release();
            cameraDevice.close();
            camera_device = null;
        }

        /**
         * Method called when any error occurred with the camera
         * @param cameraDevice
         * @param error
         */
        @Override
        public void onError(@NonNull CameraDevice cameraDevice, int error) {
            camera_open_close_semaphore.release();
            cameraDevice.close();
            camera_device = null;
            Activity activity = MainActivity.this;
            if (null != activity) {
                activity.finish();
            }
        }

    };

    /**
     * This method opens the camera with given width and height sizes
     */
    private void openCamera(int width, int height) {

        // Request the permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestCameraPermission();
            return;
        }

        // Set the size
        setUpCameraOutputs(width, height);

        //Opens the camera
        CameraManager manager = (CameraManager)this.getSystemService(Context.CAMERA_SERVICE);
        try {
            if (!camera_open_close_semaphore.tryAcquire(2500, TimeUnit.MILLISECONDS)) {
                throw new RuntimeException("Time out waiting to lock camera opening.");
            }
            manager.openCamera(camera_id, mStateCallback, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted while trying to lock camera opening.", e);
        }
    }

    /**
     * Implementation of interface android.view.TextureView.SurfaceTextureListener method.
     * Called when the surface texture is available
     * @param surface the surface texture
     * @param width the width of the view
     * @param height the height of the view
     */
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        openCamera(width, height);
    }

    /**
     * Implementation of interface android.view.TextureView.SurfaceTextureListener method.
     * Called when the size of the surface texture changes
     * @param surface The surface texture
     * @param width The width of the view
     * @param height The height of the view
     */
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
    }

    /**
     * Implementation of interface android.view.TextureView.SurfaceTextureListener method.
     * Called when the surface texture is destroyed.
     * @param surface The surface texture
     * @return
     */
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        closeCamera();
        return true;
    }

    /**
     * Implementation of interface android.view.TextureView.SurfaceTextureListener method.
     * Called when the surface texture is updated
     * @param surface The surface texture
     */
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
    }

    /**
     * Method that request the permission to uses the camera
     */
    private void requestCameraPermission() {
        requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
    }

    /**
     * Implementation of the method from android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback and android.app.Activity
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA_PERMISSION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                }
                break;
            }
        }
    }
}



