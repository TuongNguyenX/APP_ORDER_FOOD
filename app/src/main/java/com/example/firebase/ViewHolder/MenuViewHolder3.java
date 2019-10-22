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

public class MenuViewHolder3 extends RecyclerView.ViewHolder implements
        View.OnClickListener,
        View.OnCreateContextMenuListener {

    public TextView txtMenuName3;
    public ImageView imgMenuImage3;
    private ItemClickListener itemClickListener;

    public MenuViewHolder3(@NonNull View itemView) {
        super(itemView);
        txtMenuName3 = itemView.findViewById(R.id.menu_name3);
        imgMenuImage3 = itemView.findViewById(R.id.menu_image3);

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
