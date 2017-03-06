package com.example.chris.gitarrverkstad;

/**
 * Created by stefa_000 on 2017-03-05.
 */

public class TimeBlock {

        private String day;
        private int time;
        private int type;
        private Event event;
        private Consultation consultation;

        public TimeBlock(String day, int time) {
            this.day = day;
            this.time = time;
        }

        public TimeBlock(String day, int time, Consultation consultation) {
            type = 1;
            this.day = day;
            this.time = time;
            this.consultation = consultation;
        }

        public TimeBlock(String day, int time, Event event) {
            type = 2;
            this.day = day;
            this.time = time;
            this.event = event;
        }

        public int getId(){
            if(isEvent()){
                return event.getEventId();
            } else {
                return Integer.parseInt(consultation.getConId());
            }
        }

        public Consultation getConsultation() {
            return consultation;
        }

        public void setConsultation(Consultation consultation) {
            this.consultation = consultation;
        }

        public Event getEvent() {
            return event;
        }

        public void setEvent(Event event) {
            this.event = event;
        }

        public boolean isEvent() {
            return type == 2;
        }

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }

        public String getDay() {
            return day;
        }

        public int getType() {
            return type;
        }

        public void setDay(String day) {
            this.day = day;
        }

}
