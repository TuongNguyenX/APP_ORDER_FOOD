package com.example.firebase.Model;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.firebase.Common.Common;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class Tala {

//    Query foodRating = ratingTBL.child(Common.currentUser.getPhone()).child("currentId").orderByChild("foodId").equalTo(foodId);
//        foodRating.addValueEventListener(new ValueEventListener() {
//        int count,sum = 0;
//        @Override
//        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//            for(DataSnapshot ds :dataSnapshot.getChildren()){
//                Rating item = ds.getValue(Rating.class);
//                sum = sum + Integer.parseInt(item.getRateValue());
//                count++;
//
//            }
//            if(count != 0){
//
//                ratingBar.setRating(sum/count);
//            }
//
//        }
//
//        @Override
//        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//        }
//    });
//
//
//
//    @Override
//    public void onPositiveButtonClicked(int value, @NotNull String comments) {
//        final Rating rating = new Rating(foodId, Common.currentUser.getPhone(), String.valueOf(value), comments);
//        ratingTBL.child(Common.currentUser.getPhone()).child("currentId").child(foodId).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if(dataSnapshot.child(Common.currentUser.getPhone()).child("currentId").child(foodId).exists())
//                {
//                    //Remove old value
//                    ratingTBL.child(Common.currentUser.getPhone()).child("currentId").child(foodId).removeValue();
//                    //update new value
//                    ratingTBL.child(Common.currentUser.getPhone()).child("currentId").child(foodId).setValue(rating);
//                    Toast.makeText(FoodDetails.this,"Thank you for Feedback", Toast.LENGTH_SHORT).show();
//                    finish();
//
//                }
//                else
//                {
//                    //update new value
//                    ratingTBL.child(Common.currentUser.getPhone() ).child("currentId").child(foodId).setValue(rating);
//                    Toast.makeText(FoodDetails.this,"Thank you for Feedback", Toast.LENGTH_SHORT).show();
//                    finish();
//                }
//            }
//
//        }
}
