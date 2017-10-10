package com.timelesssoftware.popularmovies;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.timelesssoftware.popularmovies.Activities.MovieDeatiledActivity;
import com.timelesssoftware.popularmovies.Models.MovieModel;
import com.timelesssoftware.popularmovies.Models.MoviesListModel;
import com.timelesssoftware.popularmovies.UI.Adapters.MovieListAdapter;
import com.timelesssoftware.popularmovies.Utils.Network.ApiHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity implements MovieListAdapter.OnMovieSelectListener {

    @Inject
    ApiHandler apiHandler;
    private RecyclerView movieListRv;
    private List<MovieModel> movieModelList = new ArrayList<>();
    private MovieListAdapter movieListAdapter;
    private boolean loading = true;
    private int visibleThreshold = 5;
    int firstVisibleItem, visibleItemCount, totalItemCount;
    private GridLayoutManager mLayoutManager;
    private int currentPage = 1;
    private int previousTotal = 0;

    private int currentSelectedFilter = R.id.order_most_viewed;
    private HashMap<String, String> params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PopularMoviesApp.getNetComponent().inject(this);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_filter);
        toolbar.setOverflowIcon(drawable);
        movieListRv = findViewById(R.id.movie_list_rv);
        mLayoutManager = new GridLayoutManager(this, 2);
        movieListRv.setLayoutManager(mLayoutManager);
        movieListRv.addOnScrollListener(onScrollListener);
        movieListAdapter = new MovieListAdapter(movieModelList, this);
        movieListAdapter.setmOnMovieSelectListener(this);
        movieListRv.setAdapter(movieListAdapter);

        params = new HashMap<>();
        params.put("sort_by", "popularity.desc");
        params.put("page", Integer.toString(currentPage));
        apiHandler.get("movie/popular", params, MoviesListModel.class).observeOn(AndroidSchedulers.mainThread()).subscribe(moviesListModelConsumer);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.order_most_poular && currentSelectedFilter != R.id.order_most_poular) {
            resetRvAdapter();
            params.put("page", "1");
            params.put("sort_by", "vote_count.desc");
            apiHandler.get("movie/top_rated", params, MoviesListModel.class).observeOn(AndroidSchedulers.mainThread()).subscribe(moviesListModelConsumer);
        }

        if (id == R.id.order_most_viewed && currentSelectedFilter != R.id.order_most_viewed) {
            resetRvAdapter();
            params.put("page", "1");
            params.put("sort_by", "popularity.desc");
            apiHandler.get("movie/popular", params, MoviesListModel.class).observeOn(AndroidSchedulers.mainThread()).subscribe(moviesListModelConsumer);
        }
        currentSelectedFilter = id;
        return super.onOptionsItemSelected(item);
    }

    public void resetRvAdapter() {
        movieModelList.clear();
        movieListAdapter.notifyDataSetChanged();
        currentPage = 1;
    }

    RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            if (dy > 0) //check for scroll down
            {
                visibleItemCount = recyclerView.getChildCount();
                totalItemCount = mLayoutManager.getItemCount();
                firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();

                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                    }
                }
                if (!loading && (totalItemCount - visibleItemCount)
                        <= (firstVisibleItem + visibleThreshold)) {
                    // End has been reached
                    // Do something
                    currentPage++;
                    params.put("page", Integer.toString(currentPage));
                    apiHandler.get("discover/movie", params, MoviesListModel.class).observeOn(AndroidSchedulers.mainThread()).subscribe(moviesListModelConsumer);
                    loading = true;
                }
            }
        }
    };


    @Override
    public void onSelectMovie(int position) {
        Log.d("MainActivity", position + "");
    }

    @Override
    public void onSelectMovieWithTransition(int position, ImageView imageView) {
        MovieModel movieModel = movieModelList.get(position);
        Intent intent = new Intent(MainActivity.this, MovieDeatiledActivity.class);
        intent.putExtra("movieModel", movieModel);
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(MainActivity.this,
                        imageView,
                        ViewCompat.getTransitionName(imageView));
        startActivity(intent, options.toBundle());
    }

    private Consumer<MoviesListModel> moviesListModelConsumer = new Consumer<MoviesListModel>() {
        @Override
        public void accept(MoviesListModel moviesListModel) throws Exception {
            List<MovieModel> movieModels = moviesListModel.results;
            if (movieModels == null) {
                Snackbar.make(movieListRv, "Can't find any movies?!?", Snackbar.LENGTH_LONG).show();
                return;
            }

            int count = movieModelList.size();
            for (MovieModel _movieModel : movieModels) {
                movieModelList.add(_movieModel);
                movieListAdapter.notifyItemInserted(count);
                count++;
            }
        }
    };
}
