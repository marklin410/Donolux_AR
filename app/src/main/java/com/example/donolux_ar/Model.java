package com.example.donolux_ar;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

public class Model implements Parcelable {

    private String title, subtitle;
    private int img;
    private String model3D;
    private int color;


    protected Model(Parcel in) {
        title = in.readString();
        subtitle = in.readString();
        img = in.readInt();
        model3D = in.readString();
    }

    protected Model(){

    }

    public static final Creator<Model> CREATOR = new Creator<Model>() {
        @Override
        public Model createFromParcel(Parcel in) {
            return new Model(in);
        }

        @Override
        public Model[] newArray(int size) {
            return new Model[size];
        }
    };

    public String getModel3D() {
        return model3D;
    }

    public void setModel3D(String model3D) {
        this.model3D = model3D;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(subtitle);
        parcel.writeInt(img);
        parcel.writeString(model3D);
    }


    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
