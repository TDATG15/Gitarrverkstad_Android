package com.example.chris.gitarrverkstad;

import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private List<Appointment> appointments = new ArrayList<Appointment>();
    View currentView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.home_layout, container, false);
        populateList();
        populateListView();
        registerClickCallback();
        return currentView;
    }

    private void populateList(){
        appointments.add(new Appointment("2017-02-28", "Hitta fel på gitarr", "Kalle Karlsson", "13:00"));
        appointments.add(new Appointment("2017-03-11", "Byt strängar på gitarr", "Pelle Persson", "14:00"));
        appointments.add(new Appointment("2017-03-15", "Stämma en gitarr", "Lukas Lundqvist", "15:00"));
    }

    private void populateListView(){
        ArrayAdapter<Appointment> adapter = new CustomListAdapter();
        ListView list = (ListView) currentView.findViewById(R.id.next_appointment_list);
        list.setAdapter(adapter);
        TextView textView = (TextView) currentView.findViewById(R.id.selected_cust);
        textView.setText(appointments.get(0).getCustomer());
        textView = (TextView) currentView.findViewById(R.id.selected_desc);
        textView.setText(appointments.get(0).getDescription());
        textView = (TextView) currentView.findViewById(R.id.selected_time);
        textView.setText(appointments.get(0).getTime());
    }

    private void registerClickCallback(){
        ListView list = (ListView) currentView.findViewById(R.id.next_appointment_list);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id){
                TextView textView = (TextView) currentView.findViewById(R.id.selected_cust);
                textView.setText(appointments.get(position).getCustomer());
                textView = (TextView) currentView.findViewById(R.id.selected_desc);
                textView.setText(appointments.get(position).getDescription());
                textView = (TextView) currentView.findViewById(R.id.selected_time);
                textView.setText(appointments.get(position).getTime());
            }
        });

    }

    private class CustomListAdapter extends ArrayAdapter<Appointment>{
        public CustomListAdapter(){
            super(getActivity(), R.layout.item_next_appointment, appointments);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            View itemView = convertView;
            if(itemView == null){
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
}
