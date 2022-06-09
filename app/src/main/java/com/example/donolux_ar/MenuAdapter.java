package com.example.donolux_ar;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MenuAdapter extends RecyclerView.Adapter<MenuHolder> {

    Context c;
    ArrayList<Model> models;

    public MenuAdapter(Context c, ArrayList<Model> models) {
        this.c = c;
        this.models = models;
    }

    @NonNull
    @Override
    public MenuHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.table_item,null);

        return new MenuHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MenuHolder holder, int position) {

        holder.titleTV.setText(models.get(position).getTitle());
       // holder.subtitleTV.setText(models.get(position).getSubtitle());
        holder.imageView.setImageResource(models.get(position).getImg());
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(c);
                ImageView iv = new ImageView(c);
                iv.setImageResource(models.get(position).getImg());
                iv.setAdjustViewBounds(true);
                builder.setView(iv);
                AlertDialog dialog = builder.create();
                iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                layoutParams.copyFrom(dialog.getWindow().getAttributes());
                layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
                layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
                dialog.getWindow().setAttributes(layoutParams);
                dialog.show();
            }
        });
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClickListener(View v, int position) {
                String model3D = models.get(position).getModel3D();
                Intent intent = new Intent(c, MainActivity.class);
                intent.putExtra("model3D", model3D);
                c.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return models.size();
    }
}
