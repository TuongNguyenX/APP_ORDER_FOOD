package com.example.firebase;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.firebase.Common.Common;
import com.example.firebase.Database.Database;
import com.example.firebase.Model.Order;
import com.example.firebase.Model.Request;
import com.example.firebase.ViewHolder.CartAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Cart extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference requests;

    TextView  txtTotalPrice;
    Button btnPlace;
    List<Order> carts = new ArrayList<>();
    CartAdapter adapter;

    ElegantNumberButton elegantNumberButton;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));

    }

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);


        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");


        recyclerView = findViewById(R.id.listCart);

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        txtTotalPrice = findViewById(R.id.total);
        btnPlace = findViewById(R.id.btnPlaceOrder);
        btnPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               if (carts.size() >0)
                   showAlertDialog();
                else
                   Toast.makeText(Cart.this, "Your Cart Empty !!!", Toast.LENGTH_SHORT).show();
            }


        });




        loadListCart();
    }
    private void showAlertDialog() {

        AlertDialog.Builder alerdialog = new AlertDialog.Builder(Cart.this);
        alerdialog.setTitle("One more step!");
        alerdialog.setMessage("Enter your address: ");



        ///////////////

//        LayoutInflater  inflater  = this.getLayoutInflater();
//        View order_address_comments = inflater.inflate(R.layout.order_address_comment,null);
//        EditText edtAddress = order_address_comments.findViewById(R.id.cart_Order_Address);
//        EditText edtComments = order_address_comments.findViewById(R.id.cart_Order_Comments);
//
        //////////////
        final EditText edtAddress = new EditText(Cart.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );

        edtAddress.setLayoutParams(lp);
        alerdialog.setView(edtAddress);///add edit Text to alert dialog
        alerdialog.setIcon(R.drawable.ic_cart_black);
        alerdialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String address = edtAddress.getText().toString();
                if (TextUtils.isEmpty(address)) {
                    Toast.makeText(getApplicationContext(), "Please Enter Your Address!", Toast.LENGTH_SHORT).show();
                    return;
                }
                Request request = new Request(
                        Common.currentUser.getPhone(),
                        Common.currentUser.getName(),
                        Common.currentUser.getEmail(),
                        edtAddress.getText().toString(),
                        txtTotalPrice.getText().toString(),
                        carts




                );
                requests.child(String.valueOf(System.currentTimeMillis()))
                        .setValue(request);

                new Database(getBaseContext()).cleanCart();
                Intent i = new Intent(Cart.this,Home.class);
                startActivity(i);
                Toast.makeText(Cart.this, "Best wish ,See You Later", Toast.LENGTH_SHORT).show();
                finish();


            }
        });
        alerdialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });

        alerdialog.show();


    }

    private void loadListCart() {

        carts = new Database(this).getCarts();
        adapter= new CartAdapter(carts,this);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);


        int total = 0;
        for (Order order:carts)
            total+=(Integer.parseInt(order.getPrice()))*(Integer.parseInt(order.getQuantity()));
        Locale locale = new Locale("vi","VN");
        NumberFormat fmt =NumberFormat.getCurrencyInstance(locale);
        txtTotalPrice.setText(fmt.format(total));


    }
//////////////menu///////////////




    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle().equals(Common.DELETE))
            deleteCart(item.getOrder());
        return true;

    }



    private void deleteCart(int order) {
        //// Remove item ai list<Order> by position
        carts.remove(order);
        ////After that, delete all old   date from  SQlite
        new Database(this).cleanCart();
        /////After final,update new data from List<Order> to SQLite
        for (Order item:carts)
            new Database(this).addToCart(item);
        loadListCart();

    }
}
