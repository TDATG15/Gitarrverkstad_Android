package com.example.chris.gitarrverkstad;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by stefa_000 on 2017-03-03.
 */

@Root(name = "consultation", strict = false)
public class Consultation {

    @Element(name = "conDate")
    private String date;

    @Element(name = "conId")
    private String conId;

    @Element(name = "conTime")
    private String time;

    @Element(name = "custName")
    private String name;

    @Element(name = "custTel")
    private String tel;

    public Consultation(){
        super();
    }

    public Consultation(String date, String conId, String time, String name, String tel) {
        this.date = date;
        this.conId = conId;
        this.time = time;
        this.name = name;
        this.tel = tel;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getConId() {
        return conId;
    }

    public void setConId(String conId) {
        this.conId = conId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

}
