package com.example.chris.gitarrverkstad;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.List;

/**
 * Created by stefa_000 on 2017-02-28.
 */

@Root(name="guitar", strict = false)
public class Instrument {

    @Element(name = "img")
    private String img;

    @Element(name = "descr")
    private String beskrivning;

    @Element(name = "gid")
    private int instrumentId;

    @Element(name = "model")
    private String model;

    @Element(name = "price")
    private String pris;

    @Element(name="prvowner")
    private String tidigareagare;

    @Element(name = "manf")
    private String tillverkare;

    public String getBeskrivning() {
        return beskrivning;
    }

    public void setBeskrivning(String beskrivning) {
        this.beskrivning = beskrivning;
    }

    public int getInstrumentId() {
        return instrumentId;
    }

    public void setInstrumentId(int instrumentId) {
        this.instrumentId = instrumentId;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getPris() {
        return pris;
    }

    public void setPris(String pris) {
        this.pris = pris;
    }

    public String getTidigareAgare() {
        return tidigareagare;
    }

    public void setTidigareAgare(String tidigareagare) {
        this.tidigareagare = tidigareagare;
    }

    public String getTillverkare() {
        return tillverkare;
    }

    public void setTillverkare(String tillverkare) {
        this.tillverkare = tillverkare;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTidigareagare() {
        return tidigareagare;
    }

    public void setTidigareagare(String tidigareagare) {
        this.tidigareagare = tidigareagare;
    }

    public Instrument(){
        super();
    }

    public Instrument(String beskrivning, int instrumentId, String model, String pris, String tidigareagare, String tillverkare, String img){
        if(beskrivning.equals(""))
            this.beskrivning = "Beskrivning";
        else
            this.beskrivning = beskrivning;
        this.instrumentId = instrumentId;
        if(img == null){
            this.img = "/9j/4AAQSkZJRgABAQEAZABkAAD/";
        }
        else {
            this.img = img;
        }
        if(model.equals(""))
            this.model = "Modell";
        else
            this.model = model;
        if(pris.equals(""))
            this.pris = "0";
        else
            this.pris = pris;
        if(tidigareagare.equals(""))
            this.tidigareagare = "Tidigare ägare";
        else
            this.tidigareagare = tidigareagare;
        if(tillverkare.equals(""))
            this.tillverkare = "Tillverkare";
        else
            this.tillverkare = tillverkare;
    }
}
