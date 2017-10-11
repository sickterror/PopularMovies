package com.timelesssoftware.popularmovies.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.timelesssoftware.popularmovies.Dagger.ApplicationContext;
import com.timelesssoftware.popularmovies.Models.MovieModel;
import com.timelesssoftware.popularmovies.Utils.Helpers.ContentValuesMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Luka on 11. 10. 2017.
 */

public class PopularMoviesHelper {
    private Context context;

    public PopularMoviesHelper(@ApplicationContext Context context) {
        this.context = context;
    }

    public List<MovieModel> getFavoritedMoves() {
        List<MovieModel> list = new ArrayList<>();
        Cursor cursor = context.getContentResolver().query(PopularMoviesContract.FavoritedMovieField.CONTENT_URI, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                MovieModel movieModel = ContentValuesMapper.mapCursorToMovieModel(cursor);
                list.add(movieModel);
            }
            cursor.close();
        }

        return list;
    }

    public MovieModel isMovieFavorited(String movieId) {
        Uri uri = PopularMoviesContract.FavoritedMovieField.CONTENT_URI.buildUpon().appendPath(movieId).build();
        Cursor ca = context.getContentResolver().query(uri, null, PopularMoviesContract.FavoritedMovieField.id + " = ?", new String[]{movieId}, null);
        if (ca != null) {
            while (ca.moveToNext()) {
                MovieModel model = ContentValuesMapper.mapCursorToMovieModel(ca);
                return model;
            }
        }
        return null;
    }

    public void markMovieAsFavorited(MovieModel movieModel) {
        ContentValues contentValues = ContentValuesMapper.mapMovieModelToContentValues(movieModel);
        context.getContentResolver().insert(PopularMoviesContract.FavoritedMovieField.CONTENT_URI,
                contentValues);
    }

    public void removeMovieModel(String movieId) {
        Uri uri = PopularMoviesContract.FavoritedMovieField.CONTENT_URI.buildUpon().appendPath(movieId).build();
        context.getContentResolver().delete(uri, PopularMoviesContract.FavoritedMovieField.id + " = ?", new String[]{movieId});
    }

}
