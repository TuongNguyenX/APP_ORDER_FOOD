package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup;

import com.example.firebase.Common.Common;
import com.example.firebase.Model.Category2;
import com.example.firebase.Model.Rating;
import com.example.firebase.ViewHolder.MenuViewHolder2;
import com.example.firebase.ViewHolder.ShowCommentsViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ShowComments extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    FirebaseRecyclerAdapter<Rating, ShowCommentsViewHolder>adapter;

    String foodId = "";



    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_comments);

//        ///firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Rating");
//        ///
        recyclerView = findViewById(R.id.recycler_comments);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
//        //
//        ///Swipe Layout

                if (getIntent() != null) {
                    foodId  = getIntent().getStringExtra(Common.INTENT_FOOD_ID);
                }
                if (!foodId.isEmpty() && foodId != null){
                    ////Create request query
//                    Query query  = databaseReference.orderByChild("foodId").equalTo(foodId);
                    getComment();
                }
    }

    private void getComment() {
        adapter = new FirebaseRecyclerAdapter<Rating, ShowCommentsViewHolder>
                (Rating.class,R.layout.item_show_comments, ShowCommentsViewHolder.class,databaseReference) {
            @Override
            protected void populateViewHolder(ShowCommentsViewHolder showCommentsViewHolder, Rating rating, int i) {
                showCommentsViewHolder.txtPhone.setText(rating.getUserPhone());
                showCommentsViewHolder.txtComments.setText(rating.getComment());
                showCommentsViewHolder.txtName.setText(rating.getFoodId());////
                showCommentsViewHolder.ratingBar.setRating(Float.parseFloat(rating.getRateValue()));

            }
        };
        recyclerView.setAdapter(adapter);
    }


}



