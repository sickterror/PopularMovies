package com.timelesssoftware.popularmovies.Data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.Arrays;

import static com.timelesssoftware.popularmovies.Data.PopularMoviesContract.CONTENT_AUTHORITY;

/**
 * Created by Luka on 11.10.2017.
 */

public class PopularMoviesProvider extends ContentProvider {
    private static final int MOVIES = 100;
    private static final int MOVIES_WITH_ID = 200;
    private PopularMoviesDb mDatabase;


    @Override
    public boolean onCreate() {
        mDatabase = new PopularMoviesDb(getContext());
        return true;
    }


    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = CONTENT_AUTHORITY;
        matcher.addURI(authority, "movies", MOVIES);
        matcher.addURI(authority, "movies/#", MOVIES_WITH_ID);
        return matcher;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase db = mDatabase.getReadableDatabase();
        switch (buildUriMatcher().match(uri)) {
            case MOVIES:
                Cursor cursor = db.query(
                        PopularMoviesContract.FavoritedMovieField.TABLE_FAVORITED_MOVIES,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                Context context = getContext();
                if (null != context) {
                    cursor.setNotificationUri(context.getContentResolver(), uri);
                }
                return cursor;
            case MOVIES_WITH_ID:
                break;
        }
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        SQLiteDatabase db = mDatabase.getWritableDatabase();
        db.insert(PopularMoviesContract.FavoritedMovieField.TABLE_FAVORITED_MOVIES, null, contentValues);
        return PopularMoviesContract.FavoritedMovieField.CONTENT_URI.buildUpon().appendPath(contentValues.getAsString(PopularMoviesContract.FavoritedMovieField.id)).build();
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
