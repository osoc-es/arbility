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
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.osoc.oncera.javabean.Ascensores;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MeasureLift extends AppCompatActivity {

    private static final String TAG = MeasureDoor.class.getSimpleName();
    private static final double MIN_OPENGL_VERSION = 3.0;
    private float upDistance=0f;
    private ArFragment arFragment;
    private ModelRenderable andyRenderable;
    private Anchor myanchor;
    private AnchorNode myanchornode;
    private DecimalFormat form_numbers = new DecimalFormat("#0.00");
    private float paramAnchura;
    private float paramProf;
    private ImageView img_instr;

    private List<AnchorNode> anchorNodes;

    private boolean medir_profundidad = false;

    private boolean braile = false, automatico = false, sonido = false, hueco = false, escalon = false;

    private Ascensores ascensor = new Ascensores(null,null,null,null,null,null,null,null,null,null, null);

    Button restart;
    Button confirm;
    TextView data;
    TextView ancho_ascensor;
    TextView profundo_ascensor;
    private String message;

    private Anchor anchor1=null, anchor2=null;

    private HitResult myhit;

    @Override
    @SuppressWarnings({"AndroidApiChecker", "FutureReturnValueIgnored"})
    // CompletableFuture requires api level 24
    // FutureReturnValueIgnored is not valid
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!checkIsSupportedDeviceOrFinish(this)) {
            return;
        }

        setContentView(R.layout.activity_measure_lift);

        final DatabaseReference anch = FirebaseDatabase.getInstance().getReference("Estandares/Ascensores/Anchura");

        anch.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                paramAnchura = dataSnapshot.getValue(Float.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final DatabaseReference prof = FirebaseDatabase.getInstance().getReference("Estandares/Ascensores/Profundidad");

        prof.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                paramProf = dataSnapshot.getValue(Float.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);
        restart = (Button)findViewById(R.id.btn_restart);
        confirm = (Button)findViewById(R.id.btn_ok);
        data = (TextView) findViewById(R.id.tv_distance);

        ancho_ascensor = (TextView) findViewById(R.id.ancho_acensor);
        profundo_ascensor = (TextView) findViewById(R.id.profundo_ascensor);
        ImageButton btnAtras = (ImageButton) findViewById(R.id.btnBack);
        img_instr = (ImageView) findViewById(R.id.img_instr);

        anchorNodes = new ArrayList<>();

        confirm.setEnabled(false);


        btnAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                anchor1=null;
                anchor2=null;
                confirm.setEnabled(false);
                confirm.setText("Next");
                img_instr.setImageResource(R.drawable.ascensor_01);
                data.setText(R.string.instr_ascensor_01);

                ancho_ascensor.setText("Anchura ascensor: --");
                profundo_ascensor.setText("Profundidad ascensor: --");

                medir_profundidad = false;

                for(AnchorNode n : anchorNodes){
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
                if(!medir_profundidad){
                    medir_profundidad = true;
                    resetMedirAnchura();
                }
                else{
                    Toast.makeText(MeasureLift.this, "Confirmado", Toast.LENGTH_SHORT).show();
                    validateEvaluation();

                }
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

                    if(anchor1 == null) {
                        anchor1 = anchor;
                    }
                    else {
                        anchor2 = anchor;
                        confirm.setEnabled(true);
                        if(!medir_profundidad){
                            ancho_ascensor.setText("Anchura ascensor: " +
                                    form_numbers.format(getMetersBetweenAnchors(anchor1, anchor2)));
                            ascensor.setAnchuraCabina((getMetersBetweenAnchors(anchor1, anchor2))*100);
                        }
                        else {
                            profundo_ascensor.setText("Profundidad ascensor: " +
                                    form_numbers.format(getMetersBetweenAnchors(anchor1, anchor2)));
                            ascensor.setProfundidadCabina(getMetersBetweenAnchors(anchor1, anchor2)*100);
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

    void ascend(AnchorNode an, float up){
        Anchor anchor =  myhit.getTrackable().createAnchor(
                myhit.getHitPose().compose(Pose.makeTranslation(0, up/100f, 0)));

        an.setAnchor(anchor);
    }


    float getMetersBetweenAnchors(Anchor anchor1, Anchor anchor2) {
        float[] distance_vector = anchor1.getPose().inverse()
                .compose(anchor2.getPose()).getTranslation();
        float totalDistanceSquared = 0;
        for(int i=0; i<3; ++i)
            totalDistanceSquared += distance_vector[i]*distance_vector[i];
        return (float) Math.sqrt(totalDistanceSquared);
    }

    void barandillaDialog(){


        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MeasureLift.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_lift, null);
        mBuilder.setTitle("Rellena el cuestionario");
        CheckBox chkBraile = (CheckBox) mView.findViewById(R.id.chkBraile);
        CheckBox chkSonido = (CheckBox) mView.findViewById(R.id.chkSonido);
        CheckBox chkAutomatico = (CheckBox) mView.findViewById(R.id.chkAutomatico);
        CheckBox chkEscalon = (CheckBox) mView.findViewById(R.id.chkEscalon);
        CheckBox chkHueco = (CheckBox) mView.findViewById(R.id.chkHueco);


        mBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i){
                dialogInterface.dismiss();
            }
        });

        mBuilder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                braile = chkBraile.isChecked();
                automatico = chkAutomatico.isChecked();
                sonido = chkSonido.isChecked();
                hueco = chkHueco.isChecked();
                escalon = chkEscalon.isChecked();

                ascensor.setBotoneraBraile(braile);
                ascensor.setPuertasAutomaticas(automatico);
                ascensor.setSenialAudible(sonido);

            }
        });

        mBuilder.setView(mView);
        AlertDialog dialog = mBuilder.create();

        dialog.show();

    }

    void validateEvaluation(){

        String s = "";

    Boolean anchura = Evaluator.IsGreaterThan(ascensor.getAnchuraCabina(),
            paramAnchura);

        s = UpdateStringIfNeeded(s, getString(R.string.asc_n_anch) + paramAnchura, anchura);

    Boolean prof = Evaluator.IsGreaterThan(ascensor.getProfundidadCabina(),
            paramProf);

        s = UpdateStringIfNeeded(s, "y", s == "" || prof);
        s = UpdateStringIfNeeded(s, getString(R.string.asc_n_prof) + paramProf, prof);


        s = UpdateStringIfNeeded(s, "y", s == "" || braile);
        s = UpdateStringIfNeeded(s, getString(R.string.asc_n_braille), braile);

        s = UpdateStringIfNeeded(s, "y", s == "" || automatico);
        s = UpdateStringIfNeeded(s, getString(R.string.asc_n_automat), automatico);

        s = UpdateStringIfNeeded(s, "y", s == "" || sonido);
        s = UpdateStringIfNeeded(s, getString(R.string.asc_n_audio), sonido);

        s = UpdateStringIfNeeded(s, "y", s == "" || hueco);
        s = UpdateStringIfNeeded(s, getString(R.string.asc_n_dist), hueco);

        s = UpdateStringIfNeeded(s, "y", s == "" || escalon);
        s = UpdateStringIfNeeded(s, getString(R.string.asc_n_alt_resal), automatico);

        ascensor.setAccesible(anchura && prof && braile && automatico && sonido && hueco && escalon);

        UpdateMessage(ascensor.getAccesible(), s);

        ascensor.setMensaje(message);

        Intent i = new Intent(this,AxesibilityActivity.class);
        i.putExtra(TypesManager.OBS_TYPE,TypesManager.obsType.ASCENSORES.getValue());
        i.putExtra(TypesManager.ASCENSOR_OBS, ascensor);

        startActivity(i);
        finish();
    }
    void resetMedirAnchura(){
        anchor1=null;
        anchor2=null;
        confirm.setEnabled(false);
        confirm.setText("Confirm");
        img_instr.setImageResource(R.drawable.ascensor_02);
        data.setText(R.string.instr_ascensor_02);
        for(AnchorNode n : anchorNodes){
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

    private String UpdateStringIfNeeded(String base, String to_add, boolean condition)
    {
        return condition ? base : base + " " + to_add;
    }

    private void UpdateMessage(boolean condition, String aux)
    {
        message = condition? getString(R.string.accesible) : getString(R.string.no_accesible);
        message += aux;
    }
}
