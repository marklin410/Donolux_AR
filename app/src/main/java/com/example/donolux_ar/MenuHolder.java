package com.example.donolux_ar;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MenuHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    ImageView imageView;
    TextView titleTV, subtitleTV;
    ItemClickListener listener;

    public MenuHolder(@NonNull View itemView) {
        super(itemView);
        this.imageView = itemView.findViewById(R.id.icon_iv);
        this.titleTV =itemView.findViewById(R.id.title_tv);
        this.subtitleTV=itemView.findViewById(R.id.subtitle_tv);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        this.listener.onItemClickListener(v, getLayoutPosition());
    }
    public void setItemClickListener(ItemClickListener ic){
        this.listener = ic;
    }


}
