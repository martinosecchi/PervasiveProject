package com.smartbin.thrashcompanion.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.smartbin.thrashcompanion.R;
import com.smartbin.thrashcompanion.data.BinGridviewAdapter;
import com.smartbin.thrashcompanion.data.IonWrapper;
import com.smartbin.thrashcompanion.data.StatFragment;

import servlet.entities.ContextEntity;
import servlet.entities.SmartbinEntity;

/**
 * Created by Ivan on 28-Nov-16.
 */

public class AdminFragment extends Fragment implements IonWrapper.SmartbinConsumer, IonWrapper.ContextConsumer {
    public static String TAG = "adm_frag_tg";
    GridView mListView;
    BinGridviewAdapter mListViewAdapter;
    SmartbinEntity[] mListBinFromServer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.admin_layout, container, false);
        mListView = (GridView) v.findViewById(R.id.bin_grid);
        try {
            IonWrapper.getBins(getActivity(), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            IonWrapper.getBins(getActivity(), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void post(ContextEntity[] responseData, SmartbinEntity source) {
        Log.i(TAG, "Post ContextEntity fired");
        if( responseData == null || responseData.length == 0 || source == null) {
            Toast.makeText(getActivity(), "Not enough context information...", Toast.LENGTH_SHORT).show();
            return;
        }
        SmartbinEntity[] be = new SmartbinEntity[]{ source };
        StatFragment.newInstance(getContext(), be, responseData).show(getFragmentManager(), "");
    }

    @Override
    public void post(SmartbinEntity[] responseData) {
        Log.i(TAG, "Post SmartbinEntity fired");
        if(responseData == null || responseData.length == 0) {
            mListViewAdapter.notifyDataSetChanged();
            return;
        }
        mListBinFromServer = responseData;
        if(mListViewAdapter == null) {
            //only display new beacons
            mListViewAdapter = new BinGridviewAdapter(getContext(), mListBinFromServer, this);
            mListView.setAdapter(mListViewAdapter);
        }
        mListViewAdapter.notifyDataSetChanged();
    }
}
