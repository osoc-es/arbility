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

/**
 * Activity to check the object accessibility passed in the bundle and display why it is not accessible based on european
 * standards or show if it fulfills those standards
 */
public class AccessibilityChecker extends AppCompatActivity {


    TextView accText;
    RelativeLayout layout;
    Button confirmButton;

    TypesManager.obsType type;
    int typeID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accessibility_checker);

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

        if (type == TypesManager.obsType.DOOR)
            CheckAccesibility(((Door) b.get(TypesManager.DOOR_OBS)).getAccessible(), ((Door) b.get(TypesManager.DOOR_OBS)).getMessage());
        else if (type == TypesManager.obsType.ILLUM)
            CheckAccesibility(((Illuminance) b.get(TypesManager.ILLUM_OBS)).getAccessible(), ((Illuminance) b.get(TypesManager.ILLUM_OBS)).getMessage());
        else if (type == TypesManager.obsType.ELEVATOR)
            CheckAccesibility(((Elevator) b.get(TypesManager.ELEVATOR_OBS)).getAccessible(), ((Elevator) b.get(TypesManager.ELEVATOR_OBS)).getMessage());
        else if (type == TypesManager.obsType.ATTPOINT)
            CheckAccesibility(((AttPoints) b.get(TypesManager.ATTPOINT_OBS)).getAccessible(), ((AttPoints) b.get(TypesManager.ATTPOINT_OBS)).getMessage());
        else if (type == TypesManager.obsType.RAMPS)
            CheckAccesibility(((Ramps) b.get(TypesManager.RAMP_OBS)).getAccessible(), ((Ramps) b.get(TypesManager.RAMP_OBS)).getMessage());
        else if (type == TypesManager.obsType.STAIRLIFTER)
            CheckAccesibility(((StairLifter) b.get(TypesManager.STAIRLIFTER_OBS)).getAccessible(), ((StairLifter) b.get(TypesManager.STAIRLIFTER_OBS)).getMessage());
        else if (type == TypesManager.obsType.EMERGENCY)
            CheckAccesibility(((EmergencyNEvacuation) b.get(TypesManager.EMERGENCY_OBS)).getAccessible(), ((EmergencyNEvacuation) b.get(TypesManager.EMERGENCY_OBS)).getMessage());
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
