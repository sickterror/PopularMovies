package com.timelesssoftware.popularmovies.Utils.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.timelesssoftware.popularmovies.Models.MovieModel;
import com.timelesssoftware.popularmovies.R;
import com.timelesssoftware.popularmovies.Utils.Network.ImageHelper;
import com.timelesssoftware.popularmovies.Utils.Pallete.GlideApp;

import java.util.List;

/**
 * Created by Luka on 3. 10. 2017.
 */

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieListViewHolder> {

    private OnMovieSelectListener mOnMovieSelectListener;
    private List<MovieModel> movieModelList;
    private Context mContext;
    private int lastPosition = -1;

    public MovieListAdapter(List<MovieModel> movieModelList, Context mContext) {
        this.movieModelList = movieModelList;
        this.mContext = mContext;
    }

    public void setmOnMovieSelectListener(OnMovieSelectListener onMovieSelectListener) {
        this.mOnMovieSelectListener = onMovieSelectListener;
    }

    @Override
    public MovieListAdapter.MovieListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_view_holder, parent, false);
        return new MovieListViewHolder(v, mOnMovieSelectListener);
    }

    @Override
    public void onBindViewHolder(final MovieListAdapter.MovieListViewHolder holder, int position) {
        final MovieModel movieModel = this.movieModelList.get(position);
        String url = ImageHelper.generateImageUrl(movieModel.poster_path, ImageHelper.ImageSizes.w500);
        holder.movieTitleTv.setText(movieModel.getTitle());
        //Comment out for udacity
        /*GlideApp.with(mContext).asBitmap()
                .load(url).diskCacheStrategy(DiskCacheStrategy.ALL).
                into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        holder.movieImageIv.setImageBitmap(resource);
                        extractColor(resource);
                    }

                    private void extractColor(Bitmap b) {
                        int color;
                        if (movieModel.getColor() != 0) {
                            Log.d("MovieListAdapter", "got color from cache");
                            color = movieModel.getColor();
                        } else {
                            Log.d("MovieListAdapter", "genereateing color");
                            Palette p = Palette.from(b).generate();
                            color = p.getDarkVibrantColor(mContext.getResources().getColor(R.color.md_white_1000));
                            movieModel.setColor(color);
                        }
                        holder.movieInfoHolder.setBackgroundColor(color);
                        if (color == mContext.getResources().getColor(R.color.md_white_1000)) {
                            holder.movieTitleTv.setTextColor(Color.BLACK);
                        }else{
                            holder.movieTitleTv.setTextColor(Color.WHITE);
                        }
                    }
                });*/

        Glide.with(mContext).load(url).into(holder.movieImageIv);
        holder.movieTitleTv.setTextColor(Color.BLACK);
        setAnimation(holder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return this.movieModelList.size();
    }

    public class MovieListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final FrameLayout movieInfoHolder;
        public final ImageView movieImageIv;
        private OnMovieSelectListener onMovieSelectListener;
        public TextView movieTitleTv;

        public MovieListViewHolder(View itemView, OnMovieSelectListener onMovieSelectListener) {
            super(itemView);
            movieImageIv = itemView.findViewById(R.id.movie_poster);
            this.onMovieSelectListener = onMovieSelectListener;
            movieInfoHolder = itemView.findViewById(R.id.movie_title);
            movieTitleTv = itemView.findViewById(R.id.movie_view_holder_title);
            this.itemView.setOnClickListener(this);
        }

        public void clearAnimation() {
            itemView.clearAnimation();
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.movie_view_holder) {
                this.onMovieSelectListener.onSelectMovie(getAdapterPosition());
                this.onMovieSelectListener.onSelectMovieWithTransition(getAdapterPosition(), movieImageIv);
            }
        }
    }

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(viewToAnimate.getContext(), R.anim.slide_bottom_top);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    public interface OnMovieSelectListener {
        void onSelectMovie(int position);

        void onSelectMovieWithTransition(int position, ImageView imageView);
    }

    @Override
    public void onViewDetachedFromWindow(final MovieListAdapter.MovieListViewHolder holder) {
        holder.clearAnimation();
    }
}
