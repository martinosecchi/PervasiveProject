package com.smartbin.thrashcompanion.fragment;

import servlet.entities.SmartbinEntity;

/**
 * Created by Ivan on 28-Nov-16.
 */

public interface FragmentSwitcher {
    public void load_admin_page();
    public void load_single_page(SmartbinEntity sbe);
}
