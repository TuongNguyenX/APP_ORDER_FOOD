package com.example.firebase.Call;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.firebase.R;

import info.hoang8f.widget.FButton;

public class Call extends AppCompatActivity {
    EditText et_phonecall;
    FButton button;
    ImageView img_phone;
    private static final int REQUEST_CALL = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);
        et_phonecall = findViewById(R.id.editText3);
        button = findViewById(R.id.button_call);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               controllerCall();
            }
        });
    }

    private void controllerCall() {
        Intent i = new Intent(Intent.ACTION_CALL);
        String number  = et_phonecall.getText().toString();

        if (number.trim().isEmpty())
        {
            Toast.makeText(this, "Please enter your number", Toast.LENGTH_SHORT).show();
            requestionPermission();
        }
        else
        {
            i.setData(Uri.parse("tel:"+number));
        }
        if (ActivityCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, "Please Grant Permission", Toast.LENGTH_SHORT).show();
        }
        else {
            startActivity(i);
        }
    }


    
    private  void requestionPermission(){
        ActivityCompat.requestPermissions(Call.this,new String[]{Manifest.permission.CALL_PHONE},1);
    }

}
