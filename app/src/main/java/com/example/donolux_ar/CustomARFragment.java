package com.example.donolux_ar;

import android.Manifest;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Toast;

import com.google.ar.core.AugmentedImageDatabase;
import com.google.ar.core.Config;
import com.google.ar.core.Session;
import com.google.ar.core.exceptions.CameraNotAvailableException;
import com.google.ar.core.exceptions.UnavailableApkTooOldException;
import com.google.ar.core.exceptions.UnavailableArcoreNotInstalledException;
import com.google.ar.core.exceptions.UnavailableDeviceNotCompatibleException;
import com.google.ar.core.exceptions.UnavailableException;
import com.google.ar.core.exceptions.UnavailableSdkTooOldException;
import com.google.ar.sceneform.ArSceneView;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.BaseArFragment;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

public class CustomARFragment extends ArFragment {

    private Session session;
    private boolean shouldConfigureSession = false;


    public void checkPause(){
        if(session!=null){
            this.getArSceneView().pause();
            session.pause();
        }
    }
    public void setUpSession(Context context){
        Dexter.withContext(context)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        startSession(context);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        Toast.makeText(context, "Need permission", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                    }
                }).check();

    }

    private void startSession(Context context){
        if(session == null){
            try{
                Log.i("sessionCreation", "startSessionCreate");
                session = new Session(context);
                shouldConfigureSession = true;
            } catch (Exception e) {
                Log.e("sessionCreation", e +" problem here");
            }
        }

        if(shouldConfigureSession){
            configSession(context);
            shouldConfigureSession = false;
            this.getArSceneView().setupSession(session);
            try{
                session.resume();
                this.getArSceneView().resume();
            } catch (CameraNotAvailableException e) {
                e.printStackTrace();
                session = null;
                return;
            }
        }

    }

    private void configSession(Context context) {
        Config config = new Config(session);
        config.setUpdateMode(Config.UpdateMode.LATEST_CAMERA_IMAGE);
        config.setFocusMode(Config.FocusMode.AUTO);
        session.configure(config);
        MainActivity activity = (MainActivity) getActivity();
        buildDatabase(config, context);
        Log.d("SetupAugImgDb", "Success");

    }

    private void  buildDatabase(Config config, Context context) {
        InputStream is = null;
        try {
            is = context.getAssets().open("qrforar.jpeg");
            Bitmap basePicture = BitmapFactory.decodeStream(is);
            AugmentedImageDatabase aid = new AugmentedImageDatabase(session);
            aid.addImage("basepicture", basePicture);
            config.setAugmentedImageDatabase(aid);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Override
    protected Config getSessionConfiguration(Session session) {
        Config config = new Config(session);
        config.setUpdateMode(Config.UpdateMode.LATEST_CAMERA_IMAGE);
        config.setFocusMode(Config.FocusMode.AUTO);
        session.configure(config);
        MainActivity activity = (MainActivity) getActivity();
        assert activity != null;
        activity.buildDatabase(config, session);
        this.getArSceneView().setupSession(session);
        Log.d("SetupAugImgDb", "Success");
        return config;
    }

}
