package com.timelesssoftware.popularmovies.Dagger.Components;

import android.content.Context;

import com.timelesssoftware.popularmovies.Dagger.ApplicationContext;
import com.timelesssoftware.popularmovies.Dagger.Modules.AppModule;
import com.timelesssoftware.popularmovies.Dagger.Modules.NetModule;
import com.timelesssoftware.popularmovies.MainActivity;
import com.timelesssoftware.popularmovies.UI.Fragments.MostViewdMoviesFragment;
import com.timelesssoftware.popularmovies.Utils.Network.ApiHandler;
;

import javax.inject.Singleton;

import dagger.Component;


/**
 * Created by lukacas on 26/07/2017.
 */


@Singleton
@Component(modules = {AppModule.class, NetModule.class})
public abstract class NetComponent {
    public abstract void inject(ApiHandler apiHandler);

    //void inject(AllPosLocationsViewModel allPosLocationsViewModel);
    @ApplicationContext
    public abstract Context getContext();

    public abstract void inject(MainActivity mainActivity);


    public abstract void inject(MostViewdMoviesFragment mostViewdMoviesFragment);
}


