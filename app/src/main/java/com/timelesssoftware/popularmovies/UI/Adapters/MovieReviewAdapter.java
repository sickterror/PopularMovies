package com.timelesssoftware.popularmovies.UI.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.timelesssoftware.popularmovies.Models.MovieReviewModel;
import com.timelesssoftware.popularmovies.R;

import java.util.List;

/**
 * Created by Luka on 10. 10. 2017.
 */

public class MovieReviewAdapter extends RecyclerView.Adapter<MovieReviewAdapter.MoviewReveiewVh> {

    private List<MovieReviewModel> movieReviewModelList;
    private OnReviewSelectListener onReviewSelectListener;

    public MovieReviewAdapter(List<MovieReviewModel> movieReviewModelList, OnReviewSelectListener onReviewSelectListener) {
        this.movieReviewModelList = movieReviewModelList;
        this.onReviewSelectListener = onReviewSelectListener;
    }

    @Override
    public MoviewReveiewVh onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_review_vh, parent, false);
        return new MoviewReveiewVh(v, onReviewSelectListener);
    }

    @Override
    public void onBindViewHolder(MoviewReveiewVh holder, int position) {
        MovieReviewModel movieReviewModel = this.movieReviewModelList.get(position);
        holder.setMovieReviewModel(movieReviewModel);
        holder.setMovieReviewSubject(movieReviewModel.getAuthor());
        holder.setMovieAuthorFirstLetter(movieReviewModel.getAuthor());
        holder.setMovieReview(movieReviewModel.getContent());
    }

    @Override
    public int getItemCount() {
        return movieReviewModelList.size();
    }

    public class MoviewReveiewVh extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView mMovieReviewSubject;
        private final ImageView mMovieAuthorIv;
        private final TextView mMovieReviewTv;
        private final ImageButton mOpenMovieReviewWeb;
        private OnReviewSelectListener onReviewSelectListener;
        public MovieReviewModel movieReviewModel;

        public MoviewReveiewVh(View itemView, OnReviewSelectListener onReviewSelectListener) {
            super(itemView);
            mMovieReviewSubject = itemView.findViewById(R.id.movie_review_subject);
            mMovieAuthorIv = itemView.findViewById(R.id.movie_review_author_iv);
            mMovieReviewTv = itemView.findViewById(R.id.movie_review_review);
            mOpenMovieReviewWeb = itemView.findViewById(R.id.open_review_link);
            mOpenMovieReviewWeb.setOnClickListener(this);
            this.onReviewSelectListener = onReviewSelectListener;
        }

        public void setMovieReviewSubject(String text) {
            this.mMovieReviewSubject.setText(text);
        }

        public void setMovieAuthorFirstLetter(String author) {
            ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
            int color = generator.getColor(author);
            TextDrawable.IBuilder builder = TextDrawable.builder()
                    .beginConfig()
                    .withBorder(4)
                    .endConfig()
                    .round();
            mMovieAuthorIv.setImageDrawable(builder.build(String.valueOf(author.charAt(0)), color));
        }

        public void setMovieReview(String text) {
            mMovieReviewTv.setText(text);
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.open_review_link) {
                onReviewSelectListener.openReview(movieReviewModel.getUrl());
            }
        }

        public MovieReviewModel getMovieReviewModel() {
            return movieReviewModel;
        }

        public void setMovieReviewModel(MovieReviewModel movieReviewModel) {
            this.movieReviewModel = movieReviewModel;
        }
    }

    public interface OnReviewSelectListener {
        void openReview(String url);
    }
}
