package com.bitanga.android.lynkactivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

public class SettingsFragment extends Fragment {

    Switch mSwitch;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings, container, false);

        mSwitch = (Switch) view.findViewById(R.id.switch1);
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //enabled = default
                //if unchecked, then disable GPS permissions?
                //toast message for now
                if (!isChecked) {
                    Toast.makeText(getContext(), "GPS is disabled",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "GPS is enabled",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

}
