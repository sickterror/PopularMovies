package com.timelesssoftware.popularmovies.Activities;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.timelesssoftware.popularmovies.Data.PopularMoviesContract;
import com.timelesssoftware.popularmovies.Models.MovieModel;
import com.timelesssoftware.popularmovies.Models.MovieReviewListModel;
import com.timelesssoftware.popularmovies.Models.MovieReviewListModelDigest;
import com.timelesssoftware.popularmovies.Models.MovieReviewModel;
import com.timelesssoftware.popularmovies.Models.MovieTrailerListModelDigest;
import com.timelesssoftware.popularmovies.Models.MovieTrailerModel;
import com.timelesssoftware.popularmovies.PopularMoviesApp;
import com.timelesssoftware.popularmovies.R;
import com.timelesssoftware.popularmovies.UI.Adapters.MovieReviewAdapter;
import com.timelesssoftware.popularmovies.UI.Adapters.MovieTrailerAdapter;
import com.timelesssoftware.popularmovies.Utils.Helpers.ContentValuesMapper;
import com.timelesssoftware.popularmovies.Utils.Helpers.GeneralHelpers;
import com.timelesssoftware.popularmovies.Utils.Helpers.LukaRvScrollListener;
import com.timelesssoftware.popularmovies.Utils.Network.ApiHandler;
import com.timelesssoftware.popularmovies.Utils.Network.ImageHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

//https://www.youtube.com/watch?v=
public class MovieDeatiledActivity extends AppCompatActivity implements View.OnClickListener, MovieTrailerAdapter.OnTrailerSelectListener {


    private static final String MOVIE_REVIEW_LIST = "movie_review_list";
    private static final String MOVIE_TRAILER_LIST = "movie_trailer_list";
    private static final String MOVIE_TRAILER_BASE_URL = "https://www.youtube.com/watch?v=";
    private ImageView moviePosterIv;
    private ImageView movieBackgrdopIv;

    private TextView movieTitleTv;
    private TextView movieReleaseDateTv;
    private TextView movieScoreTv;
    private TextView movieDescriptionTv;
    private MovieModel movieModel;
    private static int mInOrient = -1;
    private boolean shouldCancelTransition = false;
    private MovieReviewAdapter movieReviewAdapter;
    private RecyclerView movieReviewRecyclerView;
    private View bottomSheet;
    private List<MovieReviewModel> movieReviewModelList;
    private List<MovieTrailerModel> movieTrailerModelList;
    private ImageButton mCloseMovieReveiewBS;
    @Inject
    ApiHandler apiHandler;
    private RecyclerView mRecylcerView;
    private RelativeLayout bottomSheetLayout;
    private View mMovieReviewToolbar;
    private BottomSheetBehavior<RelativeLayout> bottomSheetBehavior;
    private RecyclerView mMovieTrailersRv;
    private MovieTrailerAdapter moviTrailerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PopularMoviesApp.getNetComponent().inject(this);
        overridePendingTransition(R.anim.slide_out_activity, R.anim.slide_in_activity);
        setContentView(R.layout.activity_movie_deatiled);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Bundle data = getIntent().getExtras();
        movieModel = (MovieModel) data.getParcelable("movieModel");
        moviePosterIv = findViewById(R.id.movie_detailed_poster);
        movieBackgrdopIv = findViewById(R.id.movie_backdrop);
        moviePosterIv.setTransitionName(getResources().getString(R.string.movie_poster_transition_name));
        movieTitleTv = findViewById(R.id.movie_title);
        movieReleaseDateTv = findViewById(R.id.movie_date);
        movieScoreTv = findViewById(R.id.movie_score);
        movieDescriptionTv = findViewById(R.id.movie_description);
        movieReviewRecyclerView = findViewById(R.id.movie_review_rv);
        mCloseMovieReveiewBS = findViewById(R.id.close_movie_review_bs);
        bottomSheetLayout = findViewById(R.id.linear_layout_bottom_sheet);
        mMovieReviewToolbar = findViewById(R.id.moview_review_toolbar);
        mMovieTrailersRv = findViewById(R.id.movie_trailers_rv);
        findViewById(R.id.marked_as_favorited).setOnClickListener(this);
        mMovieReviewToolbar.setBackgroundColor(movieModel.getColor());

        mMovieReviewToolbar.setOnClickListener(this);
        mCloseMovieReveiewBS.setOnClickListener(this);
        populateFields();

        mInOrient = (mInOrient == -1) ? getResources().getConfiguration().orientation : mInOrient;
        animate();
        Log.d("MovieDeatiledActivity", movieModel.getId() + "");

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout);
        bottomSheetBehavior.setBottomSheetCallback(bottomSheetCallback);
        if (savedInstanceState == null) {
            movieReviewModelList = new ArrayList<>();
            movieTrailerModelList = new ArrayList<>();
            loadMovieReviews("1");
            loadMovieTrailers();
        } else {
            movieReviewModelList = savedInstanceState.getParcelableArrayList(MOVIE_REVIEW_LIST);
            movieTrailerModelList = savedInstanceState.getParcelableArrayList(MOVIE_TRAILER_LIST);
        }
        initReviewRecylcerView();
        initTrailerRecylcerView();
    }

    void animate() {
        String imageUrl = ImageHelper.generateImageUrl(movieModel.poster_path, ImageHelper.ImageSizes.w342);
        supportPostponeEnterTransition();
        Glide.with(this)
                .load(imageUrl)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        supportStartPostponedEnterTransition();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        supportStartPostponedEnterTransition();
                        return false;
                    }
                })
                .into(moviePosterIv);

        Glide.with(this).load(ImageHelper.generateImageUrl(movieModel.backdrop_path, ImageHelper.ImageSizes.w342)).into(movieBackgrdopIv);
    }

    private void populateFields() {
        movieTitleTv.setText(movieModel.getTitle());
        movieReleaseDateTv.setText(movieModel.getRelease_date());
        movieScoreTv.setText(movieModel.getVote_average() + "");
        movieDescriptionTv.setText(movieModel.getOverview());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (shouldCancelTransition) {
            finish();
        } else {
            finishAfterTransition();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        int orient = getResources().getConfiguration().orientation;
        if (orient != mInOrient) {
            shouldCancelTransition = true;
            getWindow().setSharedElementReturnTransition(null);
            moviePosterIv.setTransitionName(null);
        }
    }

    private void initReviewRecylcerView() {
        movieReviewAdapter = new MovieReviewAdapter(movieReviewModelList);
        movieReviewRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        movieReviewRecyclerView.setAdapter(movieReviewAdapter);
        movieReviewRecyclerView.addOnScrollListener(new LukaRvScrollListener.PageScrollListener(1) {
            @Override
            public void onNextPage(int page, int nextPage) {
                loadMovieReviews(String.valueOf(page));
            }
        });
    }

    private void initTrailerRecylcerView() {
        moviTrailerAdapter = new MovieTrailerAdapter(movieTrailerModelList, this);
        mMovieTrailersRv.setLayoutManager(new GridLayoutManager(this, 2));
        mMovieTrailersRv.setAdapter(moviTrailerAdapter);
        mMovieTrailersRv.setNestedScrollingEnabled(false);
    }


    private void loadMovieReviews(String page) {
        HashMap<String, String> map = new HashMap<>();
        map.put("page", page);
        apiHandler.get("movie/" + movieModel.getId() + "/reviews", map, MovieReviewListModelDigest.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(movieReviewListModelDigestConsumer);
    }

    private void loadMovieTrailers() {
        apiHandler.get("movie/" + movieModel.getId() + "/videos", null, MovieTrailerListModelDigest.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(movieTrailerListModelDigestConsumer);
    }

    public Consumer<MovieReviewListModelDigest> movieReviewListModelDigestConsumer = new Consumer<MovieReviewListModelDigest>() {
        @Override
        public void accept(MovieReviewListModelDigest movieReviewListModelDigest) throws Exception {
            if (movieReviewListModelDigest != null) {
                for (int i = 0; i != movieReviewListModelDigest.results.size(); i++) {
                    movieReviewModelList.add(movieReviewListModelDigest.results.get(i));
                    movieReviewAdapter.notifyItemInserted(i);
                }
            }
        }
    };

    public Consumer<MovieTrailerListModelDigest> movieTrailerListModelDigestConsumer = new Consumer<MovieTrailerListModelDigest>() {
        @Override
        public void accept(MovieTrailerListModelDigest movieTrailerListModelDigest) throws Exception {
            if (movieTrailerListModelDigest != null) {
                for (int i = 0; i != movieTrailerListModelDigest.results.size(); i++) {
                    movieTrailerModelList.add(movieTrailerListModelDigest.results.get(i));
                    moviTrailerAdapter.notifyItemInserted(i);
                }
            }
        }
    };

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putParcelableArrayList(MOVIE_REVIEW_LIST, new ArrayList<>(movieReviewModelList));
        outState.putParcelableArrayList(MOVIE_TRAILER_LIST, new ArrayList<>(movieTrailerModelList));
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
        }
    }

    BottomSheetBehavior.BottomSheetCallback bottomSheetCallback = new BottomSheetBehavior.BottomSheetCallback() {


        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                //mCloseMovieReveiewBS.setVisibility(View.VISIBLE);
                float margin = GeneralHelpers.dpToPx(16, MovieDeatiledActivity.this.getApplicationContext());
                mCloseMovieReveiewBS.animate().setInterpolator(new BounceInterpolator()).setDuration(500).alpha(1).translationX(-margin);
            } else {
                //mCloseMovieReveiewBS.setVisibility(View.INVISIBLE);
                mCloseMovieReveiewBS.animate().alpha(0).translationX(0);
            }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {

        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.moview_review_toolbar:
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                break;
            case R.id.close_movie_review_bs:
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                break;
            case R.id.marked_as_favorited:
                Log.d("MovieDetAct", "mark fav");
                ContentValues contentValues = ContentValuesMapper.mapMovieModelToContentValues(movieModel);
                getContentResolver().insert(PopularMoviesContract.FavoritedMovieField.CONTENT_URI,
                        contentValues);

                Cursor c = getContentResolver().query(PopularMoviesContract.FavoritedMovieField.CONTENT_URI, null, null, null, null);
                if (c != null) {
                    while (c.moveToNext()) {
                        MovieModel model = ContentValuesMapper.mapCursorToMovieModel(c);
                        Log.d("Cursror", model.getId() + "");
                    }
                }

                Uri uri = PopularMoviesContract.FavoritedMovieField.CONTENT_URI.buildUpon().appendPath("19404").build();
                Cursor ca = getContentResolver().query(uri, null, PopularMoviesContract.FavoritedMovieField.id + " = ?", new String[]{"19404"}, null);
                if (ca != null) {
                    while (ca.moveToNext()) {
                        MovieModel model = ContentValuesMapper.mapCursorToMovieModel(ca);
                        Log.d("Cursror where ", model.getId() + "");
                    }
                }

                break;
        }
    }

    @Override
    public void onTrailerSelect(int position) {
        MovieTrailerModel movieTrailerModel = movieTrailerModelList.get(position);
        String url = MovieDeatiledActivity.MOVIE_TRAILER_BASE_URL + movieTrailerModel.getKey();
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(url));
    }
}
