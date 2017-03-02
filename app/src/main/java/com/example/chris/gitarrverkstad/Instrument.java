package com.example.chris.gitarrverkstad;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by stefa_000 on 2017-02-28.
 */

@Root(name="instrument", strict = false)
public class Instrument {

    @Element(name = "beskrivning")
    private String beskrivning;

    @Element(name = "instrumentId")
    private int instrumentId;

    @Element(name = "model")
    private String model;

    @Element(name = "pris")
    private String pris;

    @Element(name="tidigareagare")
    private String tidigareAgare;

    @Element(name = "tillverkare")
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
        return tidigareAgare;
    }

    public void setTidigareAgare(String tidigareAgare) {
        this.tidigareAgare = tidigareAgare;
    }

    public String getTillverkare() {
        return tillverkare;
    }

    public void setTillverkare(String tillverkare) {
        this.tillverkare = tillverkare;
    }

    public Instrument(){
        super();
    }

    public Instrument(String beskrivning, int instrumentId, String model, String pris, String tidigareAgare, String tillverkare){
        this.beskrivning = beskrivning;
        this.instrumentId = instrumentId;
        this.model = model;
        this.pris = pris;
        this.tidigareAgare = tidigareAgare;
        this.tillverkare = tillverkare;
    }
}
