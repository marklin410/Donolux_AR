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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
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
        recyclerView = findViewById(R.id.rv_holder);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        TextView desc = findViewById(R.id.desc_text);
            String curMenu = getIntent().getStringExtra("curMenu");
        if(curMenu == null) curMenu = "Types";
        switch (curMenu){
            case "Types":  adapter = new MenuAdapter(this, getTypesList());
                desc.setText(R.string.type_catalog);
                break;
            case "Series":  adapter = new MenuAdapter(this, getSeriesList());
                desc.setText(R.string.series);
                break;
            case "Items":  adapter = new MenuAdapter(this, getMenuList());
                desc.setText(R.string.items);
                break;
        }

        recyclerView.setAdapter(adapter);
    }

    private ArrayList<Model> getTypesList() {
        models = new ArrayList<>();

        Model model = new Model();
        model.setTitle("Встраиваемые светильники");
        model.setImg(R.drawable.vstraivaem);
        //model.setSubtitle("Серия: LONDON \nЦена: 12553 р.");
        models.add(model);

        model = new Model();
        model.setTitle("Накладные светильники");
        model.setImg(R.drawable.nakladn);
       // model.setSubtitle("Серия: MUNICH \nЦена: 6236 р.");
        models.add(model);

        model = new Model();
        model.setTitle("Подвесные светильники");
        model.setImg(R.drawable.podvesn);
        //model.setSubtitle("Серия: CLS \nЦена: 3310 р.");
        models.add(model);

        model = new Model();
        model.setTitle("Настенные светильники");
        model.setImg(R.drawable.nasten);
        //model.setSubtitle("Серия: CLS \nЦена: 3310 р.");
        models.add(model);

        model = new Model();
        model.setTitle("Настольные/напольные светильники");
        model.setImg(R.drawable.t111022);
       // model.setSubtitle("Серия: CLS \nЦена: 3310 р.");
        models.add(model);

        return models;
    }

    private ArrayList<Model> getSeriesList() {
        models = new ArrayList<>();

        Model model = new Model();
        model.setTitle("Saga");
        model.setImg(R.drawable.saga);
        //model.setSubtitle("Серия: LONDON \nЦена: 12553 р.");
        models.add(model);

        model = new Model();
        model.setTitle("Prague");
        model.setImg(R.drawable.prague);
        // model.setSubtitle("Серия: MUNICH \nЦена: 6236 р.");
        models.add(model);

        model = new Model();
        model.setTitle("Kaa");
        model.setImg(R.drawable.kaa);
        //model.setSubtitle("Серия: CLS \nЦена: 3310 р.");
        models.add(model);

        model = new Model();
        model.setTitle("Soho");
        model.setImg(R.drawable.nasten);
        //model.setSubtitle("Серия: CLS \nЦена: 3310 р.");
        models.add(model);

        return models;
    }

    private ArrayList<Model> getMenuList() {
        models = new ArrayList<>();

        Model model = new Model();
        model.setTitle("T111022/1black");
        model.setImg(R.drawable.t111022);
        model.setModel3D("t11022");
        model.setColor(Color.rgb(10,10,10));
        model.setSubtitle("Стоимость: 11736 р.");
        models.add(model);

        model = new Model();
        model.setTitle("T111022/2black");
        model.setImg(R.drawable.t111022_2black);
        model.setSubtitle("Стоимость: 21592 р.");
        model.setColor(Color.rgb(10,10,10));

        models.add(model);

        model = new Model();
        model.setTitle("T111022/1white");
        model.setModel3D("t11022_white");
        model.setImg(R.drawable.t111022_1white);
        model.setSubtitle("Стоимость: 7161 р.");
        model.setColor(Color.rgb(255,255,255));

        models.add(model);

        model = new Model();
        model.setTitle("T111022/2white");
        model.setImg(R.drawable.t111022_2white);
        model.setSubtitle("Стоимость: 21592 р.");
        model.setColor(Color.rgb(255,255,255));

        models.add(model);

        return models;
    }

    public void showDialogImage(int model){


    }
}

