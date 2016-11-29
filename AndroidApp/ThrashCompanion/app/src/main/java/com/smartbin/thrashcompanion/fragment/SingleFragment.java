package com.smartbin.thrashcompanion.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartbin.thrashcompanion.R;

import servlet.entities.SmartbinEntity;

/**
 * Created by Ivan on 28-Nov-16.
 */

public class SingleFragment extends Fragment {
    public static final String TAG = "single_bin_view_frag";
    public final String _BIN_KEY = "single_bin_item_to_populate";
    private float max_lvl = 100;


    TextView tvcon, tvlvl;
    ImageView ivbin;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_single, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView tvcon = (TextView) view.findViewById(R.id.single_conc);
        ImageView ivbin = (ImageView) view.findViewById(R.id.single_bin);
        TextView tvlvl = (TextView) view.findViewById(R.id.single_level);

        SmartbinEntity sbe = ((SmartbinEntity[]) (getArguments().getSerializable(_BIN_KEY)))[0];

        if(sbe == null) {
            Log.i(TAG, "Smartbin was null");
            return;
        }
        Log.i(TAG, sbe.name+", lvl "+sbe.level+", conc "+sbe.concentration);

        tvcon.setText( sbe.concentration + " ppm" );
        tvlvl.setText( (sbe.level / max_lvl) * 100 + " %" );

        float dc = sbe.calibration - sbe.concentration;

        if(dc < 0f) {
            Log.i(TAG, "Set color red");
            msetColor(view, android.R.color.holo_red_dark);
        }
        else {
            Log.i(TAG, "Set color green");
            msetColor(view, android.R.color.holo_green_light);
        }
    }

    private void msetColor(View p, int color) {
        ((ImageView) p.findViewById(R.id.single_bin)).setColorFilter( color );
        ((TextView) p.findViewById(R.id.single_conc)).setTextColor( color );
        ((TextView) p.findViewById(R.id.single_level)).setTextColor( color );
    }

}
