package com.timelesssoftware.popularmovies.UI.Activities;

import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.timelesssoftware.popularmovies.Models.MovieModel;
import com.timelesssoftware.popularmovies.R;
import com.timelesssoftware.popularmovies.Utils.Network.ImageHelper;


public class MovieDeatiledActivity extends AppCompatActivity {


    private ImageView moviePosterIv;
    private ImageView movieBackgrdopIv;

    private TextView movieTitleTv;
    private TextView movieReleaseDateTv;
    private TextView movieScoreTv;
    private TextView movieDescriptionTv;
    private MovieModel movieModel;
    private static int mInOrient = -1;

    private boolean shouldCancelTransition = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        populateFields();
        mInOrient = (mInOrient == -1) ? getResources().getConfiguration().orientation : mInOrient;
        animate();
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

        Glide.with(this).load(ImageHelper.generateImageUrl(movieModel.backdrop_path, ImageHelper.ImageSizes.w500)).into(movieBackgrdopIv);
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
}
