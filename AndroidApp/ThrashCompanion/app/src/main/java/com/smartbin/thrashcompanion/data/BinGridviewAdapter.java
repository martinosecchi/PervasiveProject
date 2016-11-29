package com.smartbin.thrashcompanion.data;

/**
 * Created by Ivan on 18-Nov-16.
 */

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartbin.thrashcompanion.R;

import java.util.List;
import java.util.Set;
import servlet.entities.SmartbinEntity;

public class BinGridviewAdapter extends ArrayAdapter<SmartbinEntity> {
        IonWrapper.ContextConsumer consumer;

        public BinGridviewAdapter(Context context, Set<SmartbinEntity> listThings) {
            this(context, listThings.toArray(new SmartbinEntity[listThings.size()]));
        }
        public BinGridviewAdapter(Context context, List<SmartbinEntity> listThings) {
            super(context, 0, listThings);
        }
        public BinGridviewAdapter(Context context, SmartbinEntity[] items) {
            super(context, 0, items);
        }
        public BinGridviewAdapter(Context context, SmartbinEntity[] items, IonWrapper.ContextConsumer callback) {
            super(context, 0, items);
            consumer = callback;
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            if(convertView == null){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.bin_grid_item, parent, false);
            }
            final SmartbinEntity bc = getItem(position);
            ImageView bframe = (ImageView) convertView.findViewById(R.id.b_frame);
            TextView tname = (TextView)convertView.findViewById(R.id.b_name);
            TextView tcocn = (TextView)convertView.findViewById(R.id.b_conc);
            TextView tlvl = (TextView)convertView.findViewById(R.id.b_level);

            bframe.setOnClickListener(new View.OnClickListener() {
                final FragmentActivity mContext = (FragmentActivity) parent.getContext();

                @Override
                public void onClick(View v) {
                        IonWrapper.getContextsForBin(mContext, consumer, bc);
                }
            });

            if(bc != null) {

                tname.setText(bc.name);

                float dc = bc.calibration - bc.concentration;

                if(dc < 0f) {
                    bframe.setColorFilter( getContext().getResources().getColor(android.R.color.holo_red_dark) );
                }
                else {
                    bframe.setColorFilter( getContext().getResources().getColor(android.R.color.holo_green_dark) );
                }

                tcocn.setText("Concentration: "+bc.concentration);
                tlvl.setText("Thrash Level: "+bc.level);
            }
            return convertView;
        }
}
