package com.smartbin.thrashcompanion.data;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.smartbin.thrashcompanion.R;

import java.util.ArrayList;

import servlet.entities.ContextEntity;
import servlet.entities.SmartbinEntity;

/**
 * Created by Ivan on 26-Mar-16.
 */
public class StatFragment extends DialogFragment {

    public final static String _LABEL_KEY = "labelforfragment";
    public final static String _DATA_KEY = "dataforfragment";
    final static String LEVEL = "thrash level", PRESSURE = "atmospheric pressure";

    /**
     * Generates a StatisticsFragment with the given seed data
     * @param c
     * @param data
     * @return
     */
    public static StatFragment newInstance(final Context c, final SmartbinEntity[] be, final ContextEntity[] data) {
        final StatFragment dialogFrag = new StatFragment();
        Bundle args = new Bundle();
        args.putSerializable(_DATA_KEY, data);
        args.putSerializable(_LABEL_KEY, be);
        dialogFrag.setArguments(args);
        return dialogFrag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.infoscreen, container, false);

        final SmartbinEntity be = ((SmartbinEntity[]) getArguments().getSerializable(_LABEL_KEY))[0];
        TextView lbl = (TextView) v.findViewById(R.id.info_bin_name);
        lbl.setText(be.name);
        Log.i("STAT", "Calibration: "+be.calibration);
        final ContextEntity[] data = (ContextEntity[]) getArguments().getSerializable(_DATA_KEY);
        Log.i("STAT", "data prepared "+data.length);
        if(data == null || data.length == 0) return v;
        //Data
        Object[] dd = genData(data, "Odour", "Levels");

        LineChart scent = genLineChart((ArrayList<String>) dd[0], (LineDataSet) dd[1]);
        LineChart level = genLineChart((ArrayList<String>) dd[0], (LineDataSet) dd[2]);



        LimitLine ll = new LimitLine(be.calibration, "Normal values");
        ll.setLineColor(Color.RED);
        ll.setLineWidth(2f);
        ll.setTextColor(Color.YELLOW);
        ll.setTextSize(10f);

        // programatically add the chart
        FrameLayout levelChartFrame = (FrameLayout) v.findViewById(R.id.frame_level);
        levelChartFrame.addView(level);

        FrameLayout scentChartFrame = (FrameLayout) v.findViewById(R.id.frame_smell);
        scentChartFrame.addView(scent);

        return v;
    }

    protected LineChart genLineChart(ArrayList<String> labels, LineDataSet values) {
        LineChart lc = new LineChart(getActivity());

        lc.setDrawGridBackground(false);
        LineData lds = new LineData(values);
        lc.setData( new LineData(values) );
        lc.setDescription( null );

        //Y axis format
        YAxis ylc = lc.getAxisLeft();
        ylc.setAxisMinimum(0f);
        ylc.setLabelCount(6, true);
        ylc.setTextColor(Color.GREEN);

        //X axis format
        XAxis xlc = lc.getXAxis();
        xlc.setLabelCount(12, true);
        xlc.setEnabled(true);
        xlc.setTextColor(Color.GREEN);

        lc.setClickable(false);

        return lc;
    }

    /**
     * Returns Arraylist of string labels, and datasets of concentrations and levels
     * @param raw
     * @param label
     * @return Object[3]
     */
    protected Object[] genData(ContextEntity[] raw, String label, String level) {
        ArrayList<Entry> vals = new ArrayList<>();
        ArrayList<Entry> vals2 = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();

        int index = 0;
        for(int i = raw.length-1; i >=0; i--) {
            vals.add( new Entry(index, raw[i].concentration) );
            vals2.add( new Entry(index++, raw[i].level) );
            labels.add( raw[i].date.toString() );
        }

        LineDataSet ds = new LineDataSet(vals, label);
        ds.setDrawFilled(true);
        ds.setFillColor(Color.BLUE);
        ds.setDrawCircles(false);

        LineDataSet ds2 = new LineDataSet(vals2, level);
        ds.setDrawFilled(true);
        ds.setFillColor(Color.CYAN);
        ds.setDrawCircles(false);

        return new Object[]{labels,ds, ds2};
    }

    @Override
    public void onStart() {
        super.onStart();
        LayoutParams lp = new LayoutParams();
        Window w = getDialog().getWindow();
        lp.copyFrom(w.getAttributes());
        lp.width = LayoutParams.MATCH_PARENT;
        w.setAttributes(lp);

        getDialog().findViewById(R.id.info_dismiss).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}