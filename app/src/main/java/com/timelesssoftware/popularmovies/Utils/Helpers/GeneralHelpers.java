package com.timelesssoftware.popularmovies.Utils.Helpers;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by Luka on 11.10.2017.
 */

public class GeneralHelpers {
    /**
     * Konverta denstiy pixle v pixle
     *
     * @param dp
     * @return
     */
    public static float dpToPx(float dp, Context context) {
        DisplayMetrics dpMetrics = context.getResources().getDisplayMetrics();
        float px = dp * ((float) dpMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    /**
     * Konverta pixle v denstiy pixle
     *
     * @param px
     * @return
     */
    public static float pxToDp(float px, Context context) {
        DisplayMetrics dpMetrics = context.getResources().getDisplayMetrics();
        float dp = px / ((float) dpMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }
}
