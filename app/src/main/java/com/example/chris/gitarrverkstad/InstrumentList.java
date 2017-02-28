package com.example.chris.gitarrverkstad;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by stefa_000 on 2017-02-27.
 */

@Root(name = "instruments")
public class InstrumentList {
    @ElementList(name = "instrument", required = true, inline = true)
    private List<Instrument> instruments;

    public List<Instrument> getInstruments() {
        return instruments;
    }

    public void setInstruments(List<Instrument> instruments) {
        this.instruments = instruments;
    }
}



