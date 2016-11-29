package com.smartbin.thrashcompanion.fragment;

import android.graphics.PorterDuff;
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
import com.smartbin.thrashcompanion.data.UIUtil;

/**
 * Created by Ivan on 28-Nov-16.
 */

public class SingleFragment extends Fragment {
    public static final String TAG = "single_bin_view_frag", _BIN_KEY = "single_bin_item_to_populate";
    private float max_lvl = 100;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "Create view");
        View v = inflater.inflate(R.layout.fragment_single, container, false);

        TextView tvcon = (TextView) v.findViewById(R.id.single_conc);
        TextView tvlvl = (TextView) v.findViewById(R.id.single_level);

        String[] sbe = null;
        if( getArguments() != null && getArguments().getStringArray(_BIN_KEY) != null )
            sbe = getArguments().getStringArray(_BIN_KEY);

        if(sbe == null) {
            Log.i(TAG, "Smartbin was null");
            return v;
        }
        Log.i(TAG, sbe[0]+", lvl "+sbe[3]+", conc "+sbe[2]+", calib "+sbe[1]);

        float c = Float.parseFloat(sbe[2]);

        msetColor(v, UIUtil.getBinColor(c));

        tvcon.setText( ""+sbe[2] + " ppm" );
        tvlvl.setText( ""+(Float.parseFloat(sbe[3]) / max_lvl) * 100 );
        Log.i(TAG, "Update text values");

        return v;
    }


    private void msetColor(View root, int color)
    {
        Log.i(TAG, "Got high ppm");

        ImageView iv = ((ImageView) root.findViewById(R.id.single_bin));
        iv.setImageResource(R.drawable.bin);
        iv.setColorFilter( getResources().getColor(color), PorterDuff.Mode.SRC_ATOP );
        ((TextView) root.findViewById(R.id.single_conc)).setTextColor( getResources().getColor(color) );
        ((TextView) root.findViewById(R.id.single_level)).setTextColor( getResources().getColor(color) );
    }

}
