package com.osoc.oncera;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.osoc.oncera.javabean.Ascensores;
import com.osoc.oncera.javabean.Aseos;
import com.osoc.oncera.javabean.Iluminacion;
import com.osoc.oncera.javabean.Puerta;
import com.osoc.oncera.javabean.SalvaEscaleras;

public class AxesibilityActivity extends AppCompatActivity {


    TextView accText;
    int typeID;
    RelativeLayout layout;
    TypesManager.obsType type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_axesibility);

        layout = (RelativeLayout) findViewById(R.id.layout);
        accText = (TextView)findViewById(R.id.accText);

        Bundle bundle = getIntent().getExtras();

        typeID = bundle.getInt(TypesManager.OBS_TYPE);
        type = TypesManager.obsType.valueOf(typeID);

        parseType(bundle);



    }

    private void parseType(Bundle b){
       if(type == TypesManager.obsType.ASEOS) parseAseos();
       else if (type == TypesManager.obsType.PUERTAS)  parsePuertas((Puerta)b.get(TypesManager.PUERTAS_OBS));
       else if (type == TypesManager.obsType.ILUM)  parseIlum((Iluminacion)b.get(TypesManager.ILUM_OBS));
       else if (type == TypesManager.obsType.ASCENSORES)  parseAscensor((Ascensores)b.get(TypesManager.ASCENSOR_OBS));
        //else if (type == TypesManager.obsType.MOSTRADORES)  descText = getString(R.string.descMostrador);
       // else if (type == TypesManager.obsType.RAMPAS)  descText = getString(R.string.descRampa);
        else if (type == TypesManager.obsType.SALVAESCALERAS) parseSalvaescaleras((SalvaEscaleras)b.get(TypesManager.SALVAESC_OBS));
       // else if (type == TypesManager.obsType.ESTANCIAS)  descText = getString(R.string.descEstancias);
       // else if (type == TypesManager.obsType.PASILLOS)  descText = getString(R.string.descPasillos);
       // else if (type == TypesManager.obsType.EMERGENCIAS)  descText = getString(R.string.descEmergencias);
    }

    private void parseAseos( ){
        //TODO:maybe hehe
    }

    private void parseIlum( Iluminacion ilum){
        Boolean b = ilum.getAccesible();


        if(b) {
            accText.setText("ES ACCESIBLE");
            ChangeColorWhenValid();
        }
        else {
            accText.setText("NO ES ACCESIBLE");
            ChangeColorWhenNotValid();
        }
    }

    private void parseAscensor( Ascensores a){
        Boolean b = a.getAccesible();
        if(b){
            accText.setText("ES ACCESIBLE");
            ChangeColorWhenValid();
        }
        else {
            accText.setText("NO ES ACCESIBLE");
            ChangeColorWhenNotValid();
        }

    }

    private void ChangeColorWhenNotValid()
    {
        layout.setBackgroundTintList(getResources().getColorStateList(R.color.red));
        layout.setBackgroundTintMode(PorterDuff.Mode.MULTIPLY);
        accText.setTextColor(getResources().getColor(R.color.white));
    }

    private void ChangeColorWhenValid()
    {
        layout.setBackgroundTintList(getResources().getColorStateList(R.color.greenMain));
        layout.setBackgroundTintMode(PorterDuff.Mode.OVERLAY);
        accText.setTextColor(getResources().getColor(R.color.black));
    }

    private void parseSalvaescaleras( SalvaEscaleras s){
        if(s.getAccesible()) accText.setText("ES VALIDO");
        else accText.setText("NO ES VALIDO");
    }
    private void parsePuertas( Puerta p){
        if(p.getAccesible()) accText.setText("ES VALIDO");
        else accText.setText("NO ES VALIDO");
    }
}
