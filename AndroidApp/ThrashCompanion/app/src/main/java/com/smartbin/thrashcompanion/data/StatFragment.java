package com.smartbin.thrashcompanion.data;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.smartbin.thrashcompanion.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import servlet.entities.ContextEntity;

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
    public static StatFragment newInstance(final Context c, final String label, final ContextEntity[] data) {
        final StatFragment dialogFrag = new StatFragment();
        Bundle args = new Bundle();
        args.putSerializable(_DATA_KEY, data);
        args.putString(_LABEL_KEY, label);
        dialogFrag.setArguments(args);
        return dialogFrag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.infoscreen, container, false);

        final String label = getArguments().getString(_LABEL_KEY);
        TextView lbl = (TextView) v.findViewById(R.id.info_bin_name);
        lbl.setText(label);

        final ContextEntity[] data = (ContextEntity[]) getArguments().getSerializable(_DATA_KEY);

        //Data
        BarData[] barDatas = generateBarData(data);

        // create a new chart object
        BarChart snd = new BarChart(getActivity());
        snd.setDescription("");
        snd.setDrawGridBackground(false);
        snd.setDrawBarShadow(false);
        snd.setData(barDatas[0]);
        YAxis leftAxis = snd.getAxisLeft();
        leftAxis.setAxisMinValue(0f); // this replaces setStartAtZero(true)
       // leftAxis.setAxisMaxValue(30f);
        snd.getAxisRight().setEnabled(false);
        XAxis xAxis = snd.getXAxis();
        xAxis.setEnabled(false);

        // create a new chart object
        BarChart prs = new BarChart(getActivity());
        prs.setDescription("");
        prs.setDrawGridBackground(false);
        prs.setDrawBarShadow(false);
        prs.setData(barDatas[1]);
        YAxis leftAxis1 = prs.getAxisLeft();
        leftAxis1.setAxisMinValue(0f);
      //  leftAxis1.setAxisMaxValue(50f);// this replaces setStartAtZero(true)
        prs.getAxisRight().setEnabled(false);
        XAxis xAxis1 = prs.getXAxis();
        xAxis1.setEnabled(false);

        // programatically add the chart
        FrameLayout sound = (FrameLayout) v.findViewById(R.id.frame_level);
        sound.addView(snd);

        FrameLayout pres = (FrameLayout) v.findViewById(R.id.frame_smell);
        pres.addView(prs);

        return v;
    }

    protected BarData[] generateBarData(ContextEntity[] data) {
        IBarDataSet[] set = new IBarDataSet[2];
        int snx = 0, psx = 0;
        ArrayList<BarEntry> sound = new ArrayList<BarEntry>();
        List<String> soundLbl = new ArrayList<String>();
        ArrayList<BarEntry> pres = new ArrayList<BarEntry>();
        List<String> presLbl = new ArrayList<String>();
        for(int i = data.length-1; i >= 0; i--) {
            Date tm = data[i].date;
            sound.add(new BarEntry((float) data[i].level, snx));
            soundLbl.add(snx++, tm.toString() );
            pres.add(new BarEntry((float) data[i].concentration, psx));
            presLbl.add(psx++, tm.toString() );
        }

        BarDataSet ds1 = new BarDataSet(sound, "Thrash Levels");
        ds1.setColors( new int[]{ColorTemplate.VORDIPLOM_COLORS[0]} );
        ds1.setDrawValues(false);
        set[0] = ds1;
        BarDataSet ds2 = new BarDataSet(pres, "Odour Levels");
        ds2.setColors(new int[]{ColorTemplate.VORDIPLOM_COLORS[3]});
        ds2.setDrawValues(false);
        set[1] = ds2;

        BarData[] d = new BarData[]{new BarData(  soundLbl, ds1), new BarData(presLbl, ds2)};
        return d;
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