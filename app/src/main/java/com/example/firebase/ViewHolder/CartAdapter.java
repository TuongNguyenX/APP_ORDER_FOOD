package com.example.firebase.ViewHolder;

import android.content.Context;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.firebase.Cart;
import com.example.firebase.Common.Common;
import com.example.firebase.Database.Database;
import com.example.firebase.Interface.ItemClickListener;
import com.example.firebase.Model.Order;
import com.example.firebase.R;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class CartAdapter extends RecyclerView.Adapter<CartViewHold> {

    private List<Order> listData = new ArrayList<>();
    private Context context;


    List<Order> carts = new ArrayList<>();


    public CartAdapter(List<Order> listData, Context context) {
        this.listData = listData;
        this.context = context;

    }

    @NonNull
    @Override
    public CartViewHold onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView =inflater.inflate(R.layout.item_cart_layout,viewGroup,false);
        return new CartViewHold(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final CartViewHold cartViewHold, final int i) {

        Picasso.with(context)
                .load(listData.get(i).getImage())
                .into(cartViewHold.img_info);
        TextDrawable drawable = TextDrawable.builder()
                .buildRound(""+listData.get(i).getQuantity(), Color.RED);//da kiem tra
        cartViewHold.img_cart_count.setImageDrawable(drawable);//da kiem tra
        cartViewHold.img_delete_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Order order = listData.get(i);

                Toast.makeText(context, order.getProductName()+" was deleted", Toast.LENGTH_SHORT).show();


            }
        });
        /////////////////////////////////////////////////////////////////
        Locale locale = new Locale("vi","VN");
        NumberFormat numberFormat =NumberFormat.getCurrencyInstance(locale);//
        int price =(Integer.parseInt(listData.get(i).getPrice()))*(Integer.parseInt(listData.get(i).getQuantity()));
        cartViewHold.txt_price.setText(numberFormat.format(price));//d
        cartViewHold.txt_car_name.setText(listData.get(i).getProductName());//

//
//        try {
//            // Initialize a new GradientDrawable
//            GradientDrawable gd = new GradientDrawable();
//            // Specify the shape of drawable
//            gd.setShape(GradientDrawable.RECTANGLE);
//            // Set the fill colors of drawable
//            gd.setColor(Color.parseColor(listData.get(i).getProductName()));
//            // Make the border rounded
//            gd.setCornerRadii(new float[]{0, 0, 40.0f, 40.0f, 40.0f, 40.0f, 0, 0}); // border corner radius
//            cartViewHold.txt_car_name.setBackground(gd);
//        } catch (Exception e) {
//            Log.d("error_show", e.toString());
//        }

    }
    @Override
    public int getItemCount() {
        return listData.size();
    }

    private void deleteCart(int order) {
        //// Remove item ai list<Order> by position
        carts.remove(order);
        ////After that, delete all old   date from  SQlite
        new Database(context).cleanCart();
        /////After final,update new data from List<Order> to SQLite
        for (Order item:carts)
            new Database(context).addToCart(item);
//        loadListCart();

    }
}
