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


    private EmergencyNEvacuation emergency = new EmergencyNEvacuation(null, null, null, null, null, null);
    private boolean db_lighting, db_drill;
    private String message;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measure_emergencies);

        UpdateDatabaseValues();

        dialogEmergencies();
    }

    /**
     * Dialog to ask user questions about accessibility in the emergency aspect
     */
    private void dialogEmergencies()
    {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MeasureEmergencies.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_emergencies, null);
        mBuilder.setTitle("Rellena el cuestionario");
        CheckBox chk_drill = (CheckBox) mView.findViewById(R.id.chk1);
        CheckBox chk_lighting = (CheckBox) mView.findViewById(R.id.chk2);



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

                emergency.setAlumbradoEmergencia(chk_lighting.isChecked());
                emergency.setSimulacros(chk_drill.isChecked());

                boolean cumple_alumbrado = Evaluator.IsEqualsTo(emergency.getAlumbradoEmergencia(), db_lighting);
                s = UpdateStringIfNeeded(s, getString(R.string.emergencia_n_alumbrado), cumple_alumbrado);

                boolean cumple_simulacro = Evaluator.IsEqualsTo(emergency.getSimulacros(), db_drill);
                s = UpdateStringIfNeeded(s, "y", (s == "" || cumple_simulacro));
                s = UpdateStringIfNeeded(s, getString(R.string.emergencia_n_simulacro), cumple_simulacro);

                emergency.setAccesible(cumple_alumbrado && cumple_simulacro);

                UpdateMessage(emergency.getAccesible(), s);
                emergency.setMensaje(message);

                Intent i = new Intent(getApplicationContext(),AxesibilityActivity.class);
                i.putExtra(TypesManager.OBS_TYPE,TypesManager.obsType.EMERGENCIAS.getValue());
                i.putExtra(TypesManager.EMERGENC_OBS, emergency);

                startActivity(i);
                finish();

            }
        });

        mBuilder.setView(mView);
        AlertDialog dialog = mBuilder.create();

        dialog.show();
    }

    /**
     * Get values of accessibility standards from database
     */
    private void UpdateDatabaseValues()
    {
        final DatabaseReference dbr_a = FirebaseDatabase.getInstance().getReference("Estandares/Emergencias/AlumbradoEmerg");

        dbr_a.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                db_lighting = dataSnapshot.getValue(boolean.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final DatabaseReference dbr_s = FirebaseDatabase.getInstance().getReference("Estandares/Emergencias/Simulacros");

        dbr_s.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                db_drill = dataSnapshot.getValue(boolean.class);
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
