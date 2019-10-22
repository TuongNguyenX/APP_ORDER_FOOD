package com.example.firebase.ViewHolder;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.firebase.Common.Common;
import com.example.firebase.Interface.ItemClickListener;
import com.example.firebase.R;

public class CarsViewHold extends RecyclerView.ViewHolder implements
        View.OnClickListener,
        View.OnCreateContextMenuListener
{
    public TextView cars_name,cars_price;
    public ImageView cars_image,share_img;
    private ItemClickListener itemClickListener;


    public CarsViewHold(@NonNull View itemView) {
        super(itemView);

        cars_name=itemView.findViewById(R.id.cars_name);
        cars_image = itemView.findViewById(R.id.cars_image);
        share_img = itemView.findViewById(R.id.btnShare);
        cars_price = itemView.findViewById(R.id.cars_price);



        itemView.setOnCreateContextMenuListener(this);

        itemView.setOnClickListener(this);
    }
    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Select  the action");
        menu.add(0,1,getAdapterPosition(), Common.UPDATE);
        menu.add(0,0,getAdapterPosition(),Common.DELETE);
    }
}
