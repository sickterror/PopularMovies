package com.timelesssoftware.popularmovies.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Luka on 11.10.2017.
 */

public class PopularMoviesDb extends SQLiteOpenHelper {

    private static final String DB_NAME = "popular_movies_db";
    private static final int DB_VERSION = 1;
    private final Context mContext;

    public PopularMoviesDb(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + PopularMoviesContract.FavoritedMovieField.TABLE_FAVORITED_MOVIES + "("
                + PopularMoviesContract.FavoritedMovieField._ID + " INTEGER NOT NULL PRIMARY KEY,"
                + PopularMoviesContract.FavoritedMovieField.id + " TEXT NOT NULL,"
                + PopularMoviesContract.FavoritedMovieField.title + " TEXT NOT NULL,"
                + PopularMoviesContract.FavoritedMovieField.overview + " TEXT,"
                + PopularMoviesContract.FavoritedMovieField.genre_ids + " TEXT,"
                + PopularMoviesContract.FavoritedMovieField.popularity + " REAL,"
                + PopularMoviesContract.FavoritedMovieField.vote_average + " REAL,"
                + PopularMoviesContract.FavoritedMovieField.vote_count + " INTEGER,"
                + PopularMoviesContract.FavoritedMovieField.backdrop_path + " TEXT,"
                + PopularMoviesContract.FavoritedMovieField.poster_path + " TEXT,"
                + PopularMoviesContract.FavoritedMovieField.original_title + " TEXT,"
                + PopularMoviesContract.FavoritedMovieField.original_language + " TEXT,"
                + PopularMoviesContract.FavoritedMovieField.release_date + " TEXT,"
                + PopularMoviesContract.FavoritedMovieField.color + " INTEGER,"
                + "UNIQUE (" + PopularMoviesContract.FavoritedMovieField.id + ") ON CONFLICT REPLACE)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public static void deleteDatabase(Context context) {
        context.deleteDatabase(DB_NAME);
    }
}
