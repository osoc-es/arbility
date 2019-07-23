package com.osoc.oncera;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.osoc.oncera.javabean.Elevator;
import com.osoc.oncera.javabean.Door;
import com.osoc.oncera.javabean.EmergencyNEvacuation;
import com.osoc.oncera.javabean.Illuminance;
import com.osoc.oncera.javabean.AttPoints;
import com.osoc.oncera.javabean.Ramps;
import com.osoc.oncera.javabean.StairLifter;

public class AxesibilityActivity extends AppCompatActivity {


    TextView accText;
    int typeID;
    RelativeLayout layout;
    TypesManager.obsType type;
    Button confirmar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accessibility_checker);

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

       if (type == TypesManager.obsType.DOOR)  CheckAccesibility(((Door)b.get(TypesManager.DOOR_OBS)).getAccessible(), ((Door)b.get(TypesManager.DOOR_OBS)).getMessage());
        else if (type == TypesManager.obsType.ILLUM)  CheckAccesibility(((Illuminance)b.get(TypesManager.ILLUM_OBS)).getAccessible(),((Illuminance) b.get(TypesManager.ILLUM_OBS)).getMessage());
        else if (type == TypesManager.obsType.ASCENSORES)  CheckAccesibility(((Elevator)b.get(TypesManager.ASCENSOR_OBS)).getAccessible(), ((Elevator)b.get(TypesManager.ASCENSOR_OBS)).getMessage());
       else if (type == TypesManager.obsType.MOSTRADORES)  CheckAccesibility(((AttPoints)b.get(TypesManager.MOSTRADOR_OBS)).getAccessible(), ((AttPoints)b.get(TypesManager.MOSTRADOR_OBS)).getMessage());
       else if (type == TypesManager.obsType.RAMPAS)  CheckAccesibility(((Ramps)b.get(TypesManager.RAMPA_OBS)).getAccessible(), ((Ramps)b.get(TypesManager.RAMPA_OBS)).getMessage());
       else if (type == TypesManager.obsType.SALVAESCALERAS) CheckAccesibility(((StairLifter)b.get(TypesManager.SALVAESC_OBS)).getAccessible(), ((StairLifter)b.get(TypesManager.SALVAESC_OBS)).getMessage());
       else if (type == TypesManager.obsType.EMERGENCIAS)  CheckAccesibility(((EmergencyNEvacuation)b.get(TypesManager.EMERGENC_OBS)).getAccessible(), ((EmergencyNEvacuation)b.get(TypesManager.EMERGENC_OBS)).getMessage());
    }



    private void CheckAccesibility(boolean b, String s)
    {
        if(b) OnAccesible(s);
        else OnNonAccesible(s);
    }

    private void OnAccesible(String s)
    {
        accText.setText(s);
        ChangeColorWhenValid();
    }

    private void OnNonAccesible(String s)
    {
        accText.setText(s);
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
