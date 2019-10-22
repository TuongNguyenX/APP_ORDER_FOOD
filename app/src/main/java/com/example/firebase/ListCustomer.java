package com.example.firebase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.firebase.Common.Common;
import com.example.firebase.Model.CategoryOther;
import com.example.firebase.Model.User;
import com.example.firebase.ViewHolder.MenuViewHoldOther;
import com.example.firebase.ViewHolder.UserViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ListCustomer extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView recyclerView;
    FirebaseRecyclerAdapter<User, UserViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_customer);



        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("User");

        recyclerView = findViewById(R.id.recycler_listcustomer);
//        recyler_Other.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        loadCustomer();



    }

    private void loadCustomer() {
        adapter = new FirebaseRecyclerAdapter<User, UserViewHolder>
                (User.class,R.layout.item_listcutomer, UserViewHolder.class,databaseReference) {
            @Override
            protected void populateViewHolder(UserViewHolder userViewHolder, User user, int i) {
               userViewHolder.txtEmail_customer.setText(user.getEmail());

               userViewHolder.txtPhone_customer.setText(Common.currentUser.getPhone());
               userViewHolder.txtPaswword_customer.setText(user.getPassword());
               userViewHolder.txtName_customer.setText(user.getName());
            }
        };
        recyclerView.setAdapter(adapter);
    }
}
