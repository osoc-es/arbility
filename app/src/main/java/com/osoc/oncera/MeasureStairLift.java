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
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.osoc.oncera.javabean.StairLifter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MeasureStairLift extends AppCompatActivity {
    private static final String TAG = MeasureDoor.class.getSimpleName();
    private static final double MIN_OPENGL_VERSION = 3.0;
    private ArFragment arFragment;
    private ModelRenderable andyRenderable;
    private DecimalFormat form_numbers = new DecimalFormat("#0.00");

    private List<AnchorNode> anchorNodes;

    private boolean measuring_length = false;

    private boolean controls = false, supportedWeight = false, speed = false;

    private float paramAnch;
    private float paramLargo;

    private String message;

    Button restart;
    Button confirm;
    TextView data;
    TextView platform_width;
    TextView platform_length;
    private ImageView img_instr;

    private Anchor anchor1 = null, anchor2 = null;

    private StairLifter stairLifter = new StairLifter(null,null,null,null,null,null,null,null,null,null,null);

    @Override
    @SuppressWarnings({"AndroidApiChecker", "FutureReturnValueIgnored"})
    // CompletableFuture requires api level 24
    // FutureReturnValueIgnored is not valid
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!checkIsSupportedDeviceOrFinish(this)) {
            return;
        }

        setContentView(R.layout.activity_measure_stair_lift);

        final DatabaseReference anch = FirebaseDatabase.getInstance().getReference("Standards/StairLift/Width");

        anch.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                paramAnch = dataSnapshot.getValue(Float.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final DatabaseReference larg = FirebaseDatabase.getInstance().getReference("Standards/StairLift/Length");

        larg.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                paramLargo = dataSnapshot.getValue(Float.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);
        restart = (Button) findViewById(R.id.btn_restart);
        confirm = (Button) findViewById(R.id.btn_ok);
        data = (TextView) findViewById(R.id.tv_distance);

        platform_width = (TextView) findViewById(R.id.platform_width);
        platform_length = (TextView) findViewById(R.id.platform_length);
        ImageButton btnBack = (ImageButton) findViewById(R.id.btnBack);
        img_instr = (ImageView) findViewById(R.id.img_instr);

        anchorNodes = new ArrayList<>();

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
                anchor1 = null;
                anchor2 = null;
                confirm.setEnabled(false);
                confirm.setText("Next");
                data.setText(R.string.instr_salvaescaleras_01);
                img_instr.setImageResource(R.drawable.salvaescaleras_01);

                platform_width.setText("Anchura plataforma: --");
                platform_length.setText("Longitud plataforma: --");

                measuring_length = false;

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
                if (!measuring_length) {
                    measuring_length = true;
                    measureLengthLayout();
                } else {
                    Toast.makeText(MeasureStairLift.this, "Confirmado", Toast.LENGTH_SHORT).show();
                    validate();
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
                    // Create the Anchor.
                    Anchor anchor = hitResult.createAnchor();

                    AnchorNode anchorNode = new AnchorNode(anchor);


                    anchorNode.setParent(arFragment.getArSceneView().getScene());
                    anchorNodes.add(anchorNode);

                    if (anchor1 == null) {
                        anchor1 = anchor;
                    } else {
                        anchor2 = anchor;
                        confirm.setEnabled(true);
                        if (!measuring_length) {
                            platform_width.setText("Anchura plataforma: " +
                                    form_numbers.format(getMetersBetweenAnchors(anchor1, anchor2)));
                            stairLifter.setWidth(getMetersBetweenAnchors(anchor1, anchor2)*100);

                        } else {
                            platform_length.setText("Longitud plataforma: " +
                                    form_numbers.format(getMetersBetweenAnchors(anchor1, anchor2)));
                            stairLifter.setLength(getMetersBetweenAnchors(anchor1, anchor2)*100);
                        }
                    }

                    // Create the transformable andy and add it to the anchor.
                    TransformableNode andy = new TransformableNode(arFragment.getTransformationSystem());
                    andy.setParent(anchorNode);
                    andy.setRenderable(andyRenderable);
                    andy.select();
                    andy.getScaleController().setEnabled(false);
                });
        stairLiftQuestionDialog();
    }

    /**
     * Check whether the stair lift is accessible or not and start Axesibility activity to display
     * the result
     */
    private void validate() {
        String s = "";

        Boolean anch = Evaluator.IsGreaterThan(stairLifter.getWidth(),paramAnch);
        s = UpdateStringIfNeeded(s, getString(R.string.se_n_ancho) + paramAnch, anch);

        Boolean largo = Evaluator.IsGreaterThan(stairLifter.getLength(),paramLargo);
        s = UpdateStringIfNeeded(s, "y", s == "" || largo);
        s = UpdateStringIfNeeded(s, getString(R.string.se_n_largo) + paramLargo, largo);

        s = UpdateStringIfNeeded(s, "y", s == "" || stairLifter.getShipmentControl());
        s = UpdateStringIfNeeded(s, getString(R.string.se_n_mando), stairLifter.getShipmentControl());

        s = UpdateStringIfNeeded(s, "y", s == "" || stairLifter.getCharge());
        s = UpdateStringIfNeeded(s, getString(R.string.se_n_carga), stairLifter.getCharge());

        s = UpdateStringIfNeeded(s, "y", s == "" || stairLifter.getVelocity());
        s = UpdateStringIfNeeded(s, getString(R.string.se_n_velocidad), stairLifter.getVelocity());

        stairLifter.setAccessible(anch && largo && stairLifter.getShipmentControl() && stairLifter.getCharge() && stairLifter.getVelocity());

        UpdateMessage(stairLifter.getAccessible(), s);

        stairLifter.setMessage(message);

        Intent i = new Intent(this,AccessibilityChecker.class);
        i.putExtra(TypesManager.OBS_TYPE,TypesManager.obsType.STAIRLIFTER.getValue());
        i.putExtra(TypesManager.STAIRLIFTER_OBS, stairLifter);

        startActivity(i);
        finish();
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
     * Dialog to ask user whether the stair lift meets certain standards for accessibility
     */
    private void stairLiftQuestionDialog() {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MeasureStairLift.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_chair_lift, null);
        mBuilder.setTitle("Rellena el cuestionario");

        CheckBox chkMando = (CheckBox) mView.findViewById(R.id.chkMando);
        CheckBox chkCarga = (CheckBox) mView.findViewById(R.id.chkCarga);
        CheckBox chkVelocidad = (CheckBox) mView.findViewById(R.id.chkVelocidad);


        mBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        mBuilder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                controls = chkMando.isChecked();
                supportedWeight = chkCarga.isChecked();
                speed = chkVelocidad.isChecked();

                stairLifter.setShipmentControl(controls);
                stairLifter.setCharge(supportedWeight);
                stairLifter.setVelocity(speed);
            }
        });

        mBuilder.setView(mView);
        AlertDialog dialog = mBuilder.create();

        dialog.show();
    }

    /**
     * Prepare layout to start measuring the stairlift's length
     */
    private void measureLengthLayout() {
        anchor1 = null;
        anchor2 = null;
        confirm.setEnabled(false);
        confirm.setText("Confirm");
        data.setText(R.string.instr_salvaescaleras_02);
        img_instr.setImageResource(R.drawable.salvaescaleras_02);
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

    /**
     * Add string to another string if a condition is met
     * @param base initial base string
     * @param to_add string that will be added to the base if the condition is met
     * @param condition condition that determines whether the string is altered or not
     * @return result string
     */
    private String UpdateStringIfNeeded(String base, String to_add, boolean condition)
    {
        return condition ? base : base + " " + to_add;
    }

    /**
     * Updates message to show whether an element is accessible or not with an explanation
     * @param condition boolean determining whether the element is accessible or not
     * @param aux explanation of the accessibility result
     */
    private void UpdateMessage(boolean condition, String aux)
    {
        message = condition? getString(R.string.accessible) : getString(R.string.no_accesible);
        message += aux;
    }
}
