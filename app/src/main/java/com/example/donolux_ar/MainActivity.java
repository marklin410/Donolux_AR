package com.example.donolux_ar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.ar.core.Anchor;
import com.google.ar.core.AugmentedImage;
import com.google.ar.core.Config;
import com.google.ar.core.Frame;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.core.Session;
import com.google.ar.core.TrackingState;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.ArSceneView;
import com.google.ar.sceneform.Camera;
import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.Scene;
import com.google.ar.sceneform.Sun;
import com.google.ar.sceneform.assets.RenderableSource;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.core.AugmentedImageDatabase;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.BaseArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Scene.OnUpdateListener {



    private CustomARFragment arFragment;


    private String model3D;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
       // mScaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());


        arFragment = (CustomARFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);

        model3D = getIntent().getStringExtra("model3D");
        // arFragment.setUpSession(this);
        arFragment.getArSceneView().getScene().addOnUpdateListener(this);


        Toast.makeText(this, model3D, Toast.LENGTH_LONG).show();
        /*Intent intent = getIntent();
        modelSFB = intent.getStringExtra("modelSFB");
        if(modelSFB==null) {
            modelSFB = "t111010_1.sfb";
        }*/

        ImageButton menu_bt = findViewById(R.id.menu_button);
        menu_bt.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openMenu();
                    }
                }
        );

    }
    public boolean  buildDatabase(Config config, Session session)  {
        AugmentedImageDatabase augmentedImageDatabase;
        Bitmap augmentedImageBitmap = loadAugmentedImage();
        if (augmentedImageBitmap == null) {
            return false;
        }
        augmentedImageDatabase = new AugmentedImageDatabase(session);
        augmentedImageDatabase.addImage("basepicture", augmentedImageBitmap);
        config.setAugmentedImageDatabase(augmentedImageDatabase);
        return true;
        //        InputStream is = getResources().openRawResource(R.raw.imagedb);
//            Bitmap basePicture = BitmapFactory.decodeStream(is);
//        AugmentedImageDatabase aid = null;
//        try {
//            aid = AugmentedImageDatabase.deserialize(session, is);
//            config.setAugmentedImageDatabase(aid);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        // aid.addImage("basepicture", basePicture);
    }


    //config can be intiated using new Config(session) where session is arfragment scene session

    private Bitmap loadAugmentedImage(){
        try (InputStream is = getAssets().open("qrforar.jpeg")){
            return BitmapFactory.decodeStream(is);
        }
        catch (IOException e){
            Log.e("ImageLoad", "IO Exception while loading", e);
        }
        return null;
    }
    public void openMenu(){
        Context c = MainActivity.this;
        Intent intent = new Intent(c, MenuActivity.class);
        intent.putExtra("where", "mainActivity");
        c.startActivity(intent);
    }

//    public void setUpDatabase(Config config, Session session){
//        InputStream is = getAssets()
//        Bitmap basePicture = BitmapFactory.decodeResource(this.getResources(), R.drawable.qrforar);
//        AugmentedImageDatabase aid = new AugmentedImageDatabase(session);
//        aid.addImage("basepicture", basePicture);
//        config.setAugmentedImageDatabase(aid);
//    }

    @Override
    public void onUpdate(FrameTime frameTime) {
        Frame frame = arFragment.getArSceneView().getArFrame();
        Collection<AugmentedImage> images = frame.getUpdatedTrackables(AugmentedImage.class);
        Log.i("modelRender", "update Tracking ");

        for (AugmentedImage augmentedImage : images) {
            Log.i("modelRender", "cur augmented image " + augmentedImage.getName());

            if (augmentedImage.getTrackingState() == TrackingState.TRACKING) {
                Log.i("modelRender", "tracking  augmented image " + augmentedImage.getName());

                if (augmentedImage.getName().equals("basepicture")) {
//                    MyARNode node = new MyARNode(this, model3D);
//                    node.setImage(augmentedImage);
//                    arFragment.getArSceneView().getScene().addChild(node);
                    Log.i("modelRender", "found augmented image 2");
                    setUpModel(model3D, augmentedImage.createAnchor(augmentedImage.getCenterPose()));
                }
            }
        }

    }


    private void setUpModel(String model, Anchor anchor) {
        Log.i("modelRender", "start model render");
        ModelRenderable
                .builder()
                .setSource(this,
                        RenderableSource
                        .builder()
                        .setSource(this, Uri.parse(model), RenderableSource.SourceType.GLTF2)
                        .build()
                )
                .setRegistryId(model)
                .build()
                .thenAccept(modelRenderable -> placeModel(modelRenderable, anchor))
                .exceptionally(throwable -> {
                    Log.i("modelRender", throwable.getMessage() + "cant load");
                    Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_LONG).show();
                    return null;
                });
    }

    private void placeModel(ModelRenderable renderable, Anchor anchor) {
        AnchorNode node = new AnchorNode(anchor);
        node.setParent(arFragment.getArSceneView().getScene());
        node.setRenderable(renderable);
//        TransformableNode tnode = new TransformableNode(arFragment.getTransformationSystem());
//
//        tnode.setParent(node);
//        tnode.setRenderable(renderable);
//        tnode.select();
//
//        if(modelSFB.equals("w111045_2bchrome.sfb")||modelSFB.equals("w111047_1s_nickel.sfb")){
//            Quaternion rotation2 = Quaternion.axisAngle(new Vector3(1f, 0, 0), -90f);
//            Quaternion rotation1 = Quaternion.axisAngle(new Vector3(0, 0, 1f), 180f);
//            tnode.setLocalRotation(Quaternion.multiply(rotation1,rotation2));
//        }
        arFragment.getArSceneView().getScene().addChild(node);
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        arFragment.setUpSession(this);
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        arFragment.checkPause();
//    }

    /* private void destroyModels(){
        List<Node> children = new ArrayList<>(arFragment.getArSceneView().getScene().getChildren());
        if(children != null) {
            for (Node node : children) {
                if (node instanceof AnchorNode) {
                    if (((AnchorNode) node).getAnchor() != null) {
                        ((AnchorNode) node).getAnchor().detach();
                    }
                }
                if (!(node instanceof Camera) && !(node instanceof Sun)) {
                    node.setParent(null);
                }
            }
        }
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        //destroyModels();
        switch (menuItem.getItemId()){
            case R.id.t1110101:
                modelSFB="t111010_1.sfb";
                break;
            case R.id.w1110452b:
                modelSFB = "w111045_2bchrome.sfb";
                break;
            case R.id.w1110471s:
                modelSFB="w111047_1s_nickel.sfb";
                break;
        }
        return false;
    }*/




//    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
//        @Override
//        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
//            mScaleFactor *= scaleGestureDetector.getScaleFactor();
//            mScaleFactor = Math.max(0.1f,
//                    Math.min(mScaleFactor, 10.0f));
//            TransformableNode node = new TransformableNode(arFragment.getTransformationSystem());
//            node.getScaleController().setMaxScale(mScaleFactor);
//            node.getScaleController().setMinScale(mScaleFactor);
//            List<Node> children = new ArrayList<>(arFragment.getArSceneView().getScene().getChildren());
//            for (Node anchorNode : children) {
//                if (anchorNode instanceof AnchorNode) {
//                    node.setParent(anchorNode);
//                    node.setRenderable(modelRenderable);
//                    node.select();
//                }
//            }
//            return true;
//        }
//    }


}




