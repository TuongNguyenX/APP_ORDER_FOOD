package com.example.firebase;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

public class EditUIProfile extends AppCompatActivity {
    ImageView img_avatar,img_anhbia;
    TextView tv_nameeditprofile,
             tv_statuseditprofile,
             tv_studenteditprofile,
             tv_liveeditprofile,
             tv_fromeditprofile,
             tv_joninededitprofile;
    String editprofileId= "";
    String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_uiprofile);
        toolBar();
        anhxa();
        name = getIntent().getExtras().getString("MyTeamId");
        tv_nameeditprofile.setText(name);



    }
    private void anhxa() {
    img_avatar = findViewById(R.id.img_avatar_editprofile);
    img_anhbia = findViewById(R.id.img_anhbia_editprofile);
    tv_nameeditprofile = findViewById(R.id.tv_name_editprofile);
    tv_statuseditprofile = findViewById(R.id.tv_status_editprofile);
    tv_studenteditprofile = findViewById(R.id.tv_student_editprofile);
    tv_liveeditprofile = findViewById(R.id.tv_live_editprofile);
    tv_fromeditprofile = findViewById(R.id.tv_from_editprofile);
    tv_joninededitprofile = findViewById(R.id.tv_jonined_editprofile);
    }
    private void toolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Edit Profile");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }



}
