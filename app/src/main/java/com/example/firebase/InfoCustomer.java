package com.example.firebase;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.firebase.Common.Common;
import com.example.firebase.Model.Food;
import com.example.firebase.Model.User;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class InfoCustomer extends AppCompatActivity {

    TextView tv_name,tv_email,tv_phone,tv_password;
    FloatingActionButton floatingActionButton;
    EditText edtName,edtEmail,edtPhone,edtPassword;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    User user;
    String key ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_customer);
        user = Common.currentUser;
        tv_name = findViewById(R.id.profile_Name_Customer);
        tv_phone = findViewById(R.id.profile_Phone_Customer);
        tv_email = findViewById(R.id.profile_Email_Customer);
        tv_password = findViewById(R.id.profile_Password_Customer);
        floatingActionButton = findViewById(R.id.fab_Edit_Profile);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDiaLog();
            }
        });

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("User");

        tv_name.setText(this.user.getName());
        tv_phone.setText(this.user.getPhone());
        tv_email.setText(this.user.getEmail());
        tv_password.setText(this.user.getPassword());


    }

    private void showDiaLog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(InfoCustomer.this);

        LayoutInflater inflater = this.getLayoutInflater();
        View add_menu_layout = inflater.inflate(R.layout.dialog_update_info_customer,null);
        edtName = add_menu_layout.findViewById(R.id.edtNameInfoCustomer);
        edtEmail = add_menu_layout.findViewById(R.id.edtEmailInfoCustomer);
        edtPassword = add_menu_layout.findViewById(R.id.edtPasswordInfoCustomer);
        edtPhone= add_menu_layout.findViewById(R.id.edtPhoneInfoCustomer);


        ////set daufault value  for view

        edtName.setText(this.user.getName());
        edtPhone.setText(this.user.getPhone());
        edtPassword.setText(this.user.getPassword());
        edtEmail.setText(this.user.getEmail());

        alertDialog.setView(add_menu_layout);
        alertDialog.setIcon(R.drawable.ic_add);
        


        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                ///here just create new category

                user.setName(edtName.getText().toString());
                user.setEmail(edtEmail.getText().toString());
                user.setPhone(edtPhone.getText().toString());
                user.setPassword(edtPassword.getText().toString());


//                 databaseReference.child(key).setValue(user);


            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });

        alertDialog.show();
    }
}
