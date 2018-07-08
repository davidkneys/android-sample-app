package com.davidkneys.bsctask.di;

import com.davidkneys.bsctask.App;
import com.davidkneys.bsctask.service.OnlineChecker;
import com.davidkneys.bsctask.service.OnlineCheckerImpl;
import com.davidkneys.bsctask.service.SchedulerProvider;
import com.davidkneys.bsctask.service.SchedulerProviderImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.android.schedulers.AndroidSchedulers;

@Module
public class ApplicationModule {

    private App application;

    public ApplicationModule(App application) {
        this.application = application;
    }

    @Singleton
    @Provides
    App providesApplication() {
        return application;
    }

    @Singleton
    @Provides
    OnlineChecker providesOnlineChecker() {
        return new OnlineCheckerImpl();
    }


    @Singleton
    @Provides
    SchedulerProvider providesSchedulerProvider() {
        return new SchedulerProviderImpl(AndroidSchedulers.mainThread());
    }

}
