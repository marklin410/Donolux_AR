package com.example.donolux_ar;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    MenuAdapter adapter;
    ArrayList<Model> models;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        ActionBar actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#000000"));

        actionBar.setBackgroundDrawable(colorDrawable);

        recyclerView = findViewById(R.id.mRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new MenuAdapter(this, getMenuList());
        recyclerView.setAdapter(adapter);

    }

    private ArrayList<Model> getMenuList() {
        models = new ArrayList<>();

        Model model = new Model();
        model.setTitle("W111045 2B Chrome");
        model.setImg(R.drawable.w111045_2bchrome);
        model.setSubtitle("Серия: LONDON \nЦена: 12553 р.");
        models.add(model);

        model = new Model();
        model.setTitle("W111047 1S Nickel");
        model.setImg(R.drawable.w111047_1snickel);
        model.setSubtitle("Серия: MUNICH \nЦена: 6236 р.");
        models.add(model);

        model = new Model();
        model.setTitle("Donolux T111010-1");
        model.setImg(R.drawable.t111010_1);
        model.setSubtitle("Серия: CLS \nЦена: 3310 р.");
        models.add(model);

        return models;
    }

    public void showDialogImage(int model){


    }
}

