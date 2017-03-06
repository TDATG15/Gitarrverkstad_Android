package com.example.chris.gitarrverkstad;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

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
    Consultation selectedConsultation;
    Event selectedEvent;
    EditText editTextDesc;
    EditText editTextDuration;
    EditText editTextEmail;
    EditText editTextName;
    EditText editTextTel;
    TextView viewTextType;


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
        return currentView;
    }

    public void afterConnection(Consultation consultation){
        editTextTel.setText(consultation.getTel());
        editTextDuration.setText("1 timme");
        editTextEmail.setText("");
        editTextDesc.setText("En konsultation med " + consultation.getName());
        editTextName.setText(consultation.getName());
        viewTextType.setText("Konsultation");
    }

    public void afterConnection(Event event){
        editTextTel.setText(event.getTel());
        editTextDuration.setText(event.getDuration() + " timmar" );
        editTextEmail.setText(event.getEmail());
        editTextDesc.setText(event.getDesc());
        editTextName.setText(event.getName());
        viewTextType.setText("Arbete");

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
