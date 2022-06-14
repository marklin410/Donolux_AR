package com.example.donolux_ar;


import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.ar.core.Anchor;
import com.google.ar.core.AugmentedImage;
import com.google.ar.core.Config;
import com.google.ar.core.Frame;
import com.google.ar.core.Session;
import com.google.ar.core.TrackingState;
import com.google.ar.core.exceptions.CameraNotAvailableException;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.Scene;
import com.google.ar.sceneform.assets.RenderableSource;
import com.google.ar.sceneform.rendering.Color;
import com.google.ar.sceneform.rendering.Material;
import com.google.ar.sceneform.rendering.MaterialFactory;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.core.AugmentedImageDatabase;
import com.google.ar.sceneform.rendering.Renderable;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class MainActivity extends AppCompatActivity implements Scene.OnUpdateListener {



    private CustomARFragment arFragment;


    private static Model model3D;
    Anchor anchor;
    AnchorNode node;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        arFragment = (CustomARFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);
        model3D = getIntent().getParcelableExtra("model3D");
        arFragment.getArSceneView().getScene().addOnUpdateListener(this);

        ImageButton imageButton = findViewById(R.id.menu_button);
        final PopupMenu dropDownMenu = new PopupMenu(this, imageButton);

        final Menu menu = dropDownMenu.getMenu();
        menu.add(0, 1, 1, menuIconWithText(getDrawable(R.drawable.circle), "White"));
        menu.add(0, 1, 1, menuIconWithText(getDrawable(R.drawable.circle_black), "Black"));
        dropDownMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case 0:
                    model3D.setColor(android.graphics.Color.argb(0,255,255,255));
                    model3D.setModel3D("t11022_white");
                   // if(anchor != null) setUpModel(model3D.getModel3D(), anchor);
                    if (node != null) {
                        ArrayList<Node> nodeList = new ArrayList<>
                                (arFragment.getArSceneView().getScene().getChildren());
                        for (Node childNode : nodeList) {
                            if (childNode instanceof AnchorNode) {
                                if (((AnchorNode) childNode).getAnchor() != null) {
                                    ((AnchorNode) childNode).getAnchor().detach();
                                    (childNode).setParent(null); } } }
                    } else {
                        Toast.makeText(this, "Test Delete - markAnchorNode was null", Toast.LENGTH_SHORT).show();
                    }

                    return true;
                case 1:
                    model3D.setColor(android.graphics.Color.argb(0,10,10,10));
                    model3D.setModel3D("t11022");
                    if (node != null) {
                        ArrayList<Node> nodeList = new ArrayList<>
                                (arFragment.getArSceneView().getScene().getChildren());
                        for (Node childNode : nodeList) {
                            if (childNode instanceof AnchorNode) {
                                if (((AnchorNode) childNode).getAnchor() != null) {
                                    ((AnchorNode) childNode).getAnchor().detach();
                                    (childNode).setParent(null); } } }                    }
                    else {
                        Toast.makeText(this, "Test Delete - markAnchorNode was null", Toast.LENGTH_SHORT).show();
                    }

                    //if(anchor != null) setUpModel(model3D.getModel3D(), anchor);
                    return true;
            }
            return false;
        });

        imageButton.setOnClickListener(v -> dropDownMenu.show());
    }

    private CharSequence menuIconWithText(Drawable r, String title) {

        r.setBounds(0, 0, r.getIntrinsicWidth(), r.getIntrinsicHeight());
        SpannableString sb = new SpannableString("    " + title);
        ImageSpan imageSpan = new ImageSpan(r, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return sb;
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
    }

    private Bitmap loadAugmentedImage(){
        try (InputStream is = getAssets().open("qrforar.jpeg")){
            return BitmapFactory.decodeStream(is);
        }
        catch (IOException e){
            Log.e("ImageLoad", "IO Exception while loading", e);
        }
        return null;
    }


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
                    anchor = augmentedImage.createAnchor(augmentedImage.getCenterPose());
                    setUpModel(model3D.getModel3D(), augmentedImage.createAnchor(augmentedImage.getCenterPose()));
                }
            }
        }
    }


    private void setUpModel(String model, Anchor anchor) {
        Log.i("modelRender", "start model render");
        String modelLink = "https://github.com/marklin410/Donolux_AR/blob/master/app/src/main/res/raw/t111010_1.sfb";

        ModelRenderable
                    .builder()
                    .setSource(this, getResources().getIdentifier(model, "raw",getPackageName()))
//                .setSource(this, RenderableSource.builder().setSource(
//                        this,
//                        Uri.parse(modelLink), RenderableSource.SourceType.GLTF2).build())
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
        node = new AnchorNode(anchor);
        node.setParent(arFragment.getArSceneView().getScene());
        CompletableFuture<Material> materialCompletableFuture =
                MaterialFactory.makeOpaqueWithColor(this,  new Color(0, 255, 244));

        materialCompletableFuture.thenAccept(material -> {
            Renderable r2 = renderable.makeCopy();
            r2.setMaterial(material);
            node.setRenderable(r2);

        });
        //renderable.getMaterial().setFloat3("baseColor",new Color(model3D.getColor()));
        //node.setRenderable(renderable);
        arFragment.getArSceneView().getScene().addChild(node);
    }


}




