package com.example.chris.gitarrverkstad;

import android.app.FragmentManager;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

/**
 * Created by stefa_000 on 2017-03-05.
 */

public interface ScheduleInterface {
    public void createEvent(String desc, String email, String tel, String name, String duration);
    public void getXmlInformation();
    public List<List<Pair<TextView>>> getHours();
    public void setHours(List<List<Pair<TextView>>> hours);
    public Calendar getCalender();
    public void setCalender(Calendar calender);
    public List<String> getDays();
    public void setDays(List<String> days);
    public ConsultationList getConsultationList();
    public void setConsultationList(ConsultationList consultationList);
    public EventList getEventList();
    public void setEventList(EventList eventList);
    public List<TimeBlock> getTimeBlocks();
    public void setTimeBlocks(List<TimeBlock> timeBlocks);
    public FragmentManager getMyFragmentManager();
    public void setFragmentManager(FragmentManager fragmentManager);
}
