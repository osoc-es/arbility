package com.osoc.oncera;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class MedirMostradorActivity extends AppCompatActivity {

    String[] s = { "India ", "Arica", "India ", "Arica", "India ", "Arica",
            "India ", "Arica", "India ", "Arica" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medir_mostrador);

        final ArrayAdapter<String> adp = new ArrayAdapter<String>(MedirMostradorActivity.this,
                android.R.layout.simple_spinner_item, s);

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MedirMostradorActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_door, null);
        mBuilder.setTitle("Selecciona mostrador");
        Spinner mSpinner = (Spinner) mView.findViewById(R.id.spinner);
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adp);

        mBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i){
                Toast.makeText(MedirMostradorActivity.this,
                        mSpinner.getSelectedItem().toString(),
                        Toast.LENGTH_SHORT).show();

                dialogInterface.dismiss();
            }
        });

        mBuilder.setView(mView);
        AlertDialog dialog = mBuilder.create();
        dialog.show();
    }
}
