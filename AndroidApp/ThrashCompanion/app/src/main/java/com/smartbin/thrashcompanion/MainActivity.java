package com.smartbin.thrashcompanion;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.FrameLayout;

import com.smartbin.thrashcompanion.fragment.AdminFragment;
import com.smartbin.thrashcompanion.fragment.FragmentSwitcher;
import com.smartbin.thrashcompanion.fragment.SingleFragment;

import servlet.entities.SmartbinEntity;

public class MainActivity extends FragmentActivity implements FragmentSwitcher {
    final static String TAG = "activity_main", _lfrag = "last_frag_used_for_ph";
    FrameLayout placeholder;
    AdminFragment admin_frag;
    SingleFragment single_frag;

    String last_frag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        placeholder = (FrameLayout) findViewById(R.id.main_frame);

        //find fragment
        admin_frag = (AdminFragment) getSupportFragmentManager().findFragmentByTag(AdminFragment.TAG);
        single_frag = (SingleFragment) getSupportFragmentManager().findFragmentByTag(SingleFragment.TAG);

        //check for nulls
        if( savedInstanceState != null ) {
            last_frag = savedInstanceState.getString(_lfrag);
        }

        admin_frag = admin_frag == null ? new AdminFragment() : admin_frag;
        if( single_frag == null ) {
            single_frag =  new SingleFragment();
            single_frag.setArguments( new Bundle() );
        }

        if( last_frag != null ) {
            loadByTag(last_frag);
        } else load_admin_page();

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(placeholder == null)
            placeholder = (FrameLayout) findViewById(R.id.main_frame);
        loadByTag( savedInstanceState.getString(_lfrag) );
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(_lfrag, last_frag);
    }

    @Override
    public void load_admin_page() {
        loadByTag( AdminFragment.TAG );
    }

    @Override
    public void load_single_page(SmartbinEntity sbe) {
            single_frag = new SingleFragment();
            Bundle args = new Bundle();
            String[] v = new String[] { sbe.name, ""+sbe.calibration, ""+sbe.concentration, ""+sbe.level };
            args.putStringArray(SingleFragment._BIN_KEY, v );
            single_frag.setArguments(args);
            loadByTag( SingleFragment.TAG );
    }


    void loadByTag(String tag) {
        Fragment f = null;
        switch (tag) {
            case AdminFragment.TAG:
                f = admin_frag;
                break;
            case SingleFragment.TAG:
                f = single_frag;
                break;
            default:
                Log.i(TAG, "Load fragment yielded null result");
                return;
        }
        last_frag = tag;
        getSupportFragmentManager().beginTransaction().addToBackStack(null)
                .replace(placeholder.getId(), f, tag).commit();
    }

}
