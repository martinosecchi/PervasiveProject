package com.smartbin.thrashcompanion;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.GridView;

import com.smartbin.thrashcompanion.data.BinGridviewAdapter;
import com.smartbin.thrashcompanion.data.StatFragment;
import com.smartbin.thrashcompanion.web.ApiAdapter;

import java.net.MalformedURLException;
import java.util.Observable;
import java.util.Observer;

import servlet.entities.ContextEntity;
import servlet.entities.SmartbinEntity;

public class MainActivity extends AppCompatActivity implements Observer {
    GridView mListView;
    BinGridviewAdapter mListViewAdapter;
    SmartbinEntity[] mListBinFromServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestBeaconsFromWeb();
    }


    private void requestBeaconsFromWeb(){
        try {
            ApiAdapter.getBins(this);
        } catch (MalformedURLException e) {
            Log.e("WEB", "Malformed url "+e);
            e.printStackTrace();
        }
    }

    @Override
    public void update(Observable observable, Object data) {
        if(data instanceof ContextEntity[] ) {
            ApiAdapter.POSTER p = (ApiAdapter.POSTER) observable;
            ContextEntity[] res = (ContextEntity[]) data;
            StatFragment.newInstance(getApplicationContext(), p.name, res).show(getSupportFragmentManager(), "");
        }
        if(data instanceof SmartbinEntity[]) {
            if(data == null){
                Log.i("WEB", "No results");
                return;
            }
            mListBinFromServer = (SmartbinEntity[]) data;
            if(mListViewAdapter == null) {
                //only display new beacons
                mListView = (GridView) findViewById(R.id.bin_grid);
                mListViewAdapter = new BinGridviewAdapter(this.getBaseContext(), mListBinFromServer);
                mListView.setAdapter(mListViewAdapter);
            }

            for (SmartbinEntity be : mListBinFromServer) {
                Log.i("WEB FOUND ", be.name+" ("+ be.lat + ", " + be.lng + ")");
            }
        }
    }
}
