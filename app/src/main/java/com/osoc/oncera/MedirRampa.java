package com.osoc.oncera;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
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

import com.firebase.ui.auth.data.model.User;
import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.core.Pose;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.osoc.oncera.adapters.ImageTitleAdapter;
import com.osoc.oncera.javabean.Rampas;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MedirRampa extends AppCompatActivity {

    private static final String TAG = MeasureActivity.class.getSimpleName();
    private static final double MIN_OPENGL_VERSION = 3.0;
    private float upDistance = 0f;
    private ArFragment arFragment;
    private ModelRenderable andyRenderable;
    private Anchor myanchor;
    private AnchorNode myanchornode;
    private DecimalFormat form_numbers = new DecimalFormat("#0.00");

    private List<AnchorNode> anchorNodes;

    private boolean medir_anchura = false;
    private boolean barandilla = true;

    Button restart;
    Button confirm;
    TextView data;
    TextView ancho_rampa;
    TextView alto_barandilla;
    TextView largo_rampa;
    SeekBar z_axis;
    private ImageView img_instr;

    private Anchor anchor1 = null, anchor2 = null;

    private HitResult myhit;

    private Rampas rampa = new Rampas(null, null, null, null, null, null, null, null, null);

    @Override
    @SuppressWarnings({"AndroidApiChecker", "FutureReturnValueIgnored"})
    // CompletableFuture requires api level 24
    // FutureReturnValueIgnored is not valid
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!checkIsSupportedDeviceOrFinish(this)) {
            return;
        }

        setContentView(R.layout.activity_medir_rampa);
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);
        restart = (Button) findViewById(R.id.btn_restart);
        confirm = (Button) findViewById(R.id.btn_ok);
        data = (TextView) findViewById(R.id.tv_distance);

        ancho_rampa = (TextView) findViewById(R.id.ancho_rampa);
        alto_barandilla = (TextView) findViewById(R.id.alto_barandilla);
        largo_rampa = (TextView) findViewById(R.id.largo_rampa);

        z_axis = (SeekBar) findViewById(R.id.z_axis);
        ImageButton btnAtras = (ImageButton) findViewById(R.id.btnAtras);
        img_instr = (ImageView) findViewById(R.id.img_instr);

        anchorNodes = new ArrayList<>();

        z_axis.setEnabled(false);
        confirm.setEnabled(false);

        btnAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                anchor1 = null;
                anchor2 = null;
                medir_anchura = false;
                z_axis.setProgress(0);
                z_axis.setEnabled(false);
                confirm.setEnabled(false);
                confirm.setText("Next");
                data.setText(R.string.instr_rampa_01);
                img_instr.setImageResource(R.drawable.rampa_01);

                largo_rampa.setText("Longitud rampa: --");
                ancho_rampa.setText("Anchura rampa: --");
                alto_barandilla.setText("Altura barandilla: --");


                for (AnchorNode n : anchorNodes) {
                    arFragment.getArSceneView().getScene().removeChild(n);
                    n.getAnchor().detach();
                    n.setParent(null);
                    n = null;
                }
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!medir_anchura) {
                    medir_anchura = true;
                    resetMedirAnchura();
                } else {
                    Intent guestActivity = new Intent(MedirRampa.this, MedirInclinacion.class);
                    guestActivity.putExtra("rampaIntermedio", rampa);
                    guestActivity.putExtra("barandilla", barandilla);
                    startActivity(guestActivity);
                    finish();
                }
            }
        });


        z_axis.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                upDistance = progress;
                ascend(myanchornode, upDistance);
                confirm.setEnabled(true);
                alto_barandilla.setText("Altura barandilla: " +
                        form_numbers.format(progress / 100f));
                rampa.setAlturaPasamanosSuperior((float) progress * 100);
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
                        if (!medir_anchura) {
                            largo_rampa.setText("Longitud rampa: " +
                                    form_numbers.format(getMetersBetweenAnchors(anchor1, anchor2)));
                            confirm.setEnabled(true);
                            rampa.setLongitud(getMetersBetweenAnchors(anchor1, anchor2) * 100);
                        } else {
                            ancho_rampa.setText("Anchura rampa: " +
                                    form_numbers.format(getMetersBetweenAnchors(anchor1, anchor2)));
                            rampa.setAnchura(getMetersBetweenAnchors(anchor1, anchor2) * 100);


                            if (!barandilla)
                                confirm.setEnabled(true);
                            else {
                                z_axis.setEnabled(true);
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
        barandillaDialog();
    }


    void ascend(AnchorNode an, float up) {
        Anchor anchor = myhit.getTrackable().createAnchor(
                myhit.getHitPose().compose(Pose.makeTranslation(0, up / 100f, 0)));

        an.setAnchor(anchor);
    }


    float getMetersBetweenAnchors(Anchor anchor1, Anchor anchor2) {
        float[] distance_vector = anchor1.getPose().inverse()
                .compose(anchor2.getPose()).getTranslation();
        float totalDistanceSquared = 0;
        for (int i = 0; i < 3; ++i)
            totalDistanceSquared += distance_vector[i] * distance_vector[i];
        return (float) Math.sqrt(totalDistanceSquared);
    }

    void barandillaDialog() {
        int[] spinnerImages = new int[]{R.drawable.rampa_barandilla
                , R.drawable.rampa_sin_barandilla};

        String[] spinnerPopulation = new String[]{"Rampa con barandilla", "Rampa sin barandilla"};


        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MedirRampa.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_door, null);
        mBuilder.setTitle("Selecciona rampa");
        Spinner mSpinner = (Spinner) mView.findViewById(R.id.spinner);
        ImageTitleAdapter mCustomAdapter = new ImageTitleAdapter(MedirRampa.this, spinnerImages, spinnerPopulation);
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
                    barandilla = false;
                    alto_barandilla.setVisibility(View.INVISIBLE);
                }
            }
        });

        mBuilder.setView(mView);
        AlertDialog dialog = mBuilder.create();

        dialog.show();
    }

    void resetMedirAnchura() {
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


    public static boolean checkIsSupportedDeviceOrFinish(final Activity activity) {
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
