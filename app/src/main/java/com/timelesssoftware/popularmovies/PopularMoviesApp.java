package com.timelesssoftware.popularmovies;

import android.app.Application;

import com.timelesssoftware.popularmovies.Dagger.Components.DaggerNetComponent;
import com.timelesssoftware.popularmovies.Dagger.Components.NetComponent;
import com.timelesssoftware.popularmovies.Dagger.Modules.AppModule;
import com.timelesssoftware.popularmovies.Dagger.Modules.NetModule;

/**
 * Created by Luka on 3. 10. 2017.
 */

public class PopularMoviesApp  extends Application {
    private static NetComponent mNetComponent;


    @Override
    public void onCreate() {
        super.onCreate();
        // Dagger%COMPONENT_NAME%
        mNetComponent = DaggerNetComponent.builder()
                // list of modules that are part of this component need to be created here too
                .appModule(new AppModule(this)) // This also corresponds to the name of your module: %component_name%Module
                .netModule(new NetModule())
                .build();
        // If a Dagger 2 component does not have any constructor arguments for any of its modules,
        // then we can use .create() as a shortcut instead:
        //  mNetComponent = com.codepath.dagger.components.DaggerNetComponent.create();
    }

    public static NetComponent getNetComponent() {
        return mNetComponent;
    }
}
