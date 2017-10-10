package com.timelesssoftware.popularmovies;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.timelesssoftware.popularmovies.UI.Fragments.MostViewdMoviesFragment;
import com.timelesssoftware.popularmovies.UI.Fragments.UserFavoritedMoviesFragment;
import com.timelesssoftware.popularmovies.Utils.Helpers.MovieEndpopintHelper;


public class MainMovieActivity extends AppCompatActivity {

    private static final String MOST_VIEWED_FRAGMENT = "most_viewed_fragment";
    private static final String MOST_POPULAR_FRAGMENT = "most_popular_fragment";
    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.most_viewed_fragment:
                    MostViewdMoviesFragment mostViewdMoviesFragment = MostViewdMoviesFragment.newInstance(MovieEndpopintHelper.MOST_VIEWD_MOVIES);
                    getSupportFragmentManager()
                            .beginTransaction()
                            //.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                            .replace(R.id.content, mostViewdMoviesFragment, MOST_VIEWED_FRAGMENT).commit();
                    return true;
                case R.id.most_popular_fragment:
                    MostViewdMoviesFragment mostPopoularMovies = MostViewdMoviesFragment.newInstance(MovieEndpopintHelper.MOST_POPULAR_MOVIES);
                    getSupportFragmentManager()
                            .beginTransaction()
                            //.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                            .replace(R.id.content, mostPopoularMovies, MOST_POPULAR_FRAGMENT).commit();
                    return true;
                case R.id.user_favorited_fragment:
                    UserFavoritedMoviesFragment userFavoritedMoviesFragment = UserFavoritedMoviesFragment.newInstance();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
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
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        if (savedInstanceState == null) {
            MostViewdMoviesFragment mostViewdMoviesFragment = MostViewdMoviesFragment.newInstance(MovieEndpopintHelper.MOST_VIEWD_MOVIES);
            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                    .replace(R.id.content, mostViewdMoviesFragment, MOST_VIEWED_FRAGMENT).commit();
        } else {
            getSupportFragmentManager().findFragmentByTag(MOST_VIEWED_FRAGMENT);
        }
    }
}
