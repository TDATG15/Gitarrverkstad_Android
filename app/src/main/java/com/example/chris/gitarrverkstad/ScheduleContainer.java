package com.example.chris.gitarrverkstad;

import android.app.FragmentManager;
import android.view.View;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

/**
 * Created by stefa_000 on 2017-03-05.
 */

public class ScheduleContainer implements ScheduleInterface {
    List<List<Pair<TextView>>> hours;
    Calendar calender;
    List<String> days;
    ConsultationList consultationList;
    EventList eventList;
    List<TimeBlock> timeBlocks;
    FragmentManager fragmentManager;

    String availableTime;
    String availableDate;
    int newduration;
    int time;
    boolean done = false;


    public ScheduleContainer(FragmentManager fragmentManager){
        this.fragmentManager = fragmentManager;
    }

    public void afterConnection(){
        getXmlInformation(true);
    }

    public void getXmlInformation(boolean getevents){
        String API_BASE_URL = "http://andersverkstad.zapto.org:8080";
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .client(new OkHttpClient())
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();
        Client client = retrofit.create(Client.class);
        if(!getevents) {
            retrofit2.Call<ConsultationList> call = client.getConsultation();
            call.enqueue(new Callback<ConsultationList>() {

                @Override
                public void onResponse(Call<ConsultationList> call, Response<ConsultationList> response) {
                    consultationList = response.body();
                    afterConnection();
                }

                @Override
                public void onFailure(Call<ConsultationList> call, Throwable t) {
                    hours.get(0).get(0).getType().setText(t.getMessage());
                }

            });
        } else {
            retrofit2.Call<EventList> call2 = client.getEvent();
            call2.enqueue(new Callback<EventList>() {

                @Override
                public void onResponse(Call<EventList> call, Response<EventList> response) {
                    eventList = response.body();
                    makeTimeBlocks();
                    fillHoursWithBlocks();
                }
                @Override
                public void onFailure(Call<EventList> call, Throwable t) {
                    hours.get(0).get(0).getType().setText(t.getMessage());
                }

            });
        }
    }

    public void makeTimeBlocks(){
        timeBlocks = new ArrayList<TimeBlock>();
        StringBuilder stringBuilder = new StringBuilder("");
        String tempDate;
        int time;
        for(int i = 0; i < consultationList.getConsultationList().size(); i++){
            stringBuilder = new StringBuilder("");
            stringBuilder.append(consultationList.getConsultationList().get(i).getDate(), 0, 10);
            tempDate = stringBuilder.toString();
            stringBuilder = new StringBuilder("");
            stringBuilder.append(consultationList.getConsultationList().get(i).getTime(), 11, 13);
            time = Integer.parseInt(stringBuilder.toString()) - 10;
            timeBlocks.add(new TimeBlock(tempDate, time, consultationList.getConsultationList().get(i)));
        }
        for(int i = 0; i < eventList.getEvents().size(); i++){
            stringBuilder = new StringBuilder("");
            stringBuilder.append(eventList.getEvents().get(i).getDate(), 0, 10);
            tempDate = stringBuilder.toString();
            stringBuilder = new StringBuilder("");
            stringBuilder.append(eventList.getEvents().get(i).getTime(), 11, 13);
            time = Integer.parseInt(stringBuilder.toString()) - 10;
            timeBlocks.add(new TimeBlock(tempDate, time, eventList.getEvents().get(i)));
        }
    }

    public void setScheduleIfExists(String text, TextView textView, Boolean exists){
        if(textView != null){
            textView.setText(text);
        }
    }

    public void setClickListenerIfExists(TimeBlock timeBlock, TextView textView, Boolean exists){
        if(textView != null){
            textView.setOnClickListener(createCustomClickListener(
                    timeBlock.getId(),
                    timeBlock.getType())
            );
        }
    }

    public void fillHoursWithBlocks(/*Boolean exists*/){
        Boolean exists = false;
        List<TimeBlock> currentBlocks = timeBlocks;
        List<TimeBlock> blocksDay  = new ArrayList<TimeBlock>();
        for(int i = 0; i < hours.size(); i++){
            blocksDay = new ArrayList<TimeBlock>();
            for(int j = 0; j < currentBlocks.size(); j++){
                if(currentBlocks.get(j).getDay().equals(days.get(i))){
                    blocksDay.add(currentBlocks.get(j));
                }
            }
            if(blocksDay.size() != 0) {
                for(int j = 0; j < blocksDay.size(); j++){
                    if (blocksDay.get(j).isEvent()) {
                        int time = blocksDay.get(j).getTime();
                        //hours.get(i).get(time).getType().setText("Arbete");
                        setScheduleIfExists("Nytt\nArbete", hours.get(i).get(time).getType(), exists);
                        hours.get(i).get(time).setId(blocksDay.get(j).getId());
                        int duration = Integer.parseInt(blocksDay.get(j).getEvent().getDuration());
                        if(duration != 1){
                            for(int k = 0; k < duration && (time + k) < hours.get(i).size(); k++){
                                /*if(time + k >= hours.get(index).size()){
                                 for(int m = 1; index + m < hours.size(); m++){
                                    for(int n = 0; n < hours.get(index + m).size(); n++){
                                        hours.get(index + m).get(n).setText("Arbete");
                                        hours.get(index + m).get(n).setOnClickListener(createCustomClickListener(
                                         blocksDay.get(index).getId(),
                                        blocksDay.get(index).getType())
                                     );
                                    }
                                    }
                                }*/
                                //hours.get(i).get(time + k).getType().setText("Arbete");
                                if(k == 0){
                                    setScheduleIfExists("Nytt\nArbete", hours.get(i).get(time + k).getType(), exists);
                                }
                                else {
                                    setScheduleIfExists("Forts\nArbete", hours.get(i).get(time + k).getType(), exists);
                                }
                                hours.get(i).get(time + k).setId(blocksDay.get(j).getId());
                                setClickListenerIfExists(blocksDay.get(j), hours.get(i).get(time + k).getType(), exists);
                                /*hours.get(i).get(time + k).getType().setOnClickListener(createCustomClickListener(
                                        blocksDay.get(j).getId(),
                                        blocksDay.get(j).getType())
                                );*/
                            }
                            //addAdditionalHoursToEvent(duration, time, i, blocksDay);
                        }
                    }
                    else {
                        setScheduleIfExists("Konsult", hours.get(i).get(blocksDay.get(j).getTime()).getType(), exists);
                       // hours.get(i).get(blocksDay.get(j).getTime()).getType().setText("Konsult");
                        hours.get(i).get(blocksDay.get(j).getTime()).setId(Integer.parseInt(blocksDay.get(j).getConsultation().getConId()));
                    }
                    setClickListenerIfExists(blocksDay.get(j), hours.get(i).get(blocksDay.get(j).getTime()).getType(), exists);
                    /*hours.get(i).get(blocksDay.get(j).getTime()).getType().setOnClickListener(createCustomClickListener(
                            blocksDay.get(j).getId(),
                            blocksDay.get(j).getType())
                    );*/
                }/*
                for (int j = 0; j < hours.get(i).size(); j++) {
                    for (int m = 0; m < blocksDay.size(); m++) {
                        if (blocksDay.get(m).getTime() == j) {
                            if (blocksDay.get(m).isEvent()) {
                                hours.get(i).get(j).setText("Arbete");
                            } else {
                                hours.get(i).get(j).setText("Konsult");
                            }
                        }
                    }
                }*/
            }
        }
    }


    public CustomClickListener createCustomClickListener(int id, int type){
        CustomClickListener tempClicker = new CustomClickListener();
        tempClicker.setId(id);
        tempClicker.setType(type);
        return tempClicker;
    }

    public void exitLayout(String text){
        ScheduleFragment frag =  new ScheduleFragment();
        Calendar cal = Calendar.getInstance();
        Date date = new Date();
        cal.setTime(date);
        frag.setWeek(cal.get(Calendar.WEEK_OF_YEAR));
        frag.text = text;
        fragmentManager.beginTransaction().replace(R.id.content_frame, frag).commit();
    }

    public void postEvent(Event event){
        String API_BASE_URL = "http://andersverkstad.zapto.org:8080";
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .client(new OkHttpClient())
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();
        Client client = retrofit.create(Client.class);
            retrofit2.Call<Event> call = client.postEvent(event);
            call.enqueue(new Callback<Event>() {

                @Override
                public void onResponse(Call<Event> call, Response<Event> response) {
                    exitLayout(null);
                }

                @Override
                public void onFailure(Call<Event> call, Throwable t) {
                    exitLayout(t.getMessage());
                }

            });
    }

    public void writeDateTimeToString(Date date){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        time = time + 10;
        String string = Integer.toString(time);
        if(string.length() > 2 || string.length() <= 0){
            string = "00";
        }
        if(string.length() == 1){
            string = "0" + time;
        }
        availableTime = "1970-01-01T" + string + ":00:00+01:00";
        availableDate = dateFormat.format(date) + "T00:00:00+01:00";
        done = true;
    }

    public Date getAndAddToDate(Date date){
        Date dt = date;
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.add(Calendar.DATE, 1);
        while(c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY ){
            c.add(Calendar.DATE, 1);
        }
        return c.getTime();
    }

    public void findAvailableTime(List<TimeBlock> times, Date date){
        if( 0 > time ){
            time = 0;
        }
        if(time > 7){
            time = 0;
            findAvailableDate(getAndAddToDate(date));
        } else {
            List<String> strings = new ArrayList<String>();
            for (int i = 0; i < 8; i++) {
                strings.add("A");
            }
            for (int i = 0; i < times.size(); i++) {
                int temptime = times.get(i).getTime();
                int duration = Integer.parseInt(times.get(i).getEvent().getDuration());
                for (int j = 0; j < duration; j++) {
                    if (temptime + j < 8) {
                        strings.set(temptime + j, "T");
                    }
                }
            }
            boolean finished = true;
            for (; time < strings.size(); time++) {
                if (strings.get(time).equals("A")) {
                    int i;
                    for (i = 0; i < newduration && !done; i++) {
                        if (time + i >= 8) {
                            time = 0;
                            findAvailableDate(getAndAddToDate(date));
                        }
                        else {
                            if (strings.get(time + i).equals("T")) {
                                finished = false;
                                break;
                            }
                        }
                    }

                    if(i == newduration){
                        finished = true;
                    }

                    if (finished) {
                        //time = j;
                        break;
                    }
                    //findAvailableDate(dt, 0);
                }
            }
            if (!finished || time == strings.size()) {
                time = 0;
                findAvailableDate(getAndAddToDate(date));
            }


            /*
            int duration = 0;
            int timesgeti = times.get(i).getTime();
            if(times.get(i).isEvent()){
                duration = Integer.parseInt(times.get(i).getEvent().getDuration());
            } else {
                duration = 1;
            }
            int temptime = timesgeti + duration;
            if(temptime > 7){
                Date dt = new Date();
                Calendar c = Calendar.getInstance();
                c.setTime(dt);
                c.add(Calendar.DATE, 1);
                dt = c.getTime();
                findAvailableDate(dt, 0);
            }
            if(times.get(i).getTime() == time){
                ++time;
                i = 0;
            }
            }
            */
        }
    }

    public void findAvailableDate(Date nowDate){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat timeFormat = new SimpleDateFormat("HH");
        String now = dateFormat.format(nowDate);
        boolean dateAvailable = false;
        List<TimeBlock> times = new ArrayList<TimeBlock>();
        for(int i = 0; i < timeBlocks.size(); i++){
            if(timeBlocks.get(i).getDay().equals(now)){
                times.add(timeBlocks.get(i));
            }/*
            if(i == timeBlocks.size() - 1){
                dateAvailable = true;
            }*/
        }
        if(times.size() != 0) {
            findAvailableTime(times, nowDate);
        }
        if(!done) {
            writeDateTimeToString(nowDate);
        }
    }


    @Override
    public void createEventTimeDate(int time, Date date, String desc, String email, String tel, String name){
        this.time = time;
        int id = -1;
        if(eventList.getEvents().size() != 0) {
            for (int i = 0; i < eventList.getEvents().size(); i++) {
                if (id < eventList.getEvents().get(i).getEventId()) {
                    id = eventList.getEvents().get(i).getEventId();
                }
            }
            id = id + 1;
        } else {
            id = 1000;
        }
        writeDateTimeToString(date);
        Event event = new Event(availableDate, availableTime,  email, name, tel, desc, "1", id);
        postEvent(event);
    }

    @Override
    public void createEvent(String desc, String email, String tel, String name, String duration){
        if(!duration.equals("1") && !duration.equals("2") && !duration.equals("3") && !duration.equals("4") && !duration.equals("5")  ){
            duration = "1";
        }
        int id = -1;
        if(eventList.getEvents().size() != 0) {
            for (int i = 0; i < eventList.getEvents().size(); i++) {
                if (id < eventList.getEvents().get(i).getEventId()) {
                    id = eventList.getEvents().get(i).getEventId();
                }
            }
            id = id + 1;
        } else {
            id = 1000;
        }
        DateFormat timeFormat = new SimpleDateFormat("HH");
        Date now = new Date();
        time = Integer.parseInt(timeFormat.format(now)) + 1;
        availableTime = "1970-01-01T10:00+01:00";
        newduration = Integer.parseInt(duration);
        time = time - 10;
        findAvailableDate(now);
        Event event = new Event(availableDate, availableTime,  email, name, tel, desc, duration, id);
        //Event event = new Event("2017-03-08T10:00+01:00", "1970-01-01T15:00+01:00",  email, name, tel, desc, duration, 1003);
        postEvent(event);
    }

    @Override
    public void getXmlInformation(){
        getXmlInformation(false);
    }

    @Override
    public List<List<Pair<TextView>>> getHours() {
        return hours;
    }

    @Override
    public void setHours(List<List<Pair<TextView>>> hours) {
        this.hours = hours;
    }

    @Override
    public Calendar getCalender() {
        return calender;
    }

    @Override
    public void setCalender(Calendar calender) {
        this.calender = calender;
    }

    @Override
    public List<String> getDays() {
        return days;
    }

    @Override
    public void setDays(List<String> days) {
        this.days = days;
    }

    @Override
    public ConsultationList getConsultationList() {
        return consultationList;
    }

    @Override
    public void setConsultationList(ConsultationList consultationList) {
        this.consultationList = consultationList;
    }

    @Override
    public EventList getEventList() {
        return eventList;
    }

    @Override
    public void setEventList(EventList eventList) {
        this.eventList = eventList;
    }

    @Override
    public List<TimeBlock> getTimeBlocks() {
        return timeBlocks;
    }

    @Override
    public void setTimeBlocks(List<TimeBlock> timeBlocks) {
        this.timeBlocks = timeBlocks;
    }

    @Override
    public FragmentManager getMyFragmentManager() {
        return fragmentManager;
    }

    @Override
    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public class CustomClickListener implements View.OnClickListener{
        private int id;
        private int type;


        @Override
        public void onClick(View v) {
            FragmentManager fragmentManager = getMyFragmentManager();
            ScheduleEditFragment frag =  new ScheduleEditFragment();
            frag.setSelectedId(id);
            frag.setSelectedType(type);
            fragmentManager.beginTransaction().replace(R.id.content_frame, frag).commit();
        }



        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

    }
}
