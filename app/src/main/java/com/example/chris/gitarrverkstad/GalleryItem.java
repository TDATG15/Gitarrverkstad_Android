package com.example.chris.gitarrverkstad;

/**
 * Created by stefa_000 on 2017-02-13.
 */

public class GalleryItem {
    private String name;
    private double price;
    private String desc;
    private int imageID;
    public GalleryItem(String name, String desc, int imageID, double price){
        this.name = name;
        this.price = price;
        this.desc = desc;
        this.imageID = imageID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getImageID() {
        return imageID;
    }

    public void setImageID(int image) {
        this.imageID = image;
    }
}
