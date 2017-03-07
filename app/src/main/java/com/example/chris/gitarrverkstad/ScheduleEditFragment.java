package com.example.chris.gitarrverkstad;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

/**
 * Created by stefa_000 on 2017-03-04.
 */

public class ScheduleEditFragment extends Fragment{
    View currentView;
    int selectedType;
    int selectedId;
    Consultation newSelectedConsultation;
    Event newSelectedEvent;
    Consultation selectedConsultation;
    Event selectedEvent;
    EditText editTextDesc;
    EditText editTextDuration;
    EditText editTextEmail;
    EditText editTextName;
    EditText editTextTel;
    TextView viewTextType;
    Button cancelButton;
    Button saveButton;
    Button deleteButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.schedule_edit_item_layout, container, false);
        editTextDesc = (EditText) currentView.findViewById(R.id.schedule_edit_desc);
        editTextDuration = (EditText) currentView.findViewById(R.id.schedule_edit_duration);
        editTextEmail= (EditText) currentView.findViewById(R.id.schedule_edit_email);
        editTextName = (EditText) currentView.findViewById(R.id.schedule_edit_name);
        editTextName.setText("Ingen kontakt med servern");
        editTextTel = (EditText) currentView.findViewById(R.id.schedule_edit_tel);
        viewTextType = (TextView) currentView.findViewById(R.id.schedule_edit_type);
        connectXml();
        cancelButton = (Button) currentView.findViewById(R.id.schedule_edit_cancelb);
        saveButton = (Button) currentView.findViewById(R.id.schedule_edit_saveb);
        deleteButton = (Button) currentView.findViewById(R.id.schedule_edit_deleteb);
        registerCallback();
        return currentView;
    }

    public void exitLayout(){
        FragmentManager fragmentManager = getFragmentManager();
        ScheduleFragment scheduleFragment  =new ScheduleFragment();
        Calendar cal = Calendar.getInstance();
        Date date = new Date();
        cal.setTime(date);
        scheduleFragment.setWeek(cal.get(Calendar.WEEK_OF_YEAR));
        fragmentManager.beginTransaction().replace(R.id.content_frame, scheduleFragment).commit();
    }

    public void registerCallback(){
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //public Consultation(String date, String conId, String time, String name, String tel) {
                String API_BASE_URL = "http://andersverkstad.zapto.org:8080";
                OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(API_BASE_URL)
                        .client(new OkHttpClient())
                        .addConverterFactory(SimpleXmlConverterFactory.create())
                        .build();
                Client client = retrofit.create(Client.class);
                if (selectedType == 2) {

                    Event event = new Event(
                            newSelectedEvent.getDate(),
                            newSelectedEvent.getTime(),
                            editTextEmail.getText().toString(),
                            editTextName.getText().toString(),
                            editTextTel.getText().toString(),
                            editTextDesc.getText().toString(),
                            newSelectedEvent.getDuration(),
                            selectedId
                    );
                    retrofit2.Call<Event> call = client.putEvent(event, Integer.toString(selectedId));
                    call.enqueue(new Callback<Event>() {

                        @Override
                        public void onResponse(Call<Event> call, Response<Event> response) {
                            exitLayout();
                        }

                        @Override
                        public void onFailure(Call<Event> call, Throwable t) {
                            exitLayout();
                        }

                    });
                } else if (selectedType == 1){

                    //public Consultation(String date, String conId, String time, String name, String tel) {
                    Consultation consultation = new Consultation(
                            newSelectedConsultation.getDate(),
                            Integer.toString(selectedId),
                            newSelectedConsultation.getTime(),
                            editTextName.getText().toString(),
                            editTextTel.getText().toString()
                    );
                    retrofit2.Call<Consultation> call = client.putConsultation(selectedConsultation, selectedConsultation.getConId());
                    call.enqueue(new Callback<Consultation>() {

                        @Override
                        public void onResponse(Call<Consultation> call, Response<Consultation> response) {
                            exitLayout();
                        }

                        @Override
                        public void onFailure(Call<Consultation> call, Throwable t) {
                            exitLayout();
                        }

                    });
                }
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String API_BASE_URL = "http://andersverkstad.zapto.org:8080";
                OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(API_BASE_URL)
                        .client(new OkHttpClient())
                        .addConverterFactory(SimpleXmlConverterFactory.create())
                        .build();
                Client client = retrofit.create(Client.class);
                if (selectedType == 2) {
                    retrofit2.Call<Event> call = client.deleteEvent(Integer.toString(selectedId));
                    call.enqueue(new Callback<Event>() {

                        @Override
                        public void onResponse(Call<Event> call, Response<Event> response) {
                            exitLayout();
                        }

                        @Override
                        public void onFailure(Call<Event> call, Throwable t) {
                            exitLayout();
                        }

                    });
                } else if (selectedType == 1){
                    retrofit2.Call<Consultation> call = client.deleteConsultation(Integer.toString(selectedId));
                    call.enqueue(new Callback<Consultation>() {

                        @Override
                        public void onResponse(Call<Consultation> call, Response<Consultation> response) {
                            exitLayout();
                        }

                        @Override
                        public void onFailure(Call<Consultation> call, Throwable t) {
                            exitLayout();
                        }

                    });
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitLayout();
            }
        });
    }

    public void afterConnection(Consultation consultation){
        newSelectedConsultation = consultation;
        editTextTel.setText(consultation.getTel());
        editTextDuration.setText("1 timme");
        editTextEmail.setText("");
        editTextDesc.setText("En konsultation med " + consultation.getName());
        editTextName.setText(consultation.getName());
        viewTextType.setText("Konsultation kl " + newSelectedConsultation.getTime().substring(11, 13) + ":00");
    }

    public void afterConnection(Event event){
        newSelectedEvent = event;
        editTextTel.setText(event.getTel());
        editTextDuration.setText(event.getDuration() + " timmar" );
        editTextEmail.setText(event.getEmail());
        editTextDesc.setText(event.getDesc());
        editTextName.setText(event.getName());
        viewTextType.setText("Arbete kl " + newSelectedEvent.getTime().substring(11, 13) + ":00");

    }

    public void connectXml() {
        String API_BASE_URL = "http://andersverkstad.zapto.org:8080";
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .client(new OkHttpClient())
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();
        Client client = retrofit.create(Client.class);
        if (selectedType == 1) {
            retrofit2.Call<Consultation> call = client.getConsultationOfIndex(Integer.toString(selectedId));
            call.enqueue(new Callback<Consultation>() {

                @Override
                public void onResponse(Call<Consultation> call, Response<Consultation> response) {
                    Consultation consultation = response.body();
                    afterConnection(consultation);
                }

                @Override
                public void onFailure(Call<Consultation> call, Throwable t) {
                    Consultation consultation = null;
                    afterConnection(consultation);
                }

            });
        } else if (selectedType == 2){
            retrofit2.Call<Event> call = client.getEventOfIndex(Integer.toString(selectedId));
            call.enqueue(new Callback<Event>() {

                @Override
                public void onResponse(Call<Event> call, Response<Event> response) {
                    Event event = response.body();
                    afterConnection(event);
                }

                @Override
                public void onFailure(Call<Event> call, Throwable t) {
                    Event event  = null;
                    afterConnection(event);
                }

            });
        }
    }

    public View getCurrentView() {
        return currentView;
    }

    public void setCurrentView(View currentView) {
        this.currentView = currentView;
    }

    public int getSelectedType() {
        return selectedType;
    }

    public void setSelectedType(int selectedType) {
        this.selectedType = selectedType;
    }

    public int getSelectedId() {
        return selectedId;
    }

    public void setSelectedId(int selectedId) {
        this.selectedId = selectedId;
    }

    public Consultation getSelectedConsultation() {
        return selectedConsultation;
    }

    public void setSelectedConsultation(Consultation selectedConsultation) {
        this.selectedConsultation = selectedConsultation;
    }

    public Event getSelectedEvent() {
        return selectedEvent;
    }

    public void setSelectedEvent(Event selectedEvent) {
        this.selectedEvent = selectedEvent;
    }
}
