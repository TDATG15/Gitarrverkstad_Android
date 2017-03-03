package com.example.chris.gitarrverkstad;

import android.app.Fragment;
import android.app.Service;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

/**
 * Created by stefa_000 on 2017-02-10.
 */

public class ScheduleFragment extends Fragment {
    View currentView;
    List<List<TextView>> hours;
    Calendar calender;
    List<String> days;
    ConsultationList consultationList;
    EventList eventList;
    List<TimeBlock> timeBlocks;
    boolean eventsConnected = false;
    /*List<TextView> viewHoursMon;
    List<TextView> viewHoursTue;
    List<TextView> viewHoursWen;
    List<TextView> viewHoursThu;
    List<TextView> viewHoursFri;*/
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.schedule_layout, container, false);
        populateViewHours();
        setMondayDate();
        getXmlInformation(false);
        setHasOptionsMenu(true);
        return currentView;
    }

    public void afterConnection(){
        getXmlInformation(true);
    }

    public void setMondayDate(){
        days = new ArrayList<String>();
        calender = Calendar.getInstance();
        calender.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        days.add(formatter.format(calender.getTime()));
        calender.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
        days.add(formatter.format(calender.getTime()));
        calender.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
        days.add(formatter.format(calender.getTime()));
        calender.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
        days.add(formatter.format(calender.getTime()));
        calender.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
        days.add(formatter.format(calender.getTime()));
        calender.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        days.add(formatter.format(calender.getTime()));
        calender.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        days.add(formatter.format(calender.getTime()));
    }

    public void populateViewHours(){
        hours = new ArrayList<List<TextView>>();
        List<TextView> templist = fillList("b");
        hours.add(templist);
        templist = fillList("c");
        hours.add(templist);
        templist = fillList("d");
        hours.add(templist);
        templist = fillList("e");
        hours.add(templist);
        templist = fillList("f");
        hours.add(templist);
    }

    public List<TextView> fillList(String target){
        List<TextView> templist = new ArrayList<TextView>();
        for(int i = 1; i < 9; i++) {
            int id = currentView.getContext().getResources().getIdentifier(
                    "test" + target + "text" + i,
                    "id",
                    currentView.getContext().getPackageName());
            templist.add((TextView) currentView.findViewById(id));
        }
        for(int i = 0; i < templist.size(); i++) {
            templist.get(i).setText("L");
        }
        return templist;
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

    public void fillHoursWithBlocks(){
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
                }
            }
        }
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
                    hours.get(0).get(0).setText(t.getMessage());
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
                    hours.get(0).get(0).setText(t.getMessage());
                }

            });
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        for (int i = 1; i != 53; i++) {
            menu.add("Vecka " + i);
        }
        inflater.inflate(R.menu.empty_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //TODO: open selected week schedule

        return super.onOptionsItemSelected(item);
    }

    public class TimeBlock{
        private String day;
        private int time;
        private int type;
        private Event event;
        private Consultation consultation;

        public TimeBlock(String day, int time) {
            this.day = day;
            this.time = time;
        }

        public TimeBlock(String day, int time, Consultation consultation) {
            type = 1;
            this.day = day;
            this.time = time;
            this.consultation = consultation;
        }

        public TimeBlock(String day, int time, Event event) {
            type = 2;
            this.day = day;
            this.time = time;
            this.event = event;
        }

        public Consultation getConsultation() {
            return consultation;
        }

        public void setConsultation(Consultation consultation) {
            this.consultation = consultation;
        }

        public Event getEvent() {
            return event;
        }

        public void setEvent(Event event) {
            this.event = event;
        }

        public boolean isEvent() {
            return type == 2;
        }

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }

        public String getDay() {
            return day;
        }

        public void setDay(String day) {
            this.day = day;
        }
    }
}
