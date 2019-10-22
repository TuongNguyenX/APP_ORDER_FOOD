package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firebase.Common.Common;
import com.example.firebase.Interface.ItemClickListener;
import com.example.firebase.Model.Cars;
import com.example.firebase.Model.Category;
import com.example.firebase.Model.Category2;
import com.example.firebase.Model.Sale;
import com.example.firebase.ViewHolder.CarsViewHold;
import com.example.firebase.ViewHolder.MenuViewHolder2;
import com.example.firebase.ViewHolder.SaleDetailViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class SaleList extends AppCompatActivity {
    DatabaseReference databaseReference,databaseReference_Sale;
    FirebaseDatabase firebaseDatabase;
    FirebaseRecyclerAdapter<Sale, SaleDetailViewHolder>adapter;
    String saleId = "";
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    Sale currentSale;
    TextView txt_food_Sale;
    ImageView img_food_Sale;
    FloatingActionButton floatingActionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_list);

        floatingActionButton = findViewById(R.id.fab_addlist_sale);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SaleList.this,Cart.class);
                startActivity(intent);
            }
        });
        recyclerView = findViewById(R.id.recycler_SaleList);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("SaleDetail");
        databaseReference_Sale = firebaseDatabase.getReference("Sale");

        if (getIntent() != null) {
            saleId = getIntent().getStringExtra("SaleId");
        }
        if (!saleId.isEmpty() && saleId!= null){
            if (Common.isConnectedtoInternet(getBaseContext())) {
                loadListSaleDetail(saleId);
                loadListSale(saleId);

            }
            else {
                Toast.makeText(getBaseContext(), "Please check your connection", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void loadListSale(String saleId) {
        databaseReference_Sale.child(saleId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentSale = dataSnapshot.getValue(Sale.class);
                txt_food_Sale = findViewById(R.id.txt_food_Sale);
                img_food_Sale = findViewById(R.id.img_food_Sale);
                txt_food_Sale.setText(currentSale.getName());
                Picasso.with(getBaseContext())
                        .load(currentSale.getImage())
                        .into(img_food_Sale);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadListSaleDetail(String saleId) {
        adapter = new FirebaseRecyclerAdapter<Sale, SaleDetailViewHolder>
                (Sale.class,R.layout.item_sale_list, SaleDetailViewHolder.class, databaseReference.orderByChild("menuId").equalTo(saleId)) {
            @Override
            protected void populateViewHolder(SaleDetailViewHolder saleDetailViewHolder, Sale sale, final int i) {
                saleDetailViewHolder.name_sale_list.setText(sale.getName());
                saleDetailViewHolder.price_sale_list.setText(String.format("$ %s",sale.getPrice()));
                Picasso.with(getBaseContext())
                        .load(sale.getImage())
                        .into(saleDetailViewHolder.image_sale_list);


                saleDetailViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent intent = new Intent(SaleList.this,SaleDetail.class);
                        intent.putExtra("SaleDetailId",adapter.getRef(i).getKey());
                        startActivity(intent);
                    }
                });

            }

        };
        recyclerView.setAdapter(adapter);

    }

}
