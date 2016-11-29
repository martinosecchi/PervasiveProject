package com.smartbin.thrashcompanion;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.FrameLayout;

import com.smartbin.thrashcompanion.fragment.AdminFragment;
import com.smartbin.thrashcompanion.fragment.FragmentSwitcher;
import com.smartbin.thrashcompanion.fragment.SingleFragment;

import servlet.entities.SmartbinEntity;

public class MainActivity extends FragmentActivity implements FragmentSwitcher {
    FrameLayout placeholder;
    AdminFragment admin_frag;
    SingleFragment single_frag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        placeholder = (FrameLayout) findViewById(R.id.main_frame);

        //find fragment
        admin_frag = (AdminFragment) getSupportFragmentManager().findFragmentByTag(AdminFragment.TAG);
        single_frag = (SingleFragment) getSupportFragmentManager().findFragmentByTag(SingleFragment.TAG);

        //check for nulls
        admin_frag = admin_frag == null ? new AdminFragment() : admin_frag;
        if( single_frag == null ) {
            single_frag =  new SingleFragment();
            single_frag.setArguments( new Bundle() );
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(placeholder.getId(), admin_frag, admin_frag.TAG).addToBackStack(null)
                .commit();
    }

    @Override
    public void load_admin_page() {
        getSupportFragmentManager().beginTransaction().addToBackStack(null)
                .replace(placeholder.getId(), admin_frag, admin_frag.TAG).commit();
    }

    @Override
    public void load_single_page(SmartbinEntity sbe) {
        // Create fragment and give it an argument specifying the article it should show
        Bundle args = single_frag.getArguments();
        args.putSerializable(single_frag._BIN_KEY, new SmartbinEntity[]{sbe} );

        getSupportFragmentManager().beginTransaction().addToBackStack(null)
                .replace(placeholder.getId(), single_frag, single_frag.TAG).commit();
    }


}
