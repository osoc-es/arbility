package com.osoc.oncera;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.core.Pose;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;
import com.osoc.oncera.adapters.ImageTitleAdapter;
import com.osoc.oncera.javabean.Ramps;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MeasureRamp extends AppCompatActivity {

    private static final String TAG = MeasureDoor.class.getSimpleName();
    private static final double MIN_OPENGL_VERSION = 3.0;
    private float upDistance = 0f;
    private ArFragment arFragment;
    private ModelRenderable andyRenderable;
    private Anchor myanchor;
    private AnchorNode myanchornode;
    private DecimalFormat form_numbers = new DecimalFormat("#0.00");

    private List<AnchorNode> anchorNodes;

    private boolean measuring_width = false;
    private boolean railing = true;

    Button restart;
    Button confirm;
    TextView data;
    TextView ramp_width;
    TextView ledge_height;
    TextView ramp_length;
    SeekBar sk_height_control;
    private ImageView img_instr;

    private Anchor anchor1 = null, anchor2 = null;

    private HitResult myhit;

    private Ramps ramp = new Ramps(null, null, null, null, null, null, null, null, null);

    @Override
    @SuppressWarnings({"AndroidApiChecker", "FutureReturnValueIgnored"})
    // CompletableFuture requires api level 24
    // FutureReturnValueIgnored is not valid
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!checkIsSupportedDeviceOrFinish(this)) {
            return;
        }

        setContentView(R.layout.activity_measure_ramp);
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);
        restart = (Button) findViewById(R.id.btn_restart);
        confirm = (Button) findViewById(R.id.btn_ok);
        data = (TextView) findViewById(R.id.tv_distance);

        ramp_width = (TextView) findViewById(R.id.ramp_width);
        ledge_height = (TextView) findViewById(R.id.ledge_height);
        ramp_length = (TextView) findViewById(R.id.ramp_length);

        sk_height_control = (SeekBar) findViewById(R.id.sk_height_control);
        ImageButton btnBack = (ImageButton) findViewById(R.id.btnBack);
        img_instr = (ImageView) findViewById(R.id.img_instr);

        anchorNodes = new ArrayList<>();

        sk_height_control.setEnabled(false);
        confirm.setEnabled(false);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetLayout();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!measuring_width) {
                    measuring_width = true;
                    measureWidthLayout();
                } else {
                    Intent guestActivity = new Intent(MeasureRamp.this, MeasureInclination.class);
                    guestActivity.putExtra("rampaIntermedio", ramp);
                    guestActivity.putExtra("railing", railing);
                    startActivity(guestActivity);
                    finish();
                }
            }
        });


        sk_height_control.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                upDistance = progress;
                ascend(myanchornode, upDistance);
                confirm.setEnabled(true);
                ledge_height.setText("Altura barandilla: " +
                        form_numbers.format(progress / 100f));
                ramp.setHandRailHeight((float) progress * 100);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        // When you build a Renderable, Sceneform loads its resources in the background while returning
        // a CompletableFuture. Call thenAccept(), handle(), or check isDone() before calling get().
        ModelRenderable.builder()
                .setSource(this, R.raw.cubito)
                .build()
                .thenAccept(renderable -> andyRenderable = renderable)
                .exceptionally(
                        throwable -> {
                            Toast toast =
                                    Toast.makeText(this, "Unable to load andy renderable", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            return null;
                        });


        arFragment.setOnTapArPlaneListener(
                (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {
                    if (andyRenderable == null) {
                        return;
                    }
                    myhit = hitResult;

                    // Create the Anchor.
                    Anchor anchor = hitResult.createAnchor();

                    AnchorNode anchorNode = new AnchorNode(anchor);


                    anchorNode.setParent(arFragment.getArSceneView().getScene());
                    anchorNodes.add(anchorNode);

                    if (anchor1 == null) {
                        anchor1 = anchor;
                    } else {
                        anchor2 = anchor;
                        if (!measuring_width) {
                            ramp_length.setText("Longitud rampa: " +
                                    form_numbers.format(getMetersBetweenAnchors(anchor1, anchor2)));
                            confirm.setEnabled(true);
                            ramp.setLength(getMetersBetweenAnchors(anchor1, anchor2) * 100);
                        } else {
                            ramp_width.setText("Anchura rampa: " +
                                    form_numbers.format(getMetersBetweenAnchors(anchor1, anchor2)));
                            ramp.setWidth(getMetersBetweenAnchors(anchor1, anchor2) * 100);


                            if (!railing)
                                confirm.setEnabled(true);
                            else {
                                sk_height_control.setEnabled(true);
                                data.setText(R.string.instr_rampa_03);
                                img_instr.setImageResource(R.drawable.rampa_03);
                            }
                        }
                    }
                    myanchornode = anchorNode;

                    // Create the transformable andy and add it to the anchor.
                    TransformableNode andy = new TransformableNode(arFragment.getTransformationSystem());
                    andy.setParent(anchorNode);
                    andy.setRenderable(andyRenderable);
                    andy.select();
                    andy.getScaleController().setEnabled(false);
                });
        railingDialog();
    }

    /**
     * Function to raise an object perpendicular to the ArPlane a specific distance
     * @param an anchor belonging to the object that should be raised
     * @param up distance in centimeters the object should be raised vertically
     */
    private void ascend(AnchorNode an, float up) {
        Anchor anchor = myhit.getTrackable().createAnchor(
                myhit.getHitPose().compose(Pose.makeTranslation(0, up / 100f, 0)));

        an.setAnchor(anchor);
    }

    /**
     * Function to return the distance in meters between two objects placed in ArPlane
     * @param anchor1 first object's anchor
     * @param anchor2 second object's anchor
     * @return the distance between the two anchors in meters
     */
    private float getMetersBetweenAnchors(Anchor anchor1, Anchor anchor2) {
        float[] distance_vector = anchor1.getPose().inverse()
                .compose(anchor2.getPose()).getTranslation();
        float totalDistanceSquared = 0;
        for (int i = 0; i < 3; ++i)
            totalDistanceSquared += distance_vector[i] * distance_vector[i];
        return (float) Math.sqrt(totalDistanceSquared);
    }

    /**
     * Dialog to ask user whether the ramp has a railing or not
     */
    private void railingDialog() {
        int[] spinnerImages = new int[]{R.drawable.rampa_barandilla
                , R.drawable.rampa_sin_barandilla};

        String[] spinnerPopulation = new String[]{"Rampa con barandilla", "Rampa sin barandilla"};


        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MeasureRamp.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_single_spinner, null);
        mBuilder.setTitle("Selecciona rampa");
        Spinner mSpinner = (Spinner) mView.findViewById(R.id.spinner);
        ImageTitleAdapter mCustomAdapter = new ImageTitleAdapter(MeasureRamp.this, spinnerImages, spinnerPopulation);
        mSpinner.setAdapter(mCustomAdapter);


        mBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        mBuilder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (mSpinner.getSelectedItemPosition() == 1) {
                    railing = false;
                    ledge_height.setVisibility(View.INVISIBLE);
                }
            }
        });

        mBuilder.setView(mView);
        AlertDialog dialog = mBuilder.create();

        dialog.show();
    }

    /**
     * Prepare layout to start measuring the ramp's width
     */
    private void measureWidthLayout() {
        anchor1 = null;
        anchor2 = null;
        confirm.setEnabled(false);
        confirm.setText("Confirm");
        data.setText(R.string.instr_rampa_02);
        img_instr.setImageResource(R.drawable.rampa_02);
        for (AnchorNode n : anchorNodes) {
            arFragment.getArSceneView().getScene().removeChild(n);
            n.getAnchor().detach();
            n.setParent(null);
            n = null;
        }
    }

    /**
     * Reset layout to its initial state
     */
    private void resetLayout(){
        anchor1 = null;
        anchor2 = null;
        measuring_width = false;
        sk_height_control.setProgress(0);
        sk_height_control.setEnabled(false);
        confirm.setEnabled(false);
        confirm.setText("Next");
        data.setText(R.string.instr_rampa_01);
        img_instr.setImageResource(R.drawable.rampa_01);

        ramp_length.setText("Longitud rampa: --");
        ramp_width.setText("Anchura rampa: --");
        ledge_height.setText("Altura barandilla: --");


        for (AnchorNode n : anchorNodes) {
            arFragment.getArSceneView().getScene().removeChild(n);
            n.getAnchor().detach();
            n.setParent(null);
            n = null;
        }
    }

    /**
     * Check whether the device supports the tools required to use the measurement tools
     * @param activity
     * @return boolean determining whether the device is supported or not
     */
    private boolean checkIsSupportedDeviceOrFinish(final Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            Log.e(TAG, "Sceneform requires Android N or later");
            Toast.makeText(activity, "Sceneform requires Android N or later", Toast.LENGTH_LONG).show();
            activity.finish();
            return false;
        }
        String openGlVersionString =
                ((ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE))
                        .getDeviceConfigurationInfo()
                        .getGlEsVersion();
        if (Double.parseDouble(openGlVersionString) < MIN_OPENGL_VERSION) {
            Log.e(TAG, "Sceneform requires OpenGL ES 3.0 later");
            Toast.makeText(activity, "Sceneform requires OpenGL ES 3.0 or later", Toast.LENGTH_LONG)
                    .show();
            activity.finish();
            return false;
        }
        return true;
    }

}
