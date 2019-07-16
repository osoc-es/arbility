package com.osoc.oncera;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
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
import com.osoc.oncera.adapters.ImageTitleAdapter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.atan2;

public class MedirMostradorActivity extends AppCompatActivity {

    private static final String TAG = MeasureActivity.class.getSimpleName();
    private static final double MIN_OPENGL_VERSION = 3.0;
    private float upDistance=0f;
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

        setContentView(R.layout.activity_medir_mostrador);
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);
        restart = (Button)findViewById(R.id.btn_restart);
        confirm = (Button)findViewById(R.id.btn_ok);
        data = (TextView) findViewById(R.id.tv_distance);
        ancho_util = (TextView) findViewById(R.id.ancho_util);
        alto_trabajo = (TextView) findViewById(R.id.alto_trabajo);
        profundo_repisa = (TextView) findViewById(R.id.profundo_repisa);
        alto_repisa = (TextView) findViewById(R.id.alto_repisa);
        z_axis = (SeekBar) findViewById(R.id.z_axis);
        anchorNodes = new ArrayList<>();

        z_axis.setEnabled(false);
        confirm.setEnabled(false);


        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                anchor1=null;
                anchor2=null;
                medir_respisa = false;
                z_axis.setProgress(0);
                z_axis.setEnabled(false);
                confirm.setEnabled(false);
                if(repisa)
                    confirm.setText("Next");
                data.setText("Haz click en las esquinas inferiores del mostrador tras calibrar");
                ancho_util.setText("Anchura util: --");
                alto_trabajo.setText("Altura trabajo: --");
                profundo_repisa.setText("Profundidad repisa: --");
                alto_repisa.setText("Altura repisa: --");
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
                if(repisa&&!medir_respisa){
                    medir_respisa = true;
                    resetMedirRespisa();
                }
                else{
                    Toast.makeText(MedirMostradorActivity.this, "Confirmado", Toast.LENGTH_SHORT).show();
                    //TODO create door model and send accesibility status to next activity
                }
            }
        });


        z_axis.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                upDistance = progress;
                ascend(myanchornode, upDistance);
                confirm.setEnabled(true);
                if(!medir_respisa)
                    alto_trabajo.setText("Altura trabajo: " +
                            form_numbers.format(progress/100f));
                else
                    alto_repisa.setText("Altura repisa: " +
                            form_numbers.format(progress/100f));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
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
                        z_axis.setEnabled(true);
                        if(!medir_respisa){
                            ancho_util.setText("Anchura util: " +
                                    form_numbers.format(getMetersBetweenAnchors(anchor1, anchor2)));

                            data.setText("Sube el cubo con el deslizador hasta que su base de con el tope del mostrador");
                        }
                        else {
                            profundo_repisa.setText("Profundidad repisa: " +
                                    form_numbers.format(getMetersBetweenAnchors(anchor1, anchor2)));

                            data.setText("Sube el cubo con el deslizador hasta que su base de con el lado inferior de la repisa");
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

    void ascend(AnchorNode an, float up){
        Anchor anchor =  myhit.getTrackable().createAnchor(
                myhit.getHitPose().compose(Pose.makeTranslation(0, up/100f, 0)));

        an.setAnchor(anchor);
    }

    void repisaDialog(){
        int[] spinnerImages = new int[]{R.drawable.mostrador_most
                , R.drawable.mostrador_most_salida};

        String[] spinnerPopulation = new String[]{"Mostrador plano", "Mostrador con repisa"};


        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MedirMostradorActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_door, null);
        mBuilder.setTitle("Selecciona mostrador");
        Spinner mSpinner = (Spinner) mView.findViewById(R.id.spinner);
        ImageTitleAdapter mCustomAdapter = new ImageTitleAdapter(MedirMostradorActivity.this, spinnerImages, spinnerPopulation);
        mSpinner.setAdapter(mCustomAdapter);


        mBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i){
                dialogInterface.dismiss();
            }
        });

        mBuilder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if(mSpinner.getSelectedItemPosition() == 1) {
                    repisa = true;
                    confirm.setText("Next");
                }
                else {
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
        for(int i=0; i<3; ++i)
            totalDistanceSquared += distance_vector[i]*distance_vector[i];
        return (float) Math.sqrt(totalDistanceSquared);
    }

    void resetMedirRespisa(){
        anchor1=null;
        anchor2=null;
        z_axis.setEnabled(false);
        z_axis.setProgress(0);
        confirm.setEnabled(false);
        confirm.setText("Confirm");
        data.setText("Delimita el hueco util de repisa con dos puntos desde un lado");
        for(AnchorNode n : anchorNodes){
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
}