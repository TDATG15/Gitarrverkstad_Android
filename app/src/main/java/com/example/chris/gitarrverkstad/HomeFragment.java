package com.example.chris.gitarrverkstad;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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
    private ArrayAdapter<Appointment> adapter;
    private Date currentDate = new Date();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat timeFormat = new SimpleDateFormat("HH");

    View currentView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.home_layout, container, false);
        try {
            ConnectXml();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return currentView;
    }

    private void populateList() {
        for (int i = 0; i != consultations.size(); i++) {
            if (consultations.get(i).getDate().contains(dateFormat.format(currentDate))) {
                appointments.add(new Appointment(
                        consultations.get(i).getDate(), "Konsultation", consultations.get(i).getName(), consultations.get(i).getTime()
                ));
            }
        }

        for (int i = 0; i != events.size(); i++) {
            if (events.get(i).getDate().contains(dateFormat.format(currentDate))) {
                appointments.add(new Appointment(
                        events.get(i).getDate(), events.get(i).getDesc(), events.get(i).getName(), events.get(i).getTime()
                ));
            }
        }


        /*appointments.add(new Appointment("2017-02-28", "Hitta fel på gitarr", "Kalle Karlsson", "13:00"));
        appointments.add(new Appointment("2017-03-11", "Byt strängar på gitarr", "Pelle Persson", "14:00"));
        appointments.add(new Appointment("2017-03-15", "Stämma en gitarr", "Lukas Lundqvist", "15:00"));
        appointments.add(new Appointment("2017-02-28", "Konsultation", "Anton Andersson", "16:00"));
        appointments.add(new Appointment("2017-03-11", "Byt strängar på gitarr", "Björn Björk", "17:00"));*/
    }

    private void populateListView() {
        adapter = new AppointmentListAdapter();
        list = (ListView) currentView.findViewById(R.id.next_appointment_list);
        list.setAdapter(adapter);
        if (appointments.size() > 0) {
            TextView textView = (TextView) currentView.findViewById(R.id.selected_cust);
            textView.setText(appointments.get(0).getCustomer());
            textView = (TextView) currentView.findViewById(R.id.selected_desc);
            textView.setText(appointments.get(0).getDescription());
            textView = (TextView) currentView.findViewById(R.id.selected_time);
            textView.setText(appointments.get(0).getTime());
        }
    }

    private void registerClickCallback() {
        ListView list = (ListView) currentView.findViewById(R.id.next_appointment_list);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                TextView textView = (TextView) currentView.findViewById(R.id.selected_cust);
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
                ConsultationList consultationList = response.body();
                consultations = consultationList.getConsultationList();

            }

            @Override
            public void onFailure(Call<ConsultationList> call, Throwable t) {

            }
        });
        OkHttpClient.Builder httpClient2 = new OkHttpClient.Builder();
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
                EventList eventList = response.body();
                events = eventList.getEvents();
                afterConnection(null);
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
        Timer timer = new Timer ();
        TimerTask minuteTask = new TimerTask () {
            @Override
            public void run () {
                if (!appointments.isEmpty()) {
                    String time = appointments.get(0).getTime().substring(11, 13);
                    String currentHour = timeFormat.format(currentDate);
                    System.out.println(time);
                    System.out.println(currentHour);
                    if (Integer.parseInt(currentHour) > Integer.parseInt(time)) {
                        System.out.println("TRUE");
                        appointments.remove(0);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        };
        // schedule the task to run starting now and then every hour...
        timer.schedule (minuteTask, 0l, 1000*60);
    }
}


