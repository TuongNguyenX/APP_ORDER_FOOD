package com.example.firebase.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.firebase.Common.Common;
import com.example.firebase.Database.Database;
import com.example.firebase.Interface.ItemClickListener;
import com.example.firebase.Model.Category;
import com.example.firebase.Model.Food;
import com.example.firebase.Model.Order;
import com.example.firebase.R;
import com.example.firebase.ViewHolder.FoodViewHolder;
import com.facebook.CallbackManager;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

public class FoodList extends AppCompatActivity {

    DatabaseReference databaseReference_Food,databaseReference_Category;
    FirebaseDatabase firebaseDatabase;

    String categoryId = "";
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    Category category;
    Database localDB;
    ImageView img_food_category;
    TextView txt_food_category;

    /////Database


    /// Facebook Share
    CallbackManager callbackManager;
    ShareDialog shareDialog;
    FloatingActionButton floatingActionButton;
    SwipeRefreshLayout swipeRefreshLayout;
    Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            ///Create photo from BitMap
            SharePhoto photo  = new SharePhoto.Builder()
                    .setBitmap(bitmap)
                    .build();
            if (ShareDialog.canShow(SharePhotoContent.class))
            {
                SharePhotoContent content = new  SharePhotoContent.Builder()
                        .addPhoto(photo)
                        .build();
                shareDialog.show(content);
            }
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };


    FirebaseRecyclerAdapter<Food, FoodViewHolder> adapter;

    FirebaseRecyclerAdapter<Food, FoodViewHolder> searchAdapter;
    List<String> suggestList = new ArrayList<>();
    MaterialSearchBar materialSearchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        img_food_category = findViewById(R.id.img_food_category);
        txt_food_category = findViewById(R.id.txt_food_category);
        swipeRefreshLayout = findViewById(R.id.swipe_layout_food);

        floatingActionButton = findViewById(R.id.fab_addlist);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FoodList.this,Cart.class);
                startActivity(intent);
            }
        });



        ////
        localDB = new Database(this);


        recyclerView = findViewById(R.id.recycler_Food);;
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference_Food = firebaseDatabase.getReference("FoodTuongAZ");
        databaseReference_Category = firebaseDatabase.getReference("CategoryTuongAZ");


       /////////////////
        swipeRefreshLayout.setColorSchemeResources(R.color.black,
                android.R.color.holo_red_dark,
                android.R.color.holo_orange_dark,
                android.R.color.background_dark);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (getIntent() != null)
                    categoryId = getIntent().getStringExtra("CategoryId");
                if (!categoryId.isEmpty() && categoryId!= null){
                    if (Common.isConnectedtoInternet(getBaseContext())) {
                        loadListFood(categoryId);
                        loadMenuFood_Category(categoryId);
                    }
                    else {
                        Toast.makeText(getBaseContext(), "Please check your connection", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                if (getIntent() != null)
                    categoryId = getIntent().getStringExtra("CategoryId");
                if (!categoryId.isEmpty() && categoryId!= null){
                    if (Common.isConnectedtoInternet(getBaseContext())) {
                        loadListFood(categoryId);
                        loadMenuFood_Category(categoryId);
                    }
                    else {
                        Toast.makeText(getBaseContext(), "Please check your connection", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        ////search

    }





    private void loadMenuFood_Category(String categoryId) {
        databaseReference_Category.child(categoryId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                category = dataSnapshot.getValue(Category.class);
                txt_food_category.setText(category.getName());
                Picasso.with(getBaseContext())
                        .load(category.getImage())
                        .into(img_food_category);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadListFood(String categoryId) {
        adapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>
                (Food.class,R.layout.item_food, FoodViewHolder.class, databaseReference_Food.orderByChild("menuId").equalTo(categoryId)) {
            @Override
            protected void populateViewHolder(final FoodViewHolder foodViewHolder, final Food food, final int i) {

                foodViewHolder.food_name.setText(food.getName());
                Picasso.with(getBaseContext())
                        .load(food.getImage())
                        .placeholder(R.drawable.imgerror)
                        .error(R.drawable.error)
                        .into(foodViewHolder.food_image);
                foodViewHolder.food_price.setText(String.format("$ %s",food.getPrice()));
                final Food local = food;

                if (localDB.isFavorites(adapter.getRef(i).getKey())){
                    foodViewHolder.img_favorite.setImageResource(R.drawable.ic_favorite_red);
                }
                //////Click change
                foodViewHolder.img_favorite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (!localDB.isFavorites(adapter.getRef(i).getKey())){

                            localDB.addToFavorites(adapter.getRef(i).getKey());
                            foodViewHolder.img_favorite.setImageResource(R.drawable.ic_favorite_red);
//                            Toast.makeText(FoodList.this, ""+local.getName()+"da them", Toast.LENGTH_SHORT).show();

                        }else {
                            localDB.removeFromFavorites(adapter.getRef(i).getKey());
                            foodViewHolder.img_favorite.setImageResource(R.drawable.ic_favorite);
//                            Toast.makeText(FoodList.this, ""+local.getName()+"xoa", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
                foodViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent i = new Intent(FoodList.this,FoodDetail.class);
                        i.putExtra("FoodId",adapter.getRef(position).getKey());
                        startActivity(i);
//                        Toast.makeText(FoodList.this, ""+local.getName(), Toast.LENGTH_SHORT).show();



                    }
                });
                foodViewHolder.food_share_facebook.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Picasso.with(getApplicationContext())
                                .load(food.getImage())
                                .into(target);
                    }
                });
                foodViewHolder.img_buy_cart_food.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new Database(getBaseContext()).addToCart(new Order(
                                adapter.getRef(i).getKey(),
                                food.getName(),
                                "1",
                                food.getPrice(),
                                food.getDiscount(),
                                food.getImage()

                        ));
                        Toast.makeText(FoodList.this, "Add to Cart", Toast.LENGTH_SHORT).show();

                    }
                });


            }
        };
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
        swipeRefreshLayout.setRefreshing(false);
    }
}
