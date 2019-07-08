package com.example.cameraexample;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private Button btnCapture;
    private Spinner spin;
    private ImageView imgCapture;
    private ImageView imgIcone;
    private static final int Image_Capture_Code = 1;
    String[] access = { "Rampa", "Bordillo", "Ascensor", "Puerta", "Other"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnCapture =(Button)findViewById(R.id.btnTakePicture);
        imgCapture = (ImageView) findViewById(R.id.img_camera);
        imgIcone = (ImageView) findViewById( R.id.img_icon );
        spin = (Spinner) findViewById(R.id.spin_access);
        spin.setOnItemSelectedListener(this);

        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,access);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);

        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cInt = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cInt,Image_Capture_Code);
            }
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Image_Capture_Code) {
            if (resultCode == RESULT_OK) {
                Bitmap bp = (Bitmap) data.getExtras().get("data");
                imgCapture.setImageBitmap(bp);
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            }
        }
    }

    //Performing action onItemSelected and onNothing selected
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        //Toast.makeText(getApplicationContext(),access[position] , Toast.LENGTH_LONG).show();
        if (access[position] == "Rampa") {
            imgIcone.setImageResource(R.drawable.rampa);
        }
        else if (access[position] == "Bordillo") {
            imgIcone.setImageResource(R.drawable.bordillo);
        }
        else if (access[position] == "Ascensor") {
            imgIcone.setImageResource(R.drawable.ascensor);
        }
        else if (access[position] == "Puerta") {
            imgIcone.setImageResource(R.drawable.puerta);
        }
        else if (access[position] == "Other") {
            imgIcone.setImageResource(R.drawable.otro);
        }

    }
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
}