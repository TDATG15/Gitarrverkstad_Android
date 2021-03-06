package com.example.chris.gitarrverkstad;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by stefa_000 on 2017-03-05.
 */

public class ScheduleNewFragment extends Fragment {
    View currentView;
    private int week;
    private ScheduleInterface scheduleInterface;
    Button buttonCancel;
    Button buttonCreate;
    EditText textDesc;
    EditText textName;
    EditText textEmail;
    EditText textDuration;
    EditText textTel;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.schedule_new_item_layout, container, false);
        buttonCancel = (Button) currentView.findViewById(R.id.schedule_new_cancelb);
        buttonCreate = (Button) currentView.findViewById(R.id.schedule_new_createb);
        textDesc = (EditText) currentView.findViewById(R.id.schedule_new_desc);
        textDesc.setText("");
        textEmail = (EditText) currentView.findViewById(R.id.schedule_new_email);
        textEmail.setText("");
        textTel = (EditText) currentView.findViewById(R.id.schedule_new_tel);
        textTel.setText("");
        textName = (EditText) currentView.findViewById(R.id.schedule_new_name);
        textName.setText("");
        textDuration = (EditText) currentView.findViewById(R.id.schedule_new_duration);
        textDuration.setText("");
        registerClickListeners();
        return currentView;
    }

    public void exitLayout(){
        FragmentManager fragmentManager = getFragmentManager();
        ScheduleFragment frag = new ScheduleFragment();
        /*Calendar cal = Calendar.getInstance();
        Date date = new Date();
        cal.setTime(date);
        frag.setWeek(cal.get(Calendar.WEEK_OF_YEAR));*/
        frag.setWeek(week);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, frag);
        fragmentTransaction.commit();
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public void registerClickListeners(){
        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scheduleInterface.getXmlInformation(week);
                scheduleInterface.createEvent(
                        textDesc.getText().toString(),
                        textEmail.getText().toString(),
                        textTel.getText().toString(),
                        textName.getText().toString(),
                        textDuration.getText().toString()
                );
            }
        });
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitLayout();
            }
        });
    }


    public ScheduleInterface getScheduleInterface() {
        return scheduleInterface;
    }

    public void setScheduleInterface(ScheduleInterface scheduleInterface) {
        this.scheduleInterface = scheduleInterface;
    }

    public View getCurrentView() {
        return currentView;
    }

    public void setCurrentView(View currentView) {
        this.currentView = currentView;
    }

}
