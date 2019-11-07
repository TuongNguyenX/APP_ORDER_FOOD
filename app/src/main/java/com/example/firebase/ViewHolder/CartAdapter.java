package com.example.firebase.ViewHolder;

import android.content.Context;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
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

class CartViewHold extends RecyclerView.ViewHolder implements
        View.OnClickListener, View.OnCreateContextMenuListener {

    public TextView txt_car_name,txt_price;
    public ImageView img_cart_count,img_delete_cart,img_info;
    public ElegantNumberButton btn_quantity;
    private ItemClickListener itemClickListener;

    public void setTxt_car_name(TextView txt_car_name) {
        this.txt_car_name = txt_car_name;
    }

    public CartViewHold(@NonNull View itemView) {
        super(itemView);
        img_info = itemView.findViewById(R.id.img_infocart);
        img_delete_cart = itemView.findViewById(R.id.delete_cart);
        txt_car_name = itemView.findViewById(R.id.cart_item_name);
        txt_price = itemView.findViewById(R.id.cart_item_price);
        img_cart_count =itemView.findViewById(R.id.cart_item_count);

        itemView.setOnCreateContextMenuListener(this);


    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        ///
        menu.setHeaderTitle("Select ation");
        menu.add(0,0,getAdapterPosition(), Common.DELETE);

    }
}
public class CartAdapter extends RecyclerView.Adapter<CartViewHold> {

    private List<Order> listData = new ArrayList<>();
    private Context context;


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
    public void onBindViewHolder(@NonNull CartViewHold cartViewHold,  final int i) {


        Picasso.with(context)
                .load(listData.get(i).getImage())
                .into(cartViewHold.img_info);


        TextDrawable drawable = TextDrawable.builder()
                .buildRound(""+listData.get(i).getQuantity(), Color.RED);//da kiem tra
        cartViewHold.img_cart_count.setImageDrawable(drawable);//da kiem tra
        cartViewHold.img_delete_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context, "kkk", Toast.LENGTH_SHORT).show();

//                deleteCart(.getOrder());
            }
        });
        /////////////////////////////////////////////////////////////////

        Locale locale = new Locale("vi","VN");
        NumberFormat numberFormat =NumberFormat.getCurrencyInstance(locale);//
        int price =(Integer.parseInt(listData.get(i).getPrice()))*(Integer.parseInt(listData.get(i).getQuantity()));
        cartViewHold.txt_price.setText(numberFormat.format(price));//d
        cartViewHold.txt_car_name.setText(listData.get(i).getProductName());//


    }

    @Override
    public int getItemCount() {
        return listData.size();//da kiem  tra
    }
}
