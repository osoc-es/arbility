package com.osoc.oncera;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import org.w3c.dom.Text;

public class LegalInfoActivity extends AppCompatActivity {

    ImageButton botonSalir;
    TextView desc;
    String descText;
    TypesManager.obsType type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_legal_info);

        botonSalir = (ImageButton)findViewById(R.id.BotonSalir);
        desc = (TextView)findViewById(R.id.descText);

        botonSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        int typeID = getIntent().getIntExtra("obsType",-1);

        type = TypesManager.obsType.valueOf(typeID);
        selectText();

    }

    private void selectText(){

        if(type == TypesManager.obsType.ASEOS) descText = getString(R.string.descAseos);
        else if (type == TypesManager.obsType.PUERTAS)  descText = getString(R.string.descPuertas);
        else if (type == TypesManager.obsType.ILUM)  descText = getString(R.string.descIlum);
        else if (type == TypesManager.obsType.ASCENSORES)  descText = getString(R.string.descAscensor);
        else if (type == TypesManager.obsType.MOSTRADORES)  descText = getString(R.string.descMostrador);
        else if (type == TypesManager.obsType.RAMPAS)  descText = getString(R.string.descRampa);
        else if (type == TypesManager.obsType.SALVAESCALERAS)  descText = getString(R.string.descSalvaescaleras);
        else if (type == TypesManager.obsType.ESTANCIAS)  descText = getString(R.string.descEstancias);
        else if (type == TypesManager.obsType.PASILLOS)  descText = getString(R.string.descPasillos);
        else if (type == TypesManager.obsType.EMERGENCIAS)  descText = getString(R.string.descEmergencias);

        desc.setText(descText);

    }

}
