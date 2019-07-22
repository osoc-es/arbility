package com.osoc.oncera;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.osoc.oncera.javabean.Ascensores;
import com.osoc.oncera.javabean.EvacuacionEmergencia;
import com.osoc.oncera.javabean.Iluminacion;
import com.osoc.oncera.javabean.Puerta;
import com.osoc.oncera.javabean.PuntosAtencion;
import com.osoc.oncera.javabean.Rampas;
import com.osoc.oncera.javabean.SalvaEscaleras;

/**
 * Activity to check the object accessibility passed in the bundle and display why it is not accessible based on european
 * standards or show if it fulfills those standards
 */
public class AccessibilityChecker extends AppCompatActivity {


    TextView accText;
    int typeID;
    RelativeLayout layout;
    TypesManager.obsType type;
    Button confirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_axesibility);

        layout = (RelativeLayout) findViewById(R.id.layout);
        accText = (TextView) findViewById(R.id.accText);
        confirmButton = (Button) findViewById(R.id.BotonConfirmar);

        Bundle bundle = getIntent().getExtras();

        typeID = bundle.getInt(TypesManager.OBS_TYPE);
        type = TypesManager.obsType.valueOf(typeID);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        parseType(bundle);

    }

    /**
     * Parse of the bundle object in order to check its accessibility parameter and show a message if it is valid or not
     * @param b PutExtra bundle for getting the accessibility boolean of each obstacle
     */
    private void parseType(Bundle b) {

        if (type == TypesManager.obsType.PUERTAS)
            CheckAccesibility(((Puerta) b.get(TypesManager.PUERTAS_OBS)).getAccesible(), ((Puerta) b.get(TypesManager.PUERTAS_OBS)).getMessage());
        else if (type == TypesManager.obsType.ILUM)
            CheckAccesibility(((Iluminacion) b.get(TypesManager.ILUM_OBS)).getAccesible(), ((Iluminacion) b.get(TypesManager.ILUM_OBS)).getMessage());
        else if (type == TypesManager.obsType.ASCENSORES)
            CheckAccesibility(((Ascensores) b.get(TypesManager.ASCENSOR_OBS)).getAccesible(), ((Ascensores) b.get(TypesManager.ASCENSOR_OBS)).getMessage());
        else if (type == TypesManager.obsType.MOSTRADORES)
            CheckAccesibility(((PuntosAtencion) b.get(TypesManager.MOSTRADOR_OBS)).getAccesible(), ((PuntosAtencion) b.get(TypesManager.MOSTRADOR_OBS)).getMessage());
        else if (type == TypesManager.obsType.RAMPAS)
            CheckAccesibility(((Rampas) b.get(TypesManager.RAMPA_OBS)).getAccesible(), ((Rampas) b.get(TypesManager.RAMPA_OBS)).getMessage());
        else if (type == TypesManager.obsType.SALVAESCALERAS)
            CheckAccesibility(((SalvaEscaleras) b.get(TypesManager.SALVAESC_OBS)).getAccesible(), ((SalvaEscaleras) b.get(TypesManager.SALVAESC_OBS)).getMessage());
        else if (type == TypesManager.obsType.EMERGENCIAS)
            CheckAccesibility(((EvacuacionEmergencia) b.get(TypesManager.EMERGENC_OBS)).getAccesible(), ((EvacuacionEmergencia) b.get(TypesManager.EMERGENC_OBS)).getMessage());
    }


    /**
     * Check object's accessibility
     * @param b object's accessibility boolean
     * @param s Message from each obstacle about legal accessibility standards
     */
    private void CheckAccesibility(boolean b, String s) {
        if (b) OnAccesible(s);
        else OnNonAccesible(s);
    }

    /**
     * Called when an obstacle is accessible to display it on the text and change the background color
     * @param s text where the message is displayed
     */
    private void OnAccesible(String s) {
        accText.setText(s);
        ChangeColorWhenValid();
    }

    /**
     * Called when an obstacle is not accessible to display why not on the text and change the background color
     * @param s text where the message is displayed
     */
    private void OnNonAccesible(String s) {
        accText.setText(s);
        ChangeColorWhenNotValid();
    }

    /**
     * Changes the background color when it is not accessible
     */
    private void ChangeColorWhenNotValid() {
        layout.setBackgroundTintList(getResources().getColorStateList(R.color.red));
        layout.setBackgroundTintMode(PorterDuff.Mode.MULTIPLY);
        accText.setTextColor(getResources().getColor(R.color.white));
        confirmButton.setBackgroundTintList(getResources().getColorStateList(R.color.white));
        confirmButton.setTextColor(getResources().getColor(R.color.red));

    }

    /**
     * Changes the background color when it is accessible
     */
    private void ChangeColorWhenValid() {
        layout.setBackgroundTintList(getResources().getColorStateList(R.color.greenMain));
        layout.setBackgroundTintMode(PorterDuff.Mode.OVERLAY);
        accText.setTextColor(getResources().getColor(R.color.black));
        confirmButton.setBackgroundTintList(getResources().getColorStateList(R.color.red));
        confirmButton.setTextColor(getResources().getColor(R.color.white));
    }


}
