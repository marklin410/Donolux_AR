package com.example.donolux_ar;

import android.content.Context;
import android.net.Uri;

import com.google.ar.core.AugmentedImage;
import com.google.ar.core.Pose;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;

import java.util.concurrent.CompletableFuture;

public class MyARNode extends AnchorNode {
    private AugmentedImage img;
    private static CompletableFuture<ModelRenderable> modelRenderableCompletableFuture;

    public MyARNode(Context context, String model){
        if(modelRenderableCompletableFuture == null){
            modelRenderableCompletableFuture = ModelRenderable.builder()
                    .setRegistryId(model)
//                    .setSource(context,RenderableSource
//                            .builder()
//                            .setSource(context, Uri.parse(model), RenderableSource.SourceType.GLTF2)
//                            .setRecenterMode(RenderableSource.RecenterMode.ROOT)
//                            .build())
                    .build();
        }
    }

    public void setImage(AugmentedImage image){
        this.img = image;
        if(!modelRenderableCompletableFuture.isDone()){
            CompletableFuture.allOf(modelRenderableCompletableFuture)
                    .thenAccept((Void aVoid) ->{
                        setImage(img);
                    })
                    .exceptionally(throwable -> {return null;});
        }

        setAnchor(img.createAnchor(img.getCenterPose()));
        Node node = new Node();
        Pose pose = Pose.makeTranslation(0.0f,0.0f,0.25f);
        node.setParent(this);
        node.setLocalPosition(new Vector3(pose.tx(),pose.ty(), pose.tz()));
        node.setLocalRotation(new Quaternion(pose.qx(), pose.qy(), pose.qz(), pose.qw()));
        node.setRenderable(modelRenderableCompletableFuture.getNow(null));
    }

    public AugmentedImage getImg() {
        return img;
    }
}
