package com.example.chris.gitarrverkstad;

/**
 * Created by stefa_000 on 2017-02-13.
 */

public class GalleryItem {
    private String instrumentID;
    private int price;
    private String desc;
    private String model;
    private String creator;
    private int imageID;
    private String prevown;

    public GalleryItem(String desc, int imageID, String model, int price, String creator, String prevown, String instrumentID){
        this.price = price;
        this.desc = desc;
        this.model = model;
        this.imageID = imageID;
        this.creator = creator;
        this.instrumentID = instrumentID;
        this.prevown = prevown;
    }

    public String getPrevown() {
        return prevown;
    }

    public void setPrevown(String prevown) {
        this.prevown = prevown;
    }

    public String getInstrumentID() {
        return instrumentID;
    }

    public void setInstrumentID(String instrumentID) {
        this.instrumentID = instrumentID;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public int getImageID() {
        return imageID;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }

}
