package com.example.chris.gitarrverkstad;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by stefa_000 on 2017-02-10.
 */

public class ScheduleFragment extends Fragment {
    View currentView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.schedule_layout, container, false);
        return currentView;
    }
}
