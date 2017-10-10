package com.timelesssoftware.popularmovies.UI.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.timelesssoftware.popularmovies.R;

/**
 * Created by Luka on 10. 10. 2017.
 */

public class MovieReviewAdapter extends RecyclerView.Adapter<MovieReviewAdapter.MoviewReveiewVh> {
    @Override
    public MoviewReveiewVh onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_review_vh, parent, false);
        return new MoviewReveiewVh(v);
    }

    @Override
    public void onBindViewHolder(MoviewReveiewVh holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class MoviewReveiewVh extends RecyclerView.ViewHolder {
        public MoviewReveiewVh(View itemView) {
            super(itemView);
        }
    }
}
