package com.smartbin.thrashcompanion.data;

/**
 * Created by Ivan on 29-Nov-16.
 */

public class UIUtil {
    final static int
            ALOT_OVER = android.R.color.holo_red_dark,
            OVER = android.R.color.holo_orange_dark,
            CLOSE = android.R.color.holo_green_light,
            UNDER = android.R.color.holo_green_dark,
            ALOT_UNDER = android.R.color.holo_blue_dark;

    final static float midDev = .05f, maxDev = .25f;

    public static int getBinColor(float concentration) {
        boolean positive = concentration >= 0;
        concentration = Math.abs(concentration); //make positive for simplicity
        if(concentration <= (1+midDev) ) { //within 5% deviation
            return CLOSE;
        }
        else if(concentration < (1+maxDev) ) { //within max deviation
            return positive ? OVER : UNDER;
        }
        else return positive ? ALOT_OVER : ALOT_UNDER; //over max deviation
    }

}
