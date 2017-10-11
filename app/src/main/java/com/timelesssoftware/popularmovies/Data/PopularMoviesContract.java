package com.timelesssoftware.popularmovies.Data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Luka on 11.10.2017.
 */

public class PopularMoviesContract {
    public static final String CONTENT_AUTHORITY = "com.timelesssoftware.popularmovies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);


    public static final class FavoritedMovieField implements BaseColumns {
        // table name
        public static final String TABLE_FAVORITED_MOVIES = "favorited_movie";
        // columns

        public static final String id = "id";
        public static final String vote_count = "vote_count";
        public static final String video = "video";
        public static final String vote_average = "vote_average";
        public static final String title = "title";
        public static final String popularity = "popularity";
        public static final String poster_path = "poster_path";
        public static final String original_language = "original_language";
        public static final String original_title = "original_title";
        public static final String genre_ids = "genre_ids";
        public static final String backdrop_path = "backdrop_path";
        public static final String adult = "adult";
        public static final String overview = "overview";
        public static final String release_date = "release_date";
        public static final String color = "color";

        // create content uri
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(TABLE_FAVORITED_MOVIES).build();
        // create cursor of base type directory for multiple entries
        public static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_FAVORITED_MOVIES;
        // create cursor of base type item for single entry
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_FAVORITED_MOVIES;

        // for building URIs on insertion
        public static Uri buildFlavorsUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
