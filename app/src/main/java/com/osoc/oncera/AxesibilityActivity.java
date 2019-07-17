package com.osoc.oncera;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.osoc.oncera.javabean.Ascensores;
import com.osoc.oncera.javabean.Aseos;
import com.osoc.oncera.javabean.EvacuacionEmergencia;
import com.osoc.oncera.javabean.Iluminacion;
import com.osoc.oncera.javabean.Puerta;
import com.osoc.oncera.javabean.PuntosAtencion;
import com.osoc.oncera.javabean.Rampas;
import com.osoc.oncera.javabean.SalvaEscaleras;

public class AxesibilityActivity extends AppCompatActivity {


    TextView accText;
    int typeID;
    RelativeLayout layout;
    TypesManager.obsType type;
    Button confirmar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_axesibility);

        layout = (RelativeLayout) findViewById(R.id.layout);
        accText = (TextView)findViewById(R.id.accText);
        confirmar = (Button) findViewById(R.id.BotonConfirmar);

        Bundle bundle = getIntent().getExtras();

        typeID = bundle.getInt(TypesManager.OBS_TYPE);
        type = TypesManager.obsType.valueOf(typeID);

        confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        parseType(bundle);

    }

    private void parseType(Bundle b){

        if (type == TypesManager.obsType.PUERTAS)  CheckAccesibility(((Puerta)b.get(TypesManager.PUERTAS_OBS)).getAccesible());
       else if (type == TypesManager.obsType.ILUM)  CheckAccesibility(((Iluminacion)b.get(TypesManager.ILUM_OBS)).getAccesible());
       else if (type == TypesManager.obsType.ASCENSORES)  CheckAccesibility(((Ascensores)b.get(TypesManager.ASCENSOR_OBS)).getAccesible());
       else if (type == TypesManager.obsType.MOSTRADORES)  CheckAccesibility(((PuntosAtencion)b.get(TypesManager.MOSTRADOR_OBS)).getAccesible());
       else if (type == TypesManager.obsType.RAMPAS)  CheckAccesibility(((Rampas)b.get(TypesManager.RAMPA_OBS)).getAccesible());
       else if (type == TypesManager.obsType.SALVAESCALERAS) CheckAccesibility(((SalvaEscaleras)b.get(TypesManager.SALVAESC_OBS)).getAccesible());
       else if (type == TypesManager.obsType.EMERGENCIAS)  CheckAccesibility(((EvacuacionEmergencia)b.get(TypesManager.EMERGENC_OBS)).getAccesible());
    }


    private void CheckAccesibility(boolean b)
    {
        if(b) OnAccesible();
        else OnNonAccesible();
    }

    private void OnAccesible()
    {
        accText.setText("ES ACCESIBLE");
        ChangeColorWhenValid();
    }

    private void OnNonAccesible()
    {
        accText.setText("NO ES ACCESIBLE");
        ChangeColorWhenNotValid();
    }


    private void ChangeColorWhenNotValid()
    {
        layout.setBackgroundTintList(getResources().getColorStateList(R.color.red));
        layout.setBackgroundTintMode(PorterDuff.Mode.MULTIPLY);
        accText.setTextColor(getResources().getColor(R.color.white));
        confirmar.setBackgroundTintList(getResources().getColorStateList(R.color.white));
        confirmar.setTextColor(getResources().getColor(R.color.red));

    }

    private void ChangeColorWhenValid()
    {
        layout.setBackgroundTintList(getResources().getColorStateList(R.color.greenMain));
        layout.setBackgroundTintMode(PorterDuff.Mode.OVERLAY);
        accText.setTextColor(getResources().getColor(R.color.black));
        confirmar.setBackgroundTintList(getResources().getColorStateList(R.color.red));
        confirmar.setTextColor(getResources().getColor(R.color.white));
    }




}
