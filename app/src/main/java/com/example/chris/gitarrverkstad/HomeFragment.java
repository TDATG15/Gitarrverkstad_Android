package com.example.chris.gitarrverkstad;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class HomeFragment extends Fragment {
    private List<Appointment> appointments = new ArrayList<>();
    private List<Consultation> consultations = new ArrayList<>();
    private List<Event> events;
    private ListView list;
    private TextView textView;
    private ListView clickCallbackList;
    private ArrayAdapter<Appointment> adapter;
    private Date currentDate;
    final private Handler handler = new Handler();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat timeFormat = new SimpleDateFormat("HH");

    View currentView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.home_layout, container, false);
        adapter = new AppointmentListAdapter();
        currentDate = new Date();
        try {
            ConnectXml();
        } catch (Exception e) {
            e.printStackTrace();
        }
        updateTimer();
        return currentView;
    }

    private void populateList() {
        //String time = appointments.get(0).getTime().substring(11, 13);
        int currentHour = Integer.parseInt(timeFormat.format(currentDate));
        int duration = 0;
        int time = 0;
        if (!appointments.isEmpty()) {
            appointments.clear();
        }
        for (int i = 0; i != consultations.size(); i++) {
            if (consultations.get(i).getDate().contains(dateFormat.format(currentDate))) {
                time = Integer.parseInt(consultations.get(i).getTime().substring(11, 13)) + 1;
                if((time + 1) >= currentHour) {
                    appointments.add(new Appointment(
                            consultations.get(i).getDate(),
                            "Konsultation",
                            consultations.get(i).getName(),
                            (time + ":00 - " + (time + 1) + ":00")
                    ));
                }
            }
        }

        for (int i = 0; i != events.size(); i++) {
            if (events.get(i).getDate().contains(dateFormat.format(currentDate))) {
                time = Integer.parseInt(events.get(i).getTime().substring(11, 13));
                duration = Integer.parseInt(events.get(i).getDuration());
                if((time + duration) > currentHour){
                    appointments.add(new Appointment(
                            events.get(i).getDate(),
                            events.get(i).getDesc(),
                            events.get(i).getName(),
                            (time + ":00 - " + (time + duration) + ":00")
                    ));
                }
            }
        }


        /*appointments.add(new Appointment("2017-02-28", "Hitta fel på gitarr", "Kalle Karlsson", "13:00"));
        appointments.add(new Appointment("2017-03-11", "Byt strängar på gitarr", "Pelle Persson", "14:00"));
        appointments.add(new Appointment("2017-03-15", "Stämma en gitarr", "Lukas Lundqvist", "15:00"));
        appointments.add(new Appointment("2017-03-07", "Konsultation", "Anton Andersson", "16:00:00"));
        appointments.add(new Appointment("2017-03-07", "Byt strängar på gitarr", "Björn Björk", "17:00:00"));*/
    }

    private void populateListView() {
        list = (ListView) currentView.findViewById(R.id.next_appointment_list);
        list.setAdapter(adapter);
        if (appointments.size() > 0) {
            textView = (TextView) currentView.findViewById(R.id.selected_cust);
            textView.setText(appointments.get(0).getCustomer());
            textView = (TextView) currentView.findViewById(R.id.selected_desc);
            textView.setText(appointments.get(0).getDescription());
            textView = (TextView) currentView.findViewById(R.id.selected_time);
            textView.setText(appointments.get(0).getTime());
        }
        else {
            textView = (TextView) currentView.findViewById(R.id.selected_cust);
            textView.setText("Kund namn");
            textView = (TextView) currentView.findViewById(R.id.selected_desc);
            textView.setText("Beskrivning");
            textView = (TextView) currentView.findViewById(R.id.selected_time);
            textView.setText("Tid");
        }
    }

    private void registerClickCallback() {
        clickCallbackList = (ListView) currentView.findViewById(R.id.next_appointment_list);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                textView = (TextView) currentView.findViewById(R.id.selected_cust);
                textView.setText(appointments.get(position).getCustomer());
                textView = (TextView) currentView.findViewById(R.id.selected_desc);
                textView.setText(appointments.get(position).getDescription());
                textView = (TextView) currentView.findViewById(R.id.selected_time);
                textView.setText(appointments.get(position).getTime());
            }
        });

    }

    private class AppointmentListAdapter extends ArrayAdapter<Appointment> {
        private AppointmentListAdapter() {
            super(getActivity(), R.layout.item_next_appointment, appointments);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                itemView = getActivity().getLayoutInflater().inflate(R.layout.item_next_appointment, parent, false);
            }
            Appointment currentAppointment = appointments.get(position);
            TextView textView = (TextView) itemView.findViewById(R.id.listitem_custname);
            textView.setText(currentAppointment.getCustomer());
            textView = (TextView) itemView.findViewById(R.id.listitem_desc);
            textView.setText(currentAppointment.getDescription());
            textView = (TextView) itemView.findViewById(R.id.listitem_time);
            textView.setText(currentAppointment.getTime());
            return itemView;
        }
    }
    private void ConnectXml() throws Exception{
        String API_BASE_URL = "http://andersverkstad.zapto.org:8080";
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        /*Request request = new Request.Builder().url("http://andersverkstad.zapto.org:8080/ProjectEE-war/webresources/entities.event").head().build();
        System.out.println("Header: " + request.header("user-agent"));*/

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .client(new OkHttpClient())
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();
        Client consultClient = retrofit.create(Client.class);

        retrofit2.Call<ConsultationList> consultCall = consultClient.getConsultation();
        consultCall.enqueue(new Callback<ConsultationList>() {
            @Override
            public void onResponse(Call<ConsultationList> call, Response<ConsultationList> response) {
                if(response.body() != null) {
                    ConsultationList consultationList = response.body();
                    consultations = consultationList.getConsultationList();
                }

            }

            @Override
            public void onFailure(Call<ConsultationList> call, Throwable t) {

            }
        });
        Retrofit retrofit2 = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .client(new OkHttpClient())
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();
        Client eventClient = retrofit2.create(Client.class);

        retrofit2.Call<EventList> eventListCall = eventClient.getEvent();
        eventListCall.enqueue(new Callback<EventList>() {
            @Override
            public void onResponse(Call<EventList> call, Response<EventList> response) {
                if(response.body() != null) {
                    EventList eventList = response.body();
                    events = eventList.getEvents();
                    afterConnection(null);
                }
            }

            @Override
            public void onFailure(Call<EventList> call, Throwable t) {

            }
        });
    }

    private void afterConnection(String failmessage){
        if(consultations != null) {
            populateList();
            Collections.sort(appointments, new Comparator<Appointment>() {
                @Override
                public int compare(Appointment o1, Appointment o2) {
                    return o1.getTime().compareTo(o2.getTime());
                }
            });
        }else{
            //populateFailList(failmessage);
        }
        populateListView();
        registerClickCallback();
    }

   private void updateTimer() {
       handler.postDelayed( new Runnable() {

           TextView emptyListText = (TextView) currentView.findViewById(R.id.if_empty_list_textview);

           @Override
           public void run() {
               if (!appointments.isEmpty()) {
                   emptyListText.setText("");
                   String time = appointments.get(0).getTime().substring(8, 10);
                   currentDate = new Date();
                   String currentHour = timeFormat.format(currentDate);
                    System.out.println("Appointment time: " + time);
                    System.out.println("Current hour: " + currentHour);
                   if (Integer.parseInt(currentHour) > Integer.parseInt(time)) {
                       System.out.println("TRUE");
                       appointments.remove(0);
                       adapter.notifyDataSetChanged();
                   }
                   if (appointments.size() > 0) {
                       textView = (TextView) currentView.findViewById(R.id.selected_cust);
                       textView.setText(appointments.get(0).getCustomer());
                       textView = (TextView) currentView.findViewById(R.id.selected_desc);
                       textView.setText(appointments.get(0).getDescription());
                       textView = (TextView) currentView.findViewById(R.id.selected_time);
                       textView.setText(appointments.get(0).getTime());
                   }
               } else {
                   emptyListText.setText("");
                   adapter.notifyDataSetChanged();
                   System.out.println("laksnflknsf");
               }
               try {
                   ConnectXml();
               } catch (Exception e) {
                   e.printStackTrace();
               }
               handler.postDelayed(this, 60 * 1000);
           }
       }, 0);
    }
}


