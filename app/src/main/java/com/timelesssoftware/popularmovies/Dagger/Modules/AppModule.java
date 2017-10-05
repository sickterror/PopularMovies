package com.timelesssoftware.popularmovies.Dagger.Modules;

import android.app.Application;
import android.content.Context;

import com.timelesssoftware.popularmovies.Dagger.ApplicationContext;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


/**
 * Created by lukacas on 26/07/2017.
 */

@Module
public class AppModule {
    Application mApplication;

    public AppModule(Application application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    Application providesApplication() {
        return mApplication;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return mApplication;
    }
}