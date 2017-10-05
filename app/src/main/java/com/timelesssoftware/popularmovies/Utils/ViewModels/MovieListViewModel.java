package com.timelesssoftware.popularmovies.Utils.ViewModels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.timelesssoftware.popularmovies.Models.MovieModel;
import com.timelesssoftware.popularmovies.Models.MoviesListModel;
import com.timelesssoftware.popularmovies.PopularMoviesApp;
import com.timelesssoftware.popularmovies.Utils.Network.ApiHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Luka on 3. 10. 2017.
 */

public class MovieListViewModel extends ViewModel {

    @Inject
    ApiHandler apiHandler;

    public MovieListViewModel() {
        PopularMoviesApp.getNetComponent().inject(this);
    }


    MutableLiveData<List<MovieModel>> listMutableLiveData = new MutableLiveData<>();

    public MutableLiveData<List<MovieModel>> getListMutableLiveData() {
        return listMutableLiveData;
    }

    public void loadMovieListData(String method, int page, String sortOrder) {
        if (method == null)
            throw new IllegalArgumentException("getMovieListData method paramater must not be null");
        String _page = Integer.toString(page);
        HashMap<String, String> params = new HashMap<>();
        params.put("sort_by", sortOrder);
        params.put("page", _page);

        apiHandler.getJsonObject(method, params, MoviesListModel.class, new ApiHandler.ApiHandlerJsonObjectListener() {
            @Override
            public <T> void onSuccesJsonObject(T object) {
                try {
                    final MoviesListModel moviesListModel = (MoviesListModel) object;
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            listMutableLiveData.setValue(moviesListModel.results);
                        }
                    });
                } catch (ClassCastException e) {
                    Log.e("MovieListViewModel", e.getMessage());
                }
            }

            @Override
            public void onFailure(IOException e) {

            }

            @Override
            public void onError(int statusCode) {

            }
        });
    }
}
