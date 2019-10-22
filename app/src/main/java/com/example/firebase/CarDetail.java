package com.example.firebase;

import android.content.Intent;
import androidx.annotation.NonNull;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.firebase.Common.Common;
import com.example.firebase.Database.Database;
import com.example.firebase.Model.Cars;
import com.example.firebase.Model.Order;
import com.example.firebase.Model.Rating;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class CarDetail extends AppCompatActivity implements RatingDialogListener {
    TextView tv_name,tv_price,tv_description;
    ImageView car_image,img_feedback;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnCart;
    ElegantNumberButton numberButton;
    String carId= "";
    FirebaseDatabase database;
    DatabaseReference cars,databaseReference_SaleDetail;
    Cars currentcars;
    Button bt_carts;
    Button btnmua;
    DatabaseReference ratingTb1;
    RatingBar ratingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_detail);

        btnmua = findViewById(R.id.muano);
        database = FirebaseDatabase.getInstance();
        cars = database.getReference("Food");
        databaseReference_SaleDetail = database.getReference("SaleDetail");
        ratingTb1 = database.getReference("Rating");

        ratingBar = findViewById(R.id.ratingBar);



        btnCart= findViewById(R.id.fab);
        btnmua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Database(getBaseContext()).addToCart(new Order(
                        carId,
                        currentcars.getName(),
                        numberButton.getNumber(),
                        currentcars.getPrice(),
                        currentcars.getDiscount()


                ));
                Toast.makeText(CarDetail.this, "ADD R", Toast.LENGTH_SHORT).show();
            }
        });
        numberButton = findViewById(R.id.number_button);

        tv_name = findViewById(R.id.car_detail_name);
        tv_price = findViewById(R.id.car_detail_price);
        tv_description = findViewById(R.id.car_description);
        car_image = findViewById(R.id.car_details_image);
        img_feedback = findViewById(R.id.image_feelback);
        img_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               showRatingDialog();
            }
        });



        ///get car id from intent

        if (getIntent()!= null)
            carId = getIntent().getStringExtra("CarId");

        if (!carId.isEmpty() && carId!= null ){
            if (Common.isConnectedtoInternet(getBaseContext())) {

                getDetailCar(carId);
                getRatingCar(carId);


            }
            else {
                Toast.makeText(this, "Please check your connection", Toast.LENGTH_SHORT).show();
            }
        }


    }




    private void getRatingCar(String carId) {
        Query carRating = ratingTb1.orderByChild("carsId").equalTo(carId);
        carRating.addValueEventListener(new ValueEventListener() {
            int count = 0,sum = 0;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot:dataSnapshot.getChildren())
                {

                    Rating item = postSnapshot.getValue(Rating.class);
                    sum += Integer.parseInt(item.getRateValue());
                    count++;
                }
                if (count != 0){
                    float average = sum/count;
                    ratingBar.setRating(average);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //// Feed back ////
    private void showRatingDialog() {
        new AppRatingDialog.Builder()
                .setPositiveButtonText("Submit")
                .setNegativeButtonText("Cancel")
                .setNoteDescriptions(Arrays.asList("Very Bad", "Not Good", "Quite Ok", "Very Good", "Excelent"))
                .setTitle("Rate this product")
                .setDefaultRating(1)
                .setDescription("Give your feeback and rating star !")
                .setTitleTextColor(R.color.colorPrimary)
                .setHint("Please write your comment here...")
                .setHintTextColor(R.color.white)
                .setDescriptionTextColor(R.color.colorPrimary)
                .setCommentTextColor(R.color.white)
                .setCommentBackgroundColor(R.color.colorPrimary)
                .setWindowAnimation(R.style.RatingDialogFadeAnim)
                .create(CarDetail.this)
                .show();
    }




    private void getDetailCar(String carId) {

        cars.child(carId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                currentcars = dataSnapshot.getValue(Cars.class);
                Picasso.with(getBaseContext()).load(currentcars.getImage())
                        .into(car_image);
                tv_price.setText(currentcars.getPrice());
                tv_name.setText(currentcars.getName());
                tv_description.setText(currentcars.getDescription());





            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onNegativeButtonClicked() {

    }

    @Override
    public void onPositiveButtonClicked(int value, @NotNull String comments) {
        /// get rating and upload to firebase
        final Rating rating = new Rating(Common.currentUser.getPhone(),
                carId,
                String.valueOf(value),
                comments);
        ratingTb1.child(Common.currentUser.getPhone()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(Common.currentUser.getPhone()).exists())
                {
                    //Remove old value
                    ratingTb1.child(Common.currentUser.getPhone()).removeValue();
                    ///Update new value
                    ratingTb1.child(Common.currentUser.getPhone()).setValue(rating);
                }
                else {
                    /// Update new value
                    ratingTb1.child(Common.currentUser.getPhone()).setValue(rating);
                }
                Toast.makeText(CarDetail.this, "Thank you for submit rating", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
