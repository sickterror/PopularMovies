package com.timelesssoftware.popularmovies.Utils.Helpers;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.timelesssoftware.popularmovies.Models.MoviesListModel;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by Luka on 11.10.2017.
 */

public class LukaRvScrollListener {

    public LukaRvScrollListener() {
    }

    public abstract static class PageScrollListener extends RecyclerView.OnScrollListener {
        int visibleItemCount;
        private int totalItemCount;
        private int firstVisibleItem;
        private boolean loading;
        private int previousTotal = 0;
        private int visibleThreshold = 1;
        private int currentPage = 1;

        public PageScrollListener() {
        }

        public PageScrollListener(int visibleThreshold) {
            this.visibleThreshold = visibleThreshold;
        }

        public PageScrollListener(int visibleThreshold, int currentPage) {
            this.visibleThreshold = visibleThreshold;
            this.currentPage = currentPage;
        }

        public abstract void onNextPage(int page, int nextPage);

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (dy > 0) //check for scroll down
            {
                visibleItemCount = recyclerView.getChildCount();
                totalItemCount = recyclerView.getLayoutManager().getItemCount();
                firstVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();

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
                    int nextPage = currentPage + 1;
                    onNextPage(currentPage, nextPage);
                    loading = true;
                }
            }
        }
    }
}
