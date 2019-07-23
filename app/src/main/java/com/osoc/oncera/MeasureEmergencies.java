package com.osoc.oncera;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.osoc.oncera.javabean.EmergencyNEvacuation;

public class MeasureEmergencies extends AppCompatActivity {


    private EmergencyNEvacuation emergencia = new EmergencyNEvacuation(null, null, null, null, null, null);
    private boolean db_alumbrado, db_simulacro;
    private String message;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measure_emergencies);

        UpdateDatabaseValues();

        dialog();
    }

    private void dialog()
    {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MeasureEmergencies.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_emergencies, null);
        mBuilder.setTitle("Rellena el cuestionario");
        CheckBox chk_simulacro = (CheckBox) mView.findViewById(R.id.chk1);
        CheckBox chk_alumbrado = (CheckBox) mView.findViewById(R.id.chk2);



        mBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i){
                dialogInterface.dismiss();
            }
        });

        mBuilder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {

                String s ="";

                emergencia.setEmergencyLighting(chk_alumbrado.isChecked());
                emergencia.setSimulation(chk_simulacro.isChecked());

                boolean cumple_alumbrado = Evaluator.IsEqualsTo(emergencia.getEmergencyLighting(), db_alumbrado);
                s = UpdateStringIfNeeded(s, getString(R.string.emergencia_n_alumbrado), cumple_alumbrado);

                boolean cumple_simulacro = Evaluator.IsEqualsTo(emergencia.getSimulation(), db_simulacro);
                s = UpdateStringIfNeeded(s, "y", (s == "" || cumple_simulacro));
                s = UpdateStringIfNeeded(s, getString(R.string.emergencia_n_simulacro), cumple_simulacro);

                emergencia.setAccessible(cumple_alumbrado && cumple_simulacro);

                UpdateMessage(emergencia.getAccessible(), s);
                emergencia.setMessage(message);

                Intent i = new Intent(getApplicationContext(),AxesibilityActivity.class);
                i.putExtra(TypesManager.OBS_TYPE,TypesManager.obsType.EMERGENCIAS.getValue());
                i.putExtra(TypesManager.EMERGENC_OBS, emergencia);

                startActivity(i);
                finish();

            }
        });

        mBuilder.setView(mView);
        AlertDialog dialog = mBuilder.create();

        dialog.show();
    }

    private void UpdateDatabaseValues()
    {
        final DatabaseReference dbr_a = FirebaseDatabase.getInstance().getReference("Estandares/Emergencias/AlumbradoEmerg");

        dbr_a.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                db_alumbrado = dataSnapshot.getValue(boolean.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final DatabaseReference dbr_s = FirebaseDatabase.getInstance().getReference("Estandares/Emergencias/Simulacros");

        dbr_s.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                db_simulacro = dataSnapshot.getValue(boolean.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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
