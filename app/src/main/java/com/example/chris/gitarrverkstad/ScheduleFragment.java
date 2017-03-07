package com.example.chris.gitarrverkstad;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
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
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by stefa_000 on 2017-02-10.
 */

public class ScheduleFragment extends Fragment {
    View currentView;
    String text;
    /*List<List<TextView>> hours2;
    List<List<Pair<TextView>>> hours;
    Calendar calender;
    List<String> days;
    ConsultationList consultationList;
    EventList eventList;
    List<TimeBlock> timeBlocks;*/
    ScheduleContainer scheduleContainer;
    boolean eventsConnected = false;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.schedule_layout, container, false);
        scheduleContainer = new ScheduleContainer(getFragmentManager());
        populateViewHours();
        setMondayDate();
        scheduleContainer.getXmlInformation();
        if(text != null){
            Toast.makeText(getActivity(), text,
                    Toast.LENGTH_LONG).show();
        }
        //getXmlInformation();
        setHasOptionsMenu(true);
        return currentView;
    }
/*
    public void afterConnection(){
        getXmlInformation(true);
    }
*/
    public void setMondayDate(){
        List<String> days = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        days.add(formatter.format(calendar.getTime()));
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
        days.add(formatter.format(calendar.getTime()));
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
        days.add(formatter.format(calendar.getTime()));
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
        days.add(formatter.format(calendar.getTime()));
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
        days.add(formatter.format(calendar.getTime()));
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        days.add(formatter.format(calendar.getTime()));
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        days.add(formatter.format(calendar.getTime()));
        scheduleContainer.setDays(days);
        scheduleContainer.setCalender(calendar);
    }

    public void populateViewHours(){
        List<List<Pair<TextView>>> hours = new ArrayList<List<Pair<TextView>>>();
        //adding map
        List<Pair<TextView>> templist = fillList("b");
        hours.add(templist);
        templist = fillList("c");
        hours.add(templist);
        templist = fillList("d");
        hours.add(templist);
        templist = fillList("e");
        hours.add(templist);
        templist = fillList("f");
        hours.add(templist);
        scheduleContainer.setHours(hours);
        /*
        List<TextView> templist = fillList("b");
        hours.add(templist);
        templist = fillList("c");
        hours.add(templist);
        templist = fillList("d");
        hours.add(templist);
        templist = fillList("e");
        hours.add(templist);
        templist = fillList("f");
        hours.add(templist);*/
    }

    public List<Pair<TextView>>  fillList(String target){
        List<Pair<TextView>>  templist = new ArrayList<Pair<TextView>>();
        for(int i = 1; i < 9; i++) {
            int id = currentView.getContext().getResources().getIdentifier(
                    "test" + target + "text" + i,
                    "id",
                    currentView.getContext().getPackageName());
            templist.add(new Pair<TextView>((TextView) currentView.findViewById(id), -1));
        }
        for(int i = 0; i < templist.size(); i++) {
            templist.get(i).getType().setText("L");
        }
        return templist;
    }
/*
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
*/
    /*
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
                for(int j = 0; j < blocksDay.size(); j++) {
                    if (blocksDay.get(j).isEvent()) {
                        int time = blocksDay.get(j).getTime();
                        hours.get(i).get(time).getType().setText("Arbete");
                        hours.get(i).get(time).setId(blocksDay.get(j).getId());
                        int duration = Integer.parseInt(blocksDay.get(j).getEvent().getDuration());
                        if (duration != 1) {
                            for (int k = 0; k < duration && (time + k) < hours.get(i).size(); k++) {

                                hours.get(i).get(time + k).getType().setText("Arbete");
                                hours.get(i).get(time + k).setId(blocksDay.get(j).getId());
                                hours.get(i).get(time + k).getType().setOnClickListener(createCustomClickListener(
                                        blocksDay.get(j).getId(),
                                        blocksDay.get(j).getType())
                                );
                            }
                        }
                    } else {
                        hours.get(i).get(blocksDay.get(j).getTime()).getType().setText("Konsult");
                        hours.get(i).get(blocksDay.get(j).getTime()).setId(Integer.parseInt(blocksDay.get(j).getConsultation().getConId()));
                    }
                    hours.get(i).get(blocksDay.get(j).getTime()).getType().setOnClickListener(createCustomClickListener(
                            blocksDay.get(j).getId(),
                            blocksDay.get(j).getType())
                    );
                }
            }
        }
    }*/
/*
    public CustomClickListener createCustomClickListener(int id, int type){
        CustomClickListener tempClicker = new CustomClickListener();
        tempClicker.setId(id);
        tempClicker.setType(type);
        return tempClicker;
    }
*/
    /*
    public void addAdditionalHoursToEvent(int duration, int time, int index, List<TimeBlock> blocksDay){
        for(int k = 0; k < duration && (time + k) < hours.get(index).size(); k++){*/
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
    /*
            hours.get(index).get(time + k).getType().setText("Arbete");
            hours.get(index).get(time + k).getType().setOnClickListener(createCustomClickListener(
                    blocksDay.get(index).getId(),
                    blocksDay.get(index).getType())
            );
        }
    }*/

   /* public void getXmlInformation(){
        getXmlInformation(false);
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
    }*/

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.add("Lägg in arbete");
        menu.add("Välj vecka");
        inflater.inflate(R.menu.empty_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(item.toString().equals("Lägg in arbete")){
            FragmentManager fragManager = getFragmentManager();
            ScheduleNewFragment frag =  new ScheduleNewFragment();
            frag.setScheduleInterface(scheduleContainer);
            fragManager.beginTransaction().replace(R.id.content_frame, frag).commit();
        } else if (item.toString().equals("Välj vecka")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_item);
            for (int i = 0; i != 53; i++) {
                arrayAdapter.add("v." + i);
            }
            builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //TODO: View the selected week
                }
            });
            builder.create();
            builder.show();
        }
        return super.onOptionsItemSelected(item);
    }
/*
    public class CustomClickListener implements View.OnClickListener{
        private int id;
        private int type;

        @Override
        public void onClick(View v) {
            FragmentManager fragmentManager = getFragmentManager();
            ScheduleEditFragment frag = new ScheduleEditFragment();
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

    }*/

}
