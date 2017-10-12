package com.timelesssoftware.popularmovies.Utils.Helpers;

import android.content.Context;

import com.timelesssoftware.popularmovies.Models.FragmentSettingsObject;
import com.timelesssoftware.popularmovies.R;

/**
 * Created by Luka on 12.10.2017.
 *
 * Store the display settings for fragments
 */

public class FragmentSettingsHelper {
    public static FragmentSettingsObject getMostViewedSettings(Context context) {
        return new FragmentSettingsObject(R.color.md_brown_500, R.color.md_brown_300, context.getResources().getString(R.string.filter_movie_most_viewed));
    }

    public static FragmentSettingsObject getMostPopularSettings(Context context) {
        return new FragmentSettingsObject(R.color.md_teal_500, R.color.md_teal_300, context.getResources().getString(R.string.filter_movie_most_popular));
    }

    public static FragmentSettingsObject getFavorritedSettings(Context context) {
        return new FragmentSettingsObject(R.color.md_indigo_500, R.color.md_indigo_300, context.getResources().getString(R.string.filter_favorited_movies));
    }
}
