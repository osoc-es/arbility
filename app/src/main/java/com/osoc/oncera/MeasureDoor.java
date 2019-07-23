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
import com.osoc.oncera.javabean.Door;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MeasureDoor extends AppCompatActivity {

    private static final String TAG = MeasureDoor.class.getSimpleName();
    private static final double MIN_OPENGL_VERSION = 3.0;
    private float upDistance = 0f;
    private ArFragment arFragment;
    private ModelRenderable andyRenderable;
    private AnchorNode myanchornode;
    private DecimalFormat form_numbers = new DecimalFormat("#0.00");

    private Anchor anchor1 = null, anchor2 = null;
    private ImageView img_instr;

    private HitResult myhit;

    private String[] doorType = new String[]{"Giratoria", "Corredera", "Abatible", "Tornos"};
    private String[] mecTypes = new String[]{"Manibela", "Pomo", "Barra", "Agarrador"};
    private boolean measure_height = false;

    private float paramHeight,paramWidth, minOpeningMc, maxOpeningMc;
    private String message;

    private Door door_ = new Door(-1, -1, null, -1, null, null, null, null, null);


    @Override
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
        SeekBar z_axis = (SeekBar) findViewById(R.id.z_axis);
        ImageButton backButtn = (ImageButton) findViewById(R.id.btnAtras);
        img_instr = (ImageView) findViewById(R.id.img_instr);
        List<AnchorNode> anchorNodes = new ArrayList<>();

        z_axis.setEnabled(false);
        confirm.setEnabled(false);


        backButtn.setOnClickListener(new View.OnClickListener() {
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
                confirm.setText("Siguiente");
                data.setText(R.string.instr_puerta_01);
                img_instr.setImageResource(R.drawable.puerta_01);
                width.setText("Anchura Puerta: --");
                mechanism.setText("Altura mecanismo: --");
                height.setText("Altura door_: --");
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
                    Confirm();
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
                    door_.setHeight(progress);
                }
                else {
                    mechanism.setText("Altura mecanismo: " +
                            form_numbers.format(progress / 100f));
                    door_.setKnobHeight(progress);
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
                        width.setText("Anchura door_: " +
                                form_numbers.format(getMetersBetweenAnchors(anchor1, anchor2)));

                        door_.setWidth((int)(getMetersBetweenAnchors(anchor1, anchor2)*100));

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
        doorDialog();
    }

    private void GetDBValues() {

        final DatabaseReference alt = FirebaseDatabase.getInstance().getReference("Standards/Door/Height");

        alt.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                paramHeight = dataSnapshot.getValue(Float.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final DatabaseReference anch = FirebaseDatabase.getInstance().getReference("Standards/Door/Width");

        anch.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                paramWidth = dataSnapshot.getValue(Float.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final DatabaseReference minMec = FirebaseDatabase.getInstance().getReference("Standards/Door/minOpeningMec");

        minMec.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                minOpeningMc = dataSnapshot.getValue(Float.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final DatabaseReference maxMec = FirebaseDatabase.getInstance().getReference("Standards/Door/maxOpeningMec");

        maxMec.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                maxOpeningMc = dataSnapshot.getValue(Float.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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

    void Confirm()
    {
        String s = "";

        boolean ffill_Height = Evaluator.IsGreaterThan(door_.getHeight(), paramHeight);
        s = UpdateStringIfNeeded(s, getString(R.string.puerta_n_altura) + paramHeight, ffill_Height);

        boolean ffill_Width = Evaluator.IsGreaterThan(door_.getWidth(),paramWidth);
        s = UpdateStringIfNeeded(s, "y", s == "" || ffill_Width);
        s = UpdateStringIfNeeded(s, getString(R.string.puerta_n_ancho) + paramWidth, ffill_Width);


        boolean ffill_door_type = ArrayUtils.contains(new String[]{"Abatible", "Tornos"}, door_.getDoorType());
        s = UpdateStringIfNeeded(s, "y", s == "" || ffill_door_type);
        s = UpdateStringIfNeeded(s, getString(R.string.puerta_n_tipo_puerta), ffill_door_type);


        boolean ffill_mec_type = ArrayUtils.contains(new String[]{"Manibela", "Barra", "Agarrador"}, door_.getMecType());
        s = UpdateStringIfNeeded(s, "y", s == "" || ffill_mec_type);
        s = UpdateStringIfNeeded(s, getString(R.string.puerta_n_tipo_mec), ffill_mec_type);

        boolean ffill_mec_high = Evaluator.IsInRange(door_.getKnobHeight(), minOpeningMc, maxOpeningMc);
        s = UpdateStringIfNeeded(s, "y", s == "" || ffill_mec_high);
        s = UpdateStringIfNeeded(s, getString(R.string.puerta_n_altura_mec) + minOpeningMc + " y " + maxOpeningMc, ffill_mec_high);


        door_.setAccessible(ffill_Height && ffill_Height && ffill_Width && ffill_mec_type && ffill_door_type && ffill_mec_high);

        UpdateMessage(door_.getAccessible(), s);
        door_.setMessage(message);

        Intent i = new Intent(this, AxesibilityActivity.class);
        i.putExtra(TypesManager.OBS_TYPE, TypesManager.obsType.DOOR.getValue());
        i.putExtra(TypesManager.DOOR_OBS, door_);

        startActivity(i);
        finish();
    }

    void doorDialog() {
        int[] spinnerImages = new int[]{R.drawable.puerta_giratoria, R.drawable.puerta_corredera
                , R.drawable.puerta_abatible, R.drawable.puerta_torno};


        int[] spinnerImages2 = new int[]{R.drawable.mecanismo_manibela, R.drawable.mecanismo_pomo
                , R.drawable.mecanismo_barra, R.drawable.mecanismo_agarrador};


        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MeasureDoor.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_door, null);
        mBuilder.setTitle("Selecciona puerta y pomo");
        Spinner mSpinnerDoor = (Spinner) mView.findViewById(R.id.spinner_puerta);
        Spinner mSpinnerMecha = (Spinner) mView.findViewById(R.id.spinner_mecanismo);

        ImageTitleAdapter mCustomAdapter = new ImageTitleAdapter(MeasureDoor.this, spinnerImages, doorType);
        mSpinnerDoor.setAdapter(mCustomAdapter);

        ImageTitleAdapter mCustomAdapter2 = new ImageTitleAdapter(MeasureDoor.this, spinnerImages2, mecTypes);
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
                door_.setDoorType(doorType[mSpinnerDoor.getSelectedItemPosition()]);
                door_.setMecType(mecTypes[mSpinnerMecha.getSelectedItemPosition()]);
            }
        });

        mBuilder.setView(mView);
        AlertDialog dialog = mBuilder.create();

        dialog.show();
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

    private String UpdateStringIfNeeded(String base, String to_add, boolean condition)
    {
        return condition ? base : base + " " + to_add;
    }

    private void UpdateMessage(boolean condition, String aux)
    {
        message = condition? getString(R.string.accessible) : getString(R.string.no_accesible);
        message += aux;
    }

}
