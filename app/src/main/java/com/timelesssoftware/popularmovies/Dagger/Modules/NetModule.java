package com.timelesssoftware.popularmovies.Dagger.Modules;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.timelesssoftware.popularmovies.Dagger.ApplicationContext;
import com.timelesssoftware.popularmovies.Data.PopularMoviesHelper;
import com.timelesssoftware.popularmovies.Data.PopularMoviesProvider;
import com.timelesssoftware.popularmovies.Utils.Network.ApiHandler;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;


/**
 * Created by lukacas on 26/07/2017.
 */

@Module
public class NetModule {
    // Dagger will only look for methods annotated with @Provides
    @Provides
    @Singleton
    // Application reference must come from AppModule.class
    SharedPreferences providesSharedPreferences(Application application) {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

    @Provides
    @Singleton
    Cache provideOkHttpCache(Application application) {
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        Cache cache = new Cache(application.getCacheDir(), cacheSize);
        return cache;
    }

    @Provides
    @Singleton
    Gson provideGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        //gsonBuilder.setFieldNamingPolicy();
        return gsonBuilder.create();
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(Cache cache) {
        return new OkHttpClient.Builder()
                .cache(cache)
                .build();
    }

    @Provides
    @Singleton
    ApiHandler provideApiHandler(OkHttpClient okHttpClient, Gson mGson, SharedPreferences mSharedPrefernces) {
        return new ApiHandler(okHttpClient, mGson, mSharedPrefernces);
    }

    @Provides
    PopularMoviesHelper popularMoviesProvider(@ApplicationContext Context context){
        return  new PopularMoviesHelper(context);
    }
}
