package com.osoc.oncera;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Build.VERSION_CODES;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.osoc.oncera.adapters.ImageTitleAdapter;
import com.osoc.oncera.javabean.PuntosAtencion;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MeasureCounter extends AppCompatActivity {

    private static final String TAG = MeasureDoor.class.getSimpleName();
    private static final double MIN_OPENGL_VERSION = 3.0;
    private float upDistance = 0f;
    private ArFragment arFragment;
    private ModelRenderable andyRenderable;
    private Anchor myanchor;
    private AnchorNode myanchornode;
    private DecimalFormat form_numbers = new DecimalFormat("#0.00");

    private List<AnchorNode> anchorNodes;

    private boolean ledge = false;
    private boolean measuring_ledge = false;

    Button restart;
    Button confirm;
    TextView data;
    TextView counter_width;
    TextView counter_height;
    TextView ledge_depth;
    TextView ledge_height;
    SeekBar sk_height_control;
    private ImageView img_instr;

    private String message;

    private Anchor anchor1 = null, anchor2 = null;

    private HitResult myhit;

    private PuntosAtencion counter = new PuntosAtencion(null, null, null, null, null, null, null, null, null);

    float db_counter_width,
            db_counter_height,
            db_ledge_height,
            db_ledge_width,
            db_ledge_depth;

    @Override
    @SuppressWarnings({"AndroidApiChecker", "FutureReturnValueIgnored"})
    // CompletableFuture requires api level 24
    // FutureReturnValueIgnored is not valid
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!checkIsSupportedDeviceOrFinish(this)) {
            return;
        }

        UpdateDatabaseValues();

        setContentView(R.layout.activity_measure_counter);
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);
        restart = (Button) findViewById(R.id.btn_restart);
        confirm = (Button) findViewById(R.id.btn_ok);
        data = (TextView) findViewById(R.id.tv_distance);
        counter_width = (TextView) findViewById(R.id.counter_width);
        counter_height = (TextView) findViewById(R.id.counter_height);
        ledge_depth = (TextView) findViewById(R.id.ledge_depth);
        ledge_height = (TextView) findViewById(R.id.ledge_height);
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
                if (ledge && !measuring_ledge) {
                    measuring_ledge = true;
                    startMeasureLedge();
                }
                else{
                    Toast.makeText(MeasureCounter.this, "Confirmado", Toast.LENGTH_SHORT).show();
                    Confirmar();
                }
            }
        });


        sk_height_control.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                upDistance = progress;
                ascend(myanchornode, upDistance);
                confirm.setEnabled(true);
                if (!measuring_ledge) {
                    counter_height.setText("Altura trabajo: " +
                            form_numbers.format(progress / 100f));
                    counter.setAlturaPlanoTrabajo((float) progress);
                } else {
                    ledge_height.setText("Altura repisa: " +
                            form_numbers.format(progress / 100f));
                    counter.setAlturaEspacioInferiorLibre((float) progress);
                }
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
                        sk_height_control.setEnabled(true);
                        if (!measuring_ledge) {
                            counter_width.setText("Anchura util: " +
                                    form_numbers.format(getMetersBetweenAnchors(anchor1, anchor2)));
                            counter.setAnchuraPlanoTrabajo(getMetersBetweenAnchors(anchor1, anchor2) * 100f);
                            counter.setAnchuraEspacioInferiorLibre(counter.getAnchuraPlanoTrabajo() * 100f);

                            img_instr.setImageResource(R.drawable.mostrador_02);
                            data.setText(R.string.instr_mostrador_02);
                        } else {
                            ledge_depth.setText("Profundidad repisa: " +
                                    form_numbers.format(getMetersBetweenAnchors(anchor1, anchor2)));

                            counter.setProfundidadEspacioInferiorLibre(getMetersBetweenAnchors(anchor1, anchor2) * 100f);

                            img_instr.setImageResource(R.drawable.mostrador_04);
                            data.setText(R.string.instr_mostrador_04);
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

        ledgeDialog();
    }

    /**
     * Function to raise an object perpendicular to the ArPlane a specific distance
     * @param an anchor belonging to the object that should be raised
     * @param up distance in centimeters the object should be raised vertically
     */
    void ascend(AnchorNode an, float up) {
        Anchor anchor = myhit.getTrackable().createAnchor(
                myhit.getHitPose().compose(Pose.makeTranslation(0, up / 100f, 0)));

        an.setAnchor(anchor);
    }

    /**
     * Dialog to ask user whether the counter has a ledge or not
     */
    void ledgeDialog() {
        int[] spinnerImages = new int[]{R.drawable.mostrador_most
                , R.drawable.mostrador_most_salida};

        String[] spinnerPopulation = new String[]{"Mostrador plano", "Mostrador con ledge"};


        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MeasureCounter.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_single_spinner, null);
        mBuilder.setTitle("Selecciona counter");
        Spinner mSpinner = (Spinner) mView.findViewById(R.id.spinner);
        ImageTitleAdapter mCustomAdapter = new ImageTitleAdapter(MeasureCounter.this, spinnerImages, spinnerPopulation);
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
                    ledge = true;
                    confirm.setText("Next");
                } else {
                    ledge_height.setVisibility(View.INVISIBLE);
                    ledge_depth.setVisibility(View.INVISIBLE);
                }
            }
        });

        mBuilder.setView(mView);
        AlertDialog dialog = mBuilder.create();

        dialog.show();
    }

    /**
     * Function to return the distance in meters between two objects placed in ArPlane
     * @param anchor1 first object's anchor
     * @param anchor2 second object's anchor
     * @return the distance between the two anchors in meters
     */
    float getMetersBetweenAnchors(Anchor anchor1, Anchor anchor2) {
        float[] distance_vector = anchor1.getPose().inverse()
                .compose(anchor2.getPose()).getTranslation();
        float totalDistanceSquared = 0;
        for (int i = 0; i < 3; ++i)
            totalDistanceSquared += distance_vector[i] * distance_vector[i];
        return (float) Math.sqrt(totalDistanceSquared);
    }

    /**
     * Set the layout to start measuring the counter's ledge
     */
    void startMeasureLedge() {
        anchor1 = null;
        anchor2 = null;
        sk_height_control.setEnabled(false);
        sk_height_control.setProgress(0);
        confirm.setEnabled(false);
        confirm.setText("Confirm");
        img_instr.setImageResource(R.drawable.mostrador_03);
        data.setText(R.string.instr_mostrador_03);
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
    public static boolean checkIsSupportedDeviceOrFinish(final Activity activity) {
        if (Build.VERSION.SDK_INT < VERSION_CODES.N) {
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

    /**
     * Check whether the counter is accessible or not and start Axesibility activity to display the
     * result
     */
    private void Confirmar() {
        String s = "";

        boolean cumple_anpt = Evaluator.IsGreaterThan(counter.getAnchuraPlanoTrabajo(), db_counter_width);
        s = UpdateStringIfNeeded(s, getString(R.string.mostr_n_anpt) + db_counter_width, cumple_anpt);

        boolean cumple_alpt = Evaluator.IsLowerThan(counter.getAlturaPlanoTrabajo(), db_counter_height);
        s = UpdateStringIfNeeded(s, "y", s == "" || cumple_alpt);
        s = UpdateStringIfNeeded(s, getString(R.string.mostr_n_alpt) + db_counter_height, cumple_alpt);

        if (ledge) {
            boolean cumple_aleif = Evaluator.IsGreaterThan(counter.getAlturaEspacioInferiorLibre(), db_ledge_height);
            s = UpdateStringIfNeeded(s, "y", s == "" || cumple_aleif);
            s = UpdateStringIfNeeded(s, getString(R.string.mostr_n_aleil) + db_ledge_height, cumple_aleif);

            boolean cumple_aneif = Evaluator.IsGreaterThan(counter.getAnchuraEspacioInferiorLibre(), db_ledge_width);
            s = UpdateStringIfNeeded(s, "y", s == "" || cumple_aneif);
            s = UpdateStringIfNeeded(s, getString(R.string.mostr_n_aneil) + db_ledge_width, cumple_aneif);

            boolean cumple_peif = Evaluator.IsGreaterThan(counter.getProfundidadEspacioInferiorLibre(), db_ledge_depth);
            s = UpdateStringIfNeeded(s, "y", s == "" || cumple_peif);
            s = UpdateStringIfNeeded(s, getString(R.string.mostr_n_peil) + db_ledge_depth, cumple_peif);

            counter.setAccesible(cumple_aleif && cumple_anpt && cumple_alpt && cumple_aneif && cumple_peif);

        } else
            counter.setAccesible(cumple_anpt && cumple_alpt );

        UpdateMessage(counter.getAccesible(), s);
        counter.setMensaje(message);

        Intent i = new Intent(this, AxesibilityActivity.class);
        i.putExtra(TypesManager.OBS_TYPE, TypesManager.obsType.MOSTRADORES.getValue());
        i.putExtra(TypesManager.MOSTRADOR_OBS, counter);

        startActivity(i);
        finish();
    }

    /**
     * Get values of accessibility standards from database
     */
    private void UpdateDatabaseValues() {
        final DatabaseReference anchuraPTDB = FirebaseDatabase.getInstance().getReference("Estandares/Mostradores/AnchuraPlanoTrabajo");

        anchuraPTDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                db_counter_width = dataSnapshot.getValue(Float.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final DatabaseReference alturaPTDB = FirebaseDatabase.getInstance().getReference("Estandares/Mostradores/AlturaPlanoTrabajo");

        alturaPTDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                db_counter_height = dataSnapshot.getValue(Float.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final DatabaseReference alturaEILDB = FirebaseDatabase.getInstance().getReference("Estandares/Mostradores/AlturaEspacioInfLibre");

        alturaEILDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                db_ledge_height = dataSnapshot.getValue(Float.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final DatabaseReference anchuraEILDB = FirebaseDatabase.getInstance().getReference("Estandares/Mostradores/AnchuraEspacioInfLibre");

        anchuraEILDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                db_ledge_width = dataSnapshot.getValue(Float.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final DatabaseReference profundidadEILDB = FirebaseDatabase.getInstance().getReference("Estandares/Mostradores/ProfundidadEspacioInfLibre");

        profundidadEILDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                db_ledge_depth = dataSnapshot.getValue(Float.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * Add string to another string if a condition is met
     * @param base initial base string
     * @param to_add string that will be added to the base if the condition is met
     * @param condition condition that determines whether the string is altered or not
     * @return result string
     */
    private String UpdateStringIfNeeded(String base, String to_add, boolean condition) {
        return condition ? base : base + " " + to_add;
    }

    /**
     * Updates message to show whether an element is accessible or not with an explanation
     * @param condition boolean determining whether the element is accessible or not
     * @param aux explanation of the accessibility result
     */
    private void UpdateMessage(boolean condition, String aux) {
        message = condition ? getString(R.string.accesible) : getString(R.string.no_accesible);
        message += aux;
    }

    /**
     * Reset layout to its initial state
     */
    private void resetLayout(){
        anchor1 = null;
        anchor2 = null;
        measuring_ledge = false;
        sk_height_control.setProgress(0);
        sk_height_control.setEnabled(false);
        confirm.setEnabled(false);
        if (ledge)
            confirm.setText("Next");
        img_instr.setImageResource(R.drawable.mostrador_01);
        data.setText(R.string.instr_mostrador_01);
        counter_width.setText("Anchura util: --");
        counter_height.setText("Altura trabajo: --");
        ledge_depth.setText("Profundidad ledge: --");
        ledge_height.setText("Altura ledge: --");
        for (AnchorNode n : anchorNodes) {
            arFragment.getArSceneView().getScene().removeChild(n);
            n.getAnchor().detach();
            n.setParent(null);
            n = null;
        }
    }

}