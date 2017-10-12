package com.timelesssoftware.popularmovies;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.timelesssoftware.popularmovies.Models.FragmentSettingsObject;
import com.timelesssoftware.popularmovies.UI.Fragments.MostViewdMoviesFragment;
import com.timelesssoftware.popularmovies.UI.Fragments.UserFavoritedMoviesFragment;
import com.timelesssoftware.popularmovies.Utils.Helpers.FragmentSettingsHelper;
import com.timelesssoftware.popularmovies.Utils.Helpers.MovieEndpopintHelper;


public class MainMovieActivity extends AppCompatActivity {

    private static final String MOST_VIEWED_FRAGMENT = "most_viewed_fragment";
    private static final String MOST_POPULAR_FRAGMENT = "most_popular_fragment";


    private AHBottomNavigation bottomNavigation;

    private AHBottomNavigation.OnTabSelectedListener onTabSelectedListener = new AHBottomNavigation.OnTabSelectedListener() {
        @Override
        public boolean onTabSelected(int position, boolean wasSelected) {
            switch (position) {
                case 0:
                    MostViewdMoviesFragment mostViewdMoviesFragment = MostViewdMoviesFragment.newInstance(MovieEndpopintHelper.MOST_VIEWD_MOVIES,
                            FragmentSettingsHelper.getMostViewedSettings(MainMovieActivity.this)
                    );
                    getSupportFragmentManager()
                            .beginTransaction()
                            //.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                            .replace(R.id.content, mostViewdMoviesFragment, MOST_VIEWED_FRAGMENT).commit();
                    return true;
                case 1:
                    MostViewdMoviesFragment mostPopoularMovies = MostViewdMoviesFragment.newInstance(MovieEndpopintHelper.MOST_POPULAR_MOVIES,
                            FragmentSettingsHelper.getMostPopularSettings(MainMovieActivity.this));
                    getSupportFragmentManager()
                            .beginTransaction()
                            //.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                            .replace(R.id.content, mostPopoularMovies, MOST_POPULAR_FRAGMENT).commit();
                    return true;
                case 2:
                    UserFavoritedMoviesFragment userFavoritedMoviesFragment = UserFavoritedMoviesFragment.newInstance(
                            FragmentSettingsHelper.getFavorritedSettings(MainMovieActivity.this));
                    getSupportFragmentManager()
                            .beginTransaction()
                            /*.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)*/
                            .replace(R.id.content, userFavoritedMoviesFragment).commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_movie);
        bottomNavigation = findViewById(R.id.bottom_navigation);
        setUpBottomNavigation();

        bottomNavigation.setOnTabSelectedListener(onTabSelectedListener);

        if (savedInstanceState == null) {
            MostViewdMoviesFragment mostViewdMoviesFragment = MostViewdMoviesFragment.newInstance(MovieEndpopintHelper.MOST_VIEWD_MOVIES,
                    FragmentSettingsHelper.getMostViewedSettings(MainMovieActivity.this));
            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                    .replace(R.id.content, mostViewdMoviesFragment, MOST_VIEWED_FRAGMENT).commit();
        } else {
            getSupportFragmentManager().findFragmentByTag(MOST_VIEWED_FRAGMENT);
        }
    }


    private void setUpBottomNavigation() {
        AHBottomNavigationItem item1 = new AHBottomNavigationItem(R.string.filter_movie_most_viewed, R.drawable.ic_achivment, R.color.md_brown_500);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(R.string.filter_movie_most_popular, R.drawable.ic_format_list_numbered_black_24dp, R.color.md_teal_500);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(R.string.filter_favorited_movies, R.drawable.ic_favorite_border_black_24dp, R.color.md_indigo_500);
        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);
        bottomNavigation.setColored(true);
        bottomNavigation.setColoredModeColors(Color.WHITE,
                Color.BLACK);
        bottomNavigation.setInactiveColor(Color.GRAY);
    }
}
