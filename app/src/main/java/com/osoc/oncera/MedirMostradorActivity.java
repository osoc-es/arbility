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
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
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

import static java.lang.Math.atan2;

public class MedirMostradorActivity extends AppCompatActivity {

    private static final String TAG = MeasureActivity.class.getSimpleName();
    private static final double MIN_OPENGL_VERSION = 3.0;
    private float upDistance = 0f;
    private ArFragment arFragment;
    private ModelRenderable andyRenderable;
    private Anchor myanchor;
    private AnchorNode myanchornode;
    private DecimalFormat form_numbers = new DecimalFormat("#0.00");

    private List<AnchorNode> anchorNodes;

    private boolean repisa = false;
    private boolean medir_respisa = false;

    Button restart;
    Button confirm;
    TextView data;
    TextView ancho_util;
    TextView alto_trabajo;
    TextView profundo_repisa;
    TextView alto_repisa;
    SeekBar z_axis;
    private ImageView img_instr;

    private String message;

    private Anchor anchor1 = null, anchor2 = null;

    private HitResult myhit;

    private PuntosAtencion mostrador = new PuntosAtencion(null, null, null, null, null, null, null, null, null);

    float db_anch_plano_trabajo, db_alt_plano_trabajo, db_alt_esp_inf_libre, db_anch_esp_inf_libre, db_prof_esp_inf_libre;

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

        setContentView(R.layout.activity_medir_mostrador);
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);
        restart = (Button) findViewById(R.id.btn_restart);
        confirm = (Button) findViewById(R.id.btn_ok);
        data = (TextView) findViewById(R.id.tv_distance);
        ancho_util = (TextView) findViewById(R.id.ancho_util);
        alto_trabajo = (TextView) findViewById(R.id.alto_trabajo);
        profundo_repisa = (TextView) findViewById(R.id.profundo_repisa);
        alto_repisa = (TextView) findViewById(R.id.alto_repisa);
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
                medir_respisa = false;
                z_axis.setProgress(0);
                z_axis.setEnabled(false);
                confirm.setEnabled(false);
                if (repisa)
                    confirm.setText("Next");
                img_instr.setImageResource(R.drawable.mostrador_01);
                data.setText(R.string.instr_mostrador_01);
                ancho_util.setText("Anchura util: --");
                alto_trabajo.setText("Altura trabajo: --");
                profundo_repisa.setText("Profundidad repisa: --");
                alto_repisa.setText("Altura repisa: --");
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
                if (repisa && !medir_respisa) {
                    medir_respisa = true;
                    resetMedirRespisa();
                } else {
                    Toast.makeText(MedirMostradorActivity.this, "Confirmado", Toast.LENGTH_SHORT).show();
                    Confirmar();
                }
            }
        });


        z_axis.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                upDistance = progress;
                ascend(myanchornode, upDistance);
                confirm.setEnabled(true);
                if (!medir_respisa) {
                    alto_trabajo.setText("Altura trabajo: " +
                            form_numbers.format(progress / 100f));
                    mostrador.setAlturaPlanoTrabajo((float) progress);
                } else {
                    alto_repisa.setText("Altura repisa: " +
                            form_numbers.format(progress / 100f));
                    mostrador.setAlturaEspacioInferiorLibre((float) progress);
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
                        z_axis.setEnabled(true);
                        if (!medir_respisa) {
                            ancho_util.setText("Anchura util: " +
                                    form_numbers.format(getMetersBetweenAnchors(anchor1, anchor2)));
                            mostrador.setAnchuraPlanoTrabajo(getMetersBetweenAnchors(anchor1, anchor2) * 100f);
                            mostrador.setAnchuraEspacioInferiorLibre(mostrador.getAnchuraPlanoTrabajo() * 100f);

                            img_instr.setImageResource(R.drawable.mostrador_02);
                            data.setText(R.string.instr_mostrador_02);
                        } else {
                            profundo_repisa.setText("Profundidad repisa: " +
                                    form_numbers.format(getMetersBetweenAnchors(anchor1, anchor2)));

                            mostrador.setProfundidadEspacioInferiorLibre(getMetersBetweenAnchors(anchor1, anchor2) * 100f);

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

        repisaDialog();
    }

    void ascend(AnchorNode an, float up) {
        Anchor anchor = myhit.getTrackable().createAnchor(
                myhit.getHitPose().compose(Pose.makeTranslation(0, up / 100f, 0)));

        an.setAnchor(anchor);
    }

    void repisaDialog() {
        int[] spinnerImages = new int[]{R.drawable.mostrador_most
                , R.drawable.mostrador_most_salida};

        String[] spinnerPopulation = new String[]{"Mostrador plano", "Mostrador con repisa"};


        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MedirMostradorActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_door, null);
        mBuilder.setTitle("Selecciona mostrador");
        Spinner mSpinner = (Spinner) mView.findViewById(R.id.spinner);
        ImageTitleAdapter mCustomAdapter = new ImageTitleAdapter(MedirMostradorActivity.this, spinnerImages, spinnerPopulation);
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
                    repisa = true;
                    confirm.setText("Next");
                } else {
                    alto_repisa.setVisibility(View.INVISIBLE);
                    profundo_repisa.setVisibility(View.INVISIBLE);
                }
            }
        });

        mBuilder.setView(mView);
        AlertDialog dialog = mBuilder.create();

        dialog.show();
    }

    float getMetersBetweenAnchors(Anchor anchor1, Anchor anchor2) {
        float[] distance_vector = anchor1.getPose().inverse()
                .compose(anchor2.getPose()).getTranslation();
        float totalDistanceSquared = 0;
        for (int i = 0; i < 3; ++i)
            totalDistanceSquared += distance_vector[i] * distance_vector[i];
        return (float) Math.sqrt(totalDistanceSquared);
    }

    void resetMedirRespisa() {
        anchor1 = null;
        anchor2 = null;
        z_axis.setEnabled(false);
        z_axis.setProgress(0);
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

    private void Confirmar() {
        String s = "";

        boolean cumple_anpt = Evaluator.IsGreaterThan(mostrador.getAnchuraPlanoTrabajo(), db_anch_plano_trabajo);
        s = UpdateStringIfNeeded(s, getString(R.string.mostr_n_anpt) + db_anch_plano_trabajo, cumple_anpt);

        boolean cumple_alpt = Evaluator.IsLowerThan(mostrador.getAlturaPlanoTrabajo(), db_alt_plano_trabajo);
        s = UpdateStringIfNeeded(s, "y", s == "" || cumple_alpt);
        s = UpdateStringIfNeeded(s, getString(R.string.mostr_n_alpt) + db_alt_plano_trabajo, cumple_alpt);

        if (repisa) {
            boolean cumple_aleif = Evaluator.IsGreaterThan(mostrador.getAlturaEspacioInferiorLibre(), db_alt_esp_inf_libre);
            s = UpdateStringIfNeeded(s, "y", s == "" || cumple_aleif);
            s = UpdateStringIfNeeded(s, getString(R.string.mostr_n_aleil) + db_alt_esp_inf_libre, cumple_aleif);

            boolean cumple_aneif = Evaluator.IsGreaterThan(mostrador.getAnchuraEspacioInferiorLibre(), db_anch_esp_inf_libre);
            s = UpdateStringIfNeeded(s, "y", s == "" || cumple_aneif);
            s = UpdateStringIfNeeded(s, getString(R.string.mostr_n_aneil) + db_anch_esp_inf_libre, cumple_aneif);

            boolean cumple_peif = Evaluator.IsGreaterThan(mostrador.getProfundidadEspacioInferiorLibre(), db_prof_esp_inf_libre);
            s = UpdateStringIfNeeded(s, "y", s == "" || cumple_peif);
            s = UpdateStringIfNeeded(s, getString(R.string.mostr_n_peil) + db_prof_esp_inf_libre, cumple_peif);

            mostrador.setAccesible(cumple_aleif && cumple_anpt && cumple_alpt && cumple_aneif && cumple_peif);

        } else
            mostrador.setAccesible(cumple_anpt && cumple_alpt );

        UpdateMessage(mostrador.getAccesible(), s);
        mostrador.setMensaje(message);

        Intent i = new Intent(this, AxesibilityActivity.class);
        i.putExtra(TypesManager.OBS_TYPE, TypesManager.obsType.MOSTRADORES.getValue());
        i.putExtra(TypesManager.MOSTRADOR_OBS, mostrador);

        startActivity(i);
        finish();

    }

    private void UpdateDatabaseValues() {
        final DatabaseReference anchuraPTDB = FirebaseDatabase.getInstance().getReference("Estandares/Mostradores/AnchuraPlanoTrabajo");

        anchuraPTDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                db_anch_plano_trabajo = dataSnapshot.getValue(Float.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final DatabaseReference alturaPTDB = FirebaseDatabase.getInstance().getReference("Estandares/Mostradores/AlturaPlanoTrabajo");

        alturaPTDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                db_alt_plano_trabajo = dataSnapshot.getValue(Float.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final DatabaseReference alturaEILDB = FirebaseDatabase.getInstance().getReference("Estandares/Mostradores/AlturaEspacioInfLibre");

        alturaEILDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                db_alt_esp_inf_libre = dataSnapshot.getValue(Float.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final DatabaseReference anchuraEILDB = FirebaseDatabase.getInstance().getReference("Estandares/Mostradores/AnchuraEspacioInfLibre");

        anchuraEILDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                db_anch_esp_inf_libre = dataSnapshot.getValue(Float.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final DatabaseReference profundidadEILDB = FirebaseDatabase.getInstance().getReference("Estandares/Mostradores/ProfundidadEspacioInfLibre");

        profundidadEILDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                db_prof_esp_inf_libre = dataSnapshot.getValue(Float.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private String UpdateStringIfNeeded(String base, String to_add, boolean condition) {
        return condition ? base : base + " " + to_add;
    }

    private void UpdateMessage(boolean condition, String aux) {
        message = condition ? getString(R.string.accesible) : getString(R.string.no_accesible);
        message += aux;
    }

}