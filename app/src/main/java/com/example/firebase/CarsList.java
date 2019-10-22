package com.example.firebase;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firebase.Common.Common;
import com.example.firebase.Interface.ItemClickListener;
import com.example.firebase.Model.Cars;
import com.example.firebase.Model.Category;
import com.example.firebase.Model.Category2;
import com.example.firebase.ViewHolder.CarsViewHold;
import com.example.firebase.ViewHolder.MenuViewHold;
import com.example.firebase.ViewHolder.MenuViewHolder2;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

import info.hoang8f.widget.FButton;

public class CarsList extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseDatabase database;
    DatabaseReference databaseReference_Food,databaseReference,databaseReference_Sale;
    String categoryId = "";

    String saleId = "";

    private final  int PICK_IMAGE_REQUEST = 71;
    Uri saveUri;
    Category category;
    StorageReference storageReference;
    FirebaseStorage storage;


    SwipeRefreshLayout swipeRefreshLayout;

    FirebaseRecyclerAdapter<Cars, CarsViewHold> adapter;
    FirebaseRecyclerAdapter<Category2, MenuViewHolder2>adapter_Sale;
    List<String> suggeestList = new ArrayList<>();


    /// Facebook Share
    CallbackManager callbackManager;
    ShareDialog shareDialog;

    //// create from picaso


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

    ///Add new Food
    EditText edtName,edtDescription,edtPrice,edtDiscount;
    FButton btnSelect,btnUpload;
    TextView tv;
    ImageView img;

    FloatingActionButton floatingActionButton;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cars_list);

        ///Firebase
         database = FirebaseDatabase.getInstance();
         databaseReference_Food = database.getReference("Food");



         database = FirebaseDatabase.getInstance();
         databaseReference = database.getReference("Category");
         databaseReference_Sale = database.getReference("Sale");
         storage = FirebaseStorage.getInstance();
         storageReference = storage.getReference();

         tv = findViewById(R.id.tv_carlist);
         img = findViewById(R.id.img_carlist);
         floatingActionButton = findViewById(R.id.fab_addlist);
         floatingActionButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 showDiaLogList();
             }
         });



        ///init Facebook
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);






        swipeRefreshLayout = findViewById(R.id.swipe_layout);
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
                        loadMenuFood(categoryId);
                        loadListSale(categoryId);
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
                        loadMenuFood(categoryId);
                    }
                    else {
                        Toast.makeText(getBaseContext(), "Please check your connection", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

         recyclerView = findViewById(R.id.recycler_cars);
         recyclerView.setHasFixedSize(true);
         layoutManager = new LinearLayoutManager(this);
         recyclerView.setLayoutManager(layoutManager);


         ////Init Facebook
            callbackManager = CallbackManager.Factory.create();
            shareDialog = new ShareDialog(this);


//        loadSuggest();

    }

    private void loadListSale(String saleId)
    {


    }

    private void showDiaLogList() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(CarsList.this);
        alertDialog.setTitle("Add new Category");
        alertDialog.setMessage("Please fill fun imformation");
        LayoutInflater inflater = this.getLayoutInflater();
        View add_menu_layout = inflater.inflate(R.layout.add_dialog_foodlist,null);
        edtName = add_menu_layout.findViewById(R.id.edtName_list);
        edtDescription = add_menu_layout.findViewById(R.id.edtDescription_list);
        edtPrice = add_menu_layout.findViewById(R.id.edtPrice_list);
        edtDiscount = add_menu_layout.findViewById(R.id.edtDiscount_list);
        alertDialog.setView(add_menu_layout);
        alertDialog.setIcon(R.drawable.ic_cart_black);
        ///set Button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                ///here just create new category


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



    private void loadMenuFood(String categoryId) {
       databaseReference.child(categoryId).addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               category = dataSnapshot.getValue(Category.class);

               tv.setText(category.getName());
               Picasso.with(getBaseContext()).load(category.getImage())
                       .into(img);

           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {
               Toast.makeText(CarsList.this, "Fail", Toast.LENGTH_SHORT).show();

           }
       });

    }




    private void loadListFood(String categoryId) {

        adapter = new FirebaseRecyclerAdapter<Cars, CarsViewHold>
                (Cars.class,R.layout.cars_item, CarsViewHold.class, databaseReference_Food.orderByChild("menuId").equalTo(categoryId)) {
            @Override
            protected void populateViewHolder(CarsViewHold viewHolder, final Cars model, int position) {
                viewHolder.cars_name.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage())
                        .into(viewHolder.cars_image);
                viewHolder.cars_price.setText(String.format("$ %s",model.getPrice()));
                final Cars local = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent i = new Intent(CarsList.this,CarDetail.class);
                        i.putExtra("CarId",adapter.getRef(position).getKey());
                        startActivity(i);

                    }
                });
                viewHolder.share_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Picasso.with(getApplicationContext())
                                .load(model.getImage())
                                .into(target);
                      }
                });


            }
        };
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setRefreshing(false);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode ==PICK_IMAGE_REQUEST && resultCode==RESULT_OK
                && data != null && data.getData() != null)
        {
            saveUri = data.getData();
            btnSelect.setText("Image Selected!");
        }
    }
}
