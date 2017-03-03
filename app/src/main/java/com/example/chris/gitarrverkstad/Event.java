package com.example.chris.gitarrverkstad;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by stefa_000 on 2017-03-03.
 */

@Root(name = "event", strict = false)
public class Event {
    @Element(name = "arrDate")
    private String date;

    @Element(name = "arrTid")
    private String time;

    @Element(name = "custMail")
    private String email;

    @Element(name = "custName")
    private String name;

    @Element(name = "custTel")
    private String tel;

    @Element(name = "descr")
    private String desc;

    @Element(name = "duration")
    private String duration;

    @Element(name = "eventId")
    private int eventId;

    public Event(){
        super();
    }

    public Event(String date, String time, String email, String name, String tel, String desc, String duration, int eventId) {
        this.date = date;
        this.time = time;
        this.email = email;
        this.name = name;
        this.tel = tel;
        this.desc = desc;
        this.duration = duration;
        this.eventId = eventId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }
}
