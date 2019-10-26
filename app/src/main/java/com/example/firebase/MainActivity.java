package com.example.firebase;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.firebase.Common.Common;
import com.example.firebase.Model.User;
import com.facebook.FacebookSdk;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import io.paperdb.Paper;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {

    Button bt_dangnhap,bt_dangky;
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);


//        printKeyHash();

        bt_dangnhap = findViewById(R.id.bt_dangnhapne);


        bt_dangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i2 = new Intent(MainActivity.this,Login.class);
                startActivity(i2);
            }
        });

        Paper.init(this);
        String user = Paper.book().read(Common.USER_KEY);
        String pwd = Paper.book().read(Common.PWD_KEY);
        if (user != null && pwd != null  )
        {
            if (!user.isEmpty()&& !pwd.isEmpty())
                login(user,pwd);
        }
    }

    private void login(final String phone, final String pwd) {
    //copu code from register class
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");
        if (Common.isConnectedtoInternet(getBaseContext())) {
            final ProgressDialog mDialog  = new ProgressDialog(MainActivity.this);
            mDialog.setMessage("Please waiting...");
            mDialog.show();
            table_user.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //check if user  not exist  in database
                    if (dataSnapshot.child(phone).exists()) {
                        User user = dataSnapshot.child(phone).getValue(User.class);
                        user.setPhone(phone);
                        if (user.getPassword().equals(pwd)) {
                            Intent intent = new Intent(MainActivity.this, Home.class);
                            Common.currentUser = user;
                            startActivity(intent);
                            finish();
                            Toast.makeText(MainActivity.this, "Log in successfull !!!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Wrong Password ", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "User is not register", Toast.LENGTH_SHORT).show();
                    }


                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });





        }
        else {
            Toast.makeText(MainActivity.this, "Please Check Your Connection", Toast.LENGTH_SHORT).show();
        }

    }



}
