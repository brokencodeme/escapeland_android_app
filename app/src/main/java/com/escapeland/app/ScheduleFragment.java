package com.escapeland.app;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ScheduleFragment extends Fragment {
    View view;


    public static ScheduleFragment newInstance(String val) {
        ScheduleFragment fragment = new ScheduleFragment();
        Bundle args = new Bundle();
        args.putString("data", val);
        fragment.setArguments(args);
        return fragment;
    }

    String data;
    String[] datas;
    TextView c;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_schedule, container, false);
        data = getArguments().getString("data", "");
        datas = data.split((","));
        data = "";
        for(int i=0; i< datas.length; i++){
            data += (i+1) + ". " + datas[i] + "\n\n";
        }
        c = view.findViewById(R.id.baba);
        c.setText("" + data);
        return view;
    }

}