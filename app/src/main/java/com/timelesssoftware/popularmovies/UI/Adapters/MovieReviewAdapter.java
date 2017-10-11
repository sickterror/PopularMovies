package com.timelesssoftware.popularmovies.UI.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    public MovieReviewAdapter(List<MovieReviewModel> movieReviewModelList) {
        this.movieReviewModelList = movieReviewModelList;
    }

    @Override
    public MoviewReveiewVh onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_review_vh, parent, false);
        return new MoviewReveiewVh(v);
    }

    @Override
    public void onBindViewHolder(MoviewReveiewVh holder, int position) {
        MovieReviewModel movieReviewModel = this.movieReviewModelList.get(position);
        holder.setMovieReviewSubject(movieReviewModel.getAuthor());
        holder.setMovieAuthorFirstLetter(movieReviewModel.getAuthor());
        holder.setMovieReview(movieReviewModel.getContent());
    }

    @Override
    public int getItemCount() {
        return movieReviewModelList.size();
    }

    public class MoviewReveiewVh extends RecyclerView.ViewHolder {

        private final TextView mMovieReviewSubject;
        private final ImageView mMovieAuthorIv;
        private final TextView mMovieReviewTv;


        public MoviewReveiewVh(View itemView) {
            super(itemView);
            mMovieReviewSubject = itemView.findViewById(R.id.movie_review_subject);
            mMovieAuthorIv = itemView.findViewById(R.id.movie_review_author_iv);
            mMovieReviewTv = itemView.findViewById(R.id.movie_review_review);
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
    }
}
