package com.smartbin.thrashcompanion.data;

/**
 * Created by Ivan on 18-Nov-16.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.smartbin.thrashcompanion.R;
import com.smartbin.thrashcompanion.web.ApiAdapter;

import java.net.MalformedURLException;
import java.util.List;
import java.util.Observer;
import java.util.Set;

import servlet.entities.SmartbinEntity;

    public class BinGridviewAdapter extends ArrayAdapter<SmartbinEntity> {

        public BinGridviewAdapter(Context context, Set<SmartbinEntity> listThings) {
            this(context, listThings.toArray(new SmartbinEntity[listThings.size()]));
        }
        public BinGridviewAdapter(Context context, List<SmartbinEntity> listThings) {
            super(context, 0, listThings);
        }
        public BinGridviewAdapter(Context context, SmartbinEntity[] items) {
            super(context, 0, items);
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            final SmartbinEntity bc = getItem(position);
            if(convertView == null){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.bin_grid_item, parent, false);
            }
            TextView tname = (TextView)convertView.findViewById(R.id.b_name);
            TextView tcocn = (TextView)convertView.findViewById(R.id.b_conc);
            TextView tlvl = (TextView)convertView.findViewById(R.id.b_level);

            convertView.findViewById(R.id.b_frame).setOnClickListener(new View.OnClickListener() {
                final Context mContext = parent.getContext();
                @Override
                public void onClick(View v) {
                    try {
                        ApiAdapter.getContextsByBinName((Observer)mContext, bc.name);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }
            });

            tname.setText(bc.name);

            float dc = bc.calibration - bc.concentration;

            if(dc < 0f)
                convertView.setBackgroundColor(
                    getContext().getResources().getColor(android.R.color.holo_red_light)
                );
            else convertView.setBackgroundColor(
                    getContext().getResources().getColor(android.R.color.holo_green_light)
            );

            tcocn.setText("Concentration: "+bc.concentration);
            tlvl.setText("Thrash Level: "+bc.level);
            return convertView;
        }
}
