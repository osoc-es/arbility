package com.osoc.oncera;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.osoc.oncera.javabean.Ascensores;
import com.osoc.oncera.javabean.Iluminacion;

public class AxesibilityActivity extends AppCompatActivity {


    TextView accText;
    int typeID;
    TypesManager.obsType type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_axesibility);

        accText = (TextView)findViewById(R.id.accText);

        Bundle bundle = getIntent().getExtras();

        typeID = bundle.getInt(TypesManager.OBS_TYPE);
        type = TypesManager.obsType.valueOf(typeID);

        parseType(bundle);



    }

    private void parseType(Bundle b){
       // if(type == TypesManager.obsType.ASEOS) parseIlum
       // else if (type == TypesManager.obsType.PUERTAS)  descText = getString(R.string.descPuertas);
         if (type == TypesManager.obsType.ILUM)  parseIlum((Iluminacion)b.get(TypesManager.ILUM_OBS));
       else if (type == TypesManager.obsType.ASCENSORES)  parseAscensor((Ascensores)b.get(TypesManager.ASCENSOR_OBS));
        //else if (type == TypesManager.obsType.MOSTRADORES)  descText = getString(R.string.descMostrador);
       // else if (type == TypesManager.obsType.RAMPAS)  descText = getString(R.string.descRampa);
        //else if (type == TypesManager.obsType.SALVAESCALERAS)  descText = getString(R.string.descSalvaescaleras);
       // else if (type == TypesManager.obsType.ESTANCIAS)  descText = getString(R.string.descEstancias);
       // else if (type == TypesManager.obsType.PASILLOS)  descText = getString(R.string.descPasillos);
       // else if (type == TypesManager.obsType.EMERGENCIAS)  descText = getString(R.string.descEmergencias);
    }

    private void parseIlum( Iluminacion ilum){
        Boolean b = ilum.getAccesible();
        if(b) accText.setText("ES VALIDO");
        else accText.setText("NO ES VALIDO");
    }

    private void parseAscensor( Ascensores a){

    }
}
