package com.davidkneys.bsctask;

import android.app.Application;
import android.content.Context;

import com.davidkneys.bsctask.di.ApplicationComponent;
import com.davidkneys.bsctask.di.ApplicationModule;
import com.davidkneys.bsctask.di.DaggerApplicationComponent;

/**
 * Application entry point. Holding DI ApplicationComponent for later injection of service
 * components.
 */
public class App extends Application {

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationComponent = DaggerApplicationComponent.builder().applicationModule(new ApplicationModule(this)).build();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }
}
