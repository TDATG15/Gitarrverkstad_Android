package com.example.chris.gitarrverkstad;

/**
 * Created by stefa_000 on 2017-02-12.
 */

public class Appointment {
    String customer;
    String description;
    String date;
    String time;
    public Appointment(String date, String description, String customer, String time){
        this.customer = customer;
        this.date = date;
        this.description = description;
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
