package com.smartbin.thrashcompanion.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.smartbin.thrashcompanion.R;
import com.smartbin.thrashcompanion.data.IonWrapper;

import java.util.ArrayList;

import servlet.entities.SmartbinEntity;

/**
 * Created by Ivan on 28-Nov-16.
 */

public class MenuFragment extends Fragment implements IonWrapper.SmartbinConsumer {
    FragmentSwitcher fragmentswitcher;
    Button btn_admin, btn_single;
    Spinner dropdown;
    String last_bin;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_menu_layout, container,  false);

        btn_admin = (Button) v.findViewById(R.id.menu_admin);
        btn_single = (Button) v.findViewById(R.id.menu_single);
        dropdown = (Spinner) v.findViewById(R.id.menu_list);

        try {
            IonWrapper.getBins(getActivity(), this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return v;
    }

    @Override
    public void post(final SmartbinEntity[] responseData) {
        ArrayList<String> keys = new ArrayList<>();
        for(SmartbinEntity sbe : responseData) keys.add(sbe.name);

        dropdown.setAdapter( new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, keys){
            @NonNull
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView v = (TextView) super.getView(position, convertView, parent);
                v.setTextColor( getResources().getColor(android.R.color.holo_green_light) );
                return v;
            }
        } );

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                last_bin = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                last_bin = null;
            }
        });
        btn_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentSwitcher fs = (FragmentSwitcher) getActivity();
                fs.load_admin_page();
            }
        });

        btn_single.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentSwitcher fs = (FragmentSwitcher) getActivity();
                if(last_bin != null) fs.load_single_page( IonWrapper.getBin(last_bin) );
            }
        });
    }
}
