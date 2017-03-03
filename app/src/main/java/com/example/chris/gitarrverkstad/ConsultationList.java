package com.example.chris.gitarrverkstad;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by stefa_000 on 2017-03-03.
 */

@Root(name = "consultations")
public class ConsultationList {
    @ElementList(name = "consultation", required = true, inline = true)
    private List<Consultation> consultationList;

    public ConsultationList(){
        super();
    }

    public List<Consultation> getConsultationList() {
        return consultationList;
    }

    public void setConsultationList(List<Consultation> consultationList) {
        this.consultationList = consultationList;
    }
}
