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
    private String tidigareagare;

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

    public Instrument(){
        super();
    }

    public Instrument(String beskrivning, int instrumentId, String model, String pris, String tidigareagare, String tillverkare){
        if(beskrivning.equals(""))
            this.beskrivning = "Beskrivning";
        else
            this.beskrivning = beskrivning;
        this.instrumentId = instrumentId;
        if(model.equals(""))
            this.model = "Modell";
        else
            this.model = model;
        if(pris.equals(""))
            this.pris = "Inget pris satt";
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
