package com.timelesssoftware.popularmovies.UI.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.timelesssoftware.popularmovies.Models.MovieTrailerModel;
import com.timelesssoftware.popularmovies.R;

import java.util.List;

/**
 * Created by Luka on 10. 10. 2017.
 */

public class MovieTrailerAdapter extends RecyclerView.Adapter<MovieTrailerAdapter.MoviewTrailerVh> {

    private List<MovieTrailerModel> movieReviewModelList;
    private OnTrailerSelectListener onTrailerSelectListener;

    public MovieTrailerAdapter(List<MovieTrailerModel> movieReviewModelList, OnTrailerSelectListener onTrailerSelectListener) {
        this.movieReviewModelList = movieReviewModelList;
        this.onTrailerSelectListener = onTrailerSelectListener;
    }

    @Override
    public MoviewTrailerVh onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.movi_trailer_view_holder, parent, false);
        return new MoviewTrailerVh(v, onTrailerSelectListener);
    }

    @Override
    public void onBindViewHolder(MoviewTrailerVh holder, int position) {
        MovieTrailerModel movieReviewModel = this.movieReviewModelList.get(position);
        holder.setmMovieTrailerTitle(movieReviewModel.getName());
    }

    @Override
    public int getItemCount() {
        return movieReviewModelList.size();
    }

    public class MoviewTrailerVh extends RecyclerView.ViewHolder implements View.OnClickListener {


        private final OnTrailerSelectListener onTrailerSelectListener;
        private final TextView mMovieTrailerTitle;
        private final ImageButton mOpenMoviteTrailer;

        public MoviewTrailerVh(View itemView, OnTrailerSelectListener onTrailerSelectListener) {
            super(itemView);
            this.onTrailerSelectListener = onTrailerSelectListener;
            itemView.setOnClickListener(this);
            mMovieTrailerTitle = itemView.findViewById(R.id.movie_trailer_title);
            mOpenMoviteTrailer = itemView.findViewById(R.id.open_movie_trailer);
            mOpenMoviteTrailer.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onTrailerSelectListener.onTrailerSelect(getAdapterPosition());
        }

        public void setmMovieTrailerTitle(String title) {
            this.mMovieTrailerTitle.setText(title);
        }
    }

    public interface OnTrailerSelectListener {
        void onTrailerSelect(int position);
    }
}
