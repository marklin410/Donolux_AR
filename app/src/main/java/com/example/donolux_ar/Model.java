package com.example.donolux_ar;

public class Model {

    private String title, subtitle;
    private int img;
    String model3D;


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
}
