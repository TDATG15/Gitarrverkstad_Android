package com.example.chris.gitarrverkstad;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by chris on 2017-03-02.
 */

@Root(name = "Boknings")
public class Bookings {

    @ElementList(name = "bokning")
    List<Bokning> bookings;

    @Root(name = "bokning")
    private class Bokning {

        @ElementList(name = "AId")
        List<AId> arandeId;

        @Root(name = "AId")
        private class AId {

            @Element(name = "arendeId")
            private int arendeId;

            @Element(name = "jobbArende")
            private String jobbArende;

            @ElementList(name = "KId")
            List<KId> customedID;

            @Root(name = "KId")
            private class KId {

                @Element(name = "adress")
                private String address;

                @Element(name = "efternamn")
                private String lastName;

                @Element(name = "fornamn")
                private String firstName;

                @Element(name = "kundId")
                private int customerID;

                @Element(name = "phone")
                private int phoneNumber;
            }
        }

        @ElementList(name = "bokningPK")
        List<BokningPK> bokningPK;

        @Root(name = "bokningPK")
        public class BokningPK {

            @Element(name = "datum")
            private String date;

            @Element(name = "TId")
            private int timeId;
        }
    }
}


