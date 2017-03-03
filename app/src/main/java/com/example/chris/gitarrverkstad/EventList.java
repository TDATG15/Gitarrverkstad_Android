package com.example.chris.gitarrverkstad;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by stefa_000 on 2017-03-03.
 */

@Root(name = "events")
public class EventList {
    @ElementList(name = "event", required = true, inline = true)
    private List<Event> events;

    public EventList(){
        super();
    }

    public EventList(List<Event> events){
        this.events = events;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }
}
