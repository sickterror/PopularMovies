package com.timelesssoftware.popularmovies.Utils.Helpers;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.timelesssoftware.popularmovies.Data.PopularMoviesContract;
import com.timelesssoftware.popularmovies.Models.MovieModel;

/**
 * Created by Luka on 11.10.2017.
 *
 * Simple helper to map Model to content values and the other way around
 */

public class ContentValuesMapper {
    public static ContentValues mapMovieModelToContentValues(MovieModel movieModel) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(PopularMoviesContract.FavoritedMovieField.id, movieModel.getId());
        contentValues.put(PopularMoviesContract.FavoritedMovieField.title, movieModel.getTitle());
        contentValues.put(PopularMoviesContract.FavoritedMovieField.vote_count, movieModel.getVote_count());
        contentValues.put(PopularMoviesContract.FavoritedMovieField.backdrop_path, movieModel.getBackdrop_path());
        contentValues.put(PopularMoviesContract.FavoritedMovieField.poster_path, movieModel.getPoster_path());
        contentValues.put(PopularMoviesContract.FavoritedMovieField.popularity, movieModel.getPopularity());
        contentValues.put(PopularMoviesContract.FavoritedMovieField.original_language, movieModel.getOriginal_language());
        contentValues.put(PopularMoviesContract.FavoritedMovieField.original_title, movieModel.getOriginal_title());
        contentValues.put(PopularMoviesContract.FavoritedMovieField.overview, movieModel.getOverview());
        contentValues.put(PopularMoviesContract.FavoritedMovieField.color, movieModel.getColor());
        return contentValues;
    }

    public static MovieModel mapCursorToMovieModel(Cursor c) {
        MovieModel movieModel = new MovieModel();
        movieModel.setId(c.getInt(c.getColumnIndex(PopularMoviesContract.FavoritedMovieField.id)));
        movieModel.setTitle(c.getString(c.getColumnIndex(PopularMoviesContract.FavoritedMovieField.title)));
        movieModel.setVote_count(c.getInt(c.getColumnIndex(PopularMoviesContract.FavoritedMovieField.vote_count)));
        movieModel.setBackdrop_path(c.getString(c.getColumnIndex(PopularMoviesContract.FavoritedMovieField.backdrop_path)));
        movieModel.setPoster_path(c.getString(c.getColumnIndex(PopularMoviesContract.FavoritedMovieField.poster_path)));
        movieModel.setPopularity(c.getInt(c.getColumnIndex(PopularMoviesContract.FavoritedMovieField.popularity)));
        movieModel.setOriginal_language(c.getString(c.getColumnIndex(PopularMoviesContract.FavoritedMovieField.original_language)));
        movieModel.setOriginal_title(c.getString(c.getColumnIndex(PopularMoviesContract.FavoritedMovieField.title)));
        movieModel.setOverview(c.getString(c.getColumnIndex(PopularMoviesContract.FavoritedMovieField.overview)));
        movieModel.setColor(c.getInt(c.getColumnIndex(PopularMoviesContract.FavoritedMovieField.color)));
        return movieModel;
    }
}
