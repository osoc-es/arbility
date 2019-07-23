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

import com.google.android.gms.common.util.ArrayUtils;
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
import com.osoc.oncera.javabean.Puerta;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MeasureDoor extends AppCompatActivity {

    private static final String TAG = MeasureDoor.class.getSimpleName();
    private static final double MIN_OPENGL_VERSION = 3.0;
    private float upDistance = 0f;
    private ArFragment arFragment;
    private ModelRenderable andyRenderable;
    private Anchor myanchor;
    private AnchorNode myanchornode;
    private DecimalFormat form_numbers = new DecimalFormat("#0.00");

    private Anchor anchor1 = null, anchor2 = null;
    private ImageView img_instr;

    private HitResult myhit;

    private String[] tipoPuerta = new String[]{"Giratoria", "Corredera", "Abatible", "Tornos"};
    private String[] tipoMecanismo = new String[]{"Manibela", "Pomo", "Barra", "Agarrador"};
    private boolean measure_height = false;

    private float paramAltura,paramAnchura,minMecApertura,maxMecApertura;
    private String message;

    private Puerta door = new Puerta(-1, -1, null, -1, null, null, null, null, null);


    @Override
    @SuppressWarnings({"AndroidApiChecker", "FutureReturnValueIgnored"})
    // CompletableFuture requires api level 24
    // FutureReturnValueIgnored is not valid
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!checkIsSupportedDeviceOrFinish(this)) {
            return;
        }

        setContentView(R.layout.activity_measure_door);

       GetDBValues();

        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);
        Button restart = (Button) findViewById(R.id.btn_restart);
        Button confirm = (Button) findViewById(R.id.btn_ok);
        TextView data = (TextView) findViewById(R.id.tv_distance);
        TextView width = (TextView) findViewById(R.id.width);
        TextView mechanism = (TextView) findViewById(R.id.height_mecha);
        TextView height = (TextView) findViewById(R.id.height);
        SeekBar z_axis = (SeekBar) findViewById(R.id.sk_height_control);
        ImageButton btnBack = (ImageButton) findViewById(R.id.btnBack);
        img_instr = (ImageView) findViewById(R.id.img_instr);
        List<AnchorNode> anchorNodes = new ArrayList<>();

        z_axis.setEnabled(false);
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
                z_axis.setProgress(0);
                z_axis.setEnabled(false);
                confirm.setEnabled(false);
                confirm.setText("Next");
                data.setText(R.string.instr_puerta_01);
                img_instr.setImageResource(R.drawable.puerta_01);
                width.setText("Anchura puerta: --");
                mechanism.setText("Altura mecanismo: --");
                height.setText("Altura puerta: --");
                measure_height = false;
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
                Toast.makeText(MeasureDoor.this, "Confirmado", Toast.LENGTH_SHORT).show();
                if (!measure_height) {
                    measure_height = true;
                    img_instr.setImageResource(R.drawable.puerta_03);
                    data.setText(R.string.instr_puerta_03);
                    confirm.setEnabled(false);
                    confirm.setText("Confirm");
                } else
                    Confirmar();
            }
        });


        z_axis.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                upDistance = progress;
                ascend(myanchornode, upDistance);
                if (measure_height) {
                    height.setText("Altura puerta: " +
                            form_numbers.format(progress / 100f));
                    door.setAltura(progress);
                }
                else {
                    mechanism.setText("Altura mecanismo: " +
                            form_numbers.format(progress / 100f));
                    door.setAlturaPomo(progress);
                }
                confirm.setEnabled(true);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

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
                        width.setText("Anchura puerta: " +
                                form_numbers.format(getMetersBetweenAnchors(anchor1, anchor2)));

                        door.setAnchura((int)(getMetersBetweenAnchors(anchor1, anchor2)*100));

                        img_instr.setImageResource(R.drawable.puerta_02);
                        data.setText(R.string.instr_puerta_02);

                        z_axis.setEnabled(true);
                    }
                    myanchornode = anchorNode;

                    // Create the transformable andy and add it to the anchor.
                    TransformableNode andy = new TransformableNode(arFragment.getTransformationSystem());
                    andy.setParent(anchorNode);
                    andy.setRenderable(andyRenderable);
                    andy.select();
                    andy.getScaleController().setEnabled(false);
                });
        puertaDialog();
    }

    /**
     * Get values of accessibility standards from database
     */
    private void GetDBValues() {

        final DatabaseReference alt = FirebaseDatabase.getInstance().getReference("Estandares/Puertas/Altura");

        alt.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                paramAltura = dataSnapshot.getValue(Float.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final DatabaseReference anch = FirebaseDatabase.getInstance().getReference("Estandares/Puertas/Anchura");

        anch.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                paramAnchura = dataSnapshot.getValue(Float.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final DatabaseReference minMec = FirebaseDatabase.getInstance().getReference("Estandares/Puertas/minMecApertura");

        minMec.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                minMecApertura = dataSnapshot.getValue(Float.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final DatabaseReference maxMec = FirebaseDatabase.getInstance().getReference("Estandares/Puertas/maxMecApertura");

        maxMec.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                maxMecApertura = dataSnapshot.getValue(Float.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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
     * Check whether the counter is accessible or not and start Axesibility activity to display the
     * result
     */
    void Confirmar()
    {
        String s = "";

        boolean cumple_altura = Evaluator.IsGreaterThan(door.getAltura(), paramAltura);
        s = UpdateStringIfNeeded(s, getString(R.string.puerta_n_altura) + paramAltura, cumple_altura);

        boolean cumple_anchura = Evaluator.IsGreaterThan(door.getAnchura(),paramAnchura);
        s = UpdateStringIfNeeded(s, "y", s == "" || cumple_anchura);
        s = UpdateStringIfNeeded(s, getString(R.string.puerta_n_ancho) + paramAnchura, cumple_anchura);


        boolean cumple_tipo_puerta = ArrayUtils.contains(new String[]{"Abatible", "Tornos"}, door.getTipoPuerta());
        s = UpdateStringIfNeeded(s, "y", s == "" || cumple_tipo_puerta);
        s = UpdateStringIfNeeded(s, getString(R.string.puerta_n_tipo_puerta), cumple_tipo_puerta);


        boolean cumple_tipo_mecanismos = ArrayUtils.contains(new String[]{"Manibela", "Barra", "Agarrador"}, door.getTipoMecanismo());
        s = UpdateStringIfNeeded(s, "y", s == "" || cumple_tipo_mecanismos);
        s = UpdateStringIfNeeded(s, getString(R.string.puerta_n_tipo_mec), cumple_tipo_mecanismos);

        boolean cumple_alto_mecanismo = Evaluator.IsInRange(door.getAlturaPomo(), minMecApertura, maxMecApertura);
        s = UpdateStringIfNeeded(s, "y", s == "" || cumple_alto_mecanismo);
        s = UpdateStringIfNeeded(s, getString(R.string.puerta_n_altura_mec) + minMecApertura + " y " + maxMecApertura, cumple_alto_mecanismo);


        door.setAccesible(cumple_altura && cumple_altura && cumple_anchura && cumple_tipo_mecanismos && cumple_tipo_puerta && cumple_alto_mecanismo);

        UpdateMessage(door.getAccesible(), s);
        door.setMensaje(message);

        Intent i = new Intent(this, AxesibilityActivity.class);
        i.putExtra(TypesManager.OBS_TYPE, TypesManager.obsType.PUERTAS.getValue());
        i.putExtra(TypesManager.PUERTAS_OBS, door);

        startActivity(i);
        finish();
    }

    /**
     * Dialog to ask user for the type of door and opening mechanism
     */
    void puertaDialog() {
        int[] spinnerImages = new int[]{R.drawable.puerta_giratoria, R.drawable.puerta_corredera
                , R.drawable.puerta_abatible, R.drawable.puerta_torno};


        int[] spinnerImages2 = new int[]{R.drawable.mecanismo_manibela, R.drawable.mecanismo_pomo
                , R.drawable.mecanismo_barra, R.drawable.mecanismo_agarrador};


        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MeasureDoor.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_door, null);
        mBuilder.setTitle("Selecciona puerta y pomo");
        Spinner mSpinnerDoor = (Spinner) mView.findViewById(R.id.spinner_puerta);
        Spinner mSpinnerMecha = (Spinner) mView.findViewById(R.id.spinner_mecanismo);

        ImageTitleAdapter mCustomAdapter = new ImageTitleAdapter(MeasureDoor.this, spinnerImages, tipoPuerta);
        mSpinnerDoor.setAdapter(mCustomAdapter);

        ImageTitleAdapter mCustomAdapter2 = new ImageTitleAdapter(MeasureDoor.this, spinnerImages2, tipoMecanismo);
        mSpinnerMecha.setAdapter(mCustomAdapter2);


        mBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        mBuilder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                door.setTipoPuerta(tipoPuerta[mSpinnerDoor.getSelectedItemPosition()]);
                door.setTipoMecanismo(tipoMecanismo[mSpinnerMecha.getSelectedItemPosition()]);
            }
        });

        mBuilder.setView(mView);
        AlertDialog dialog = mBuilder.create();

        dialog.show();
    }

    /**
     * Check whether the device supports the tools required to use the measurement tools
     * @param activity
     * @return boolean determining whether the device is supported or not
     */
    private boolean checkIsSupportedDeviceOrFinish(final Activity activity) {
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
        message = condition? getString(R.string.accesible) : getString(R.string.no_accesible);
        message += aux;
    }

}
