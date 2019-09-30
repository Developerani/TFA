package com.example.tfa;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;


public class viewHolder extends RecyclerView.ViewHolder {

    public ImageView mimageView;

    public viewHolder(View itemView) {
        super(itemView);
        mimageView = itemView.findViewById(R.id.imageView);
    }
}
