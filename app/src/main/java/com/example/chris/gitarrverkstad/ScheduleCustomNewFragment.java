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

import java.util.Date;

/**
 * Created by stefa_000 on 2017-03-07.
 */

public class ScheduleCustomNewFragment extends Fragment {
    View currentView;
    private ScheduleInterface scheduleInterface;
    private int index;
    private int week;
    private Date date;
    Button buttonCancel;
    Button buttonCreate;
    EditText textDesc;
    EditText textName;
    EditText textEmail;
    EditText textTel;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.schedule_customnew_item_layout, container, false);
        buttonCancel = (Button) currentView.findViewById(R.id.schedule_newcustom_cancelb);
        buttonCreate = (Button) currentView.findViewById(R.id.schedule_newcustom_createb);
        textDesc = (EditText) currentView.findViewById(R.id.schedule_newcustom_desc);
        textDesc.setText("");
        textEmail = (EditText) currentView.findViewById(R.id.schedule_newcustom_email);
        textEmail.setText("");
        textTel = (EditText) currentView.findViewById(R.id.schedule_newcustom_tel);
        textTel.setText("");
        textName = (EditText) currentView.findViewById(R.id.schedule_newcustom_name);
        textName.setText("");
        registerOnClickCallback();
        return currentView;
    }

    public void ExitLayout(){
        ScheduleFragment frag = new ScheduleFragment();
        frag.setWeek(week);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, frag).commit();
    }

    public void registerOnClickCallback(){
        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scheduleInterface.getXmlInformation(week);
                scheduleInterface.createEventTimeDate(
                        index,
                        date,
                        textDesc.getText().toString(),
                        textEmail.getText().toString(),
                        textTel.getText().toString(),
                        textName.getText().toString()
                        );
            }
        });
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExitLayout();
            }
        });
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setIndex(int index){
        this.index = index;
    }

    public ScheduleInterface getScheduleInterface() {
        return scheduleInterface;
    }

    public void setScheduleInterface(ScheduleInterface scheduleInterface) {
        this.scheduleInterface = scheduleInterface;
    }
}
