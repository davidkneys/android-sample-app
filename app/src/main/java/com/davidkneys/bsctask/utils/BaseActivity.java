package com.davidkneys.bsctask.utils;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.davidkneys.bsctask.App;
import com.davidkneys.bsctask.di.ApplicationComponent;
import com.davidkneys.bsctask.ui.CommonState;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Base Activity with handy methods supporting proper disposing of RxJava subscriptions.
 * Class has also utility methods to get common state, factory. Also provides App with injector from
 * {@link com.davidkneys.bsctask.App} class.
 */
public abstract class BaseActivity extends AppCompatActivity {

    private CompositeDisposable disposables = new CompositeDisposable();
    private CommonState commonState = null;

    @Inject
    ViewModelProvider.Factory factory;

    protected void subscribe(Disposable d) {
        disposables.add(d);
    }

    protected CommonState commonState() {
        if (commonState == null) {
            commonState = ViewModelProviders.of(this, factory).get(CommonState.class);
        }
        return commonState;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleManager.wrap(newBase));
    }

    protected ViewModelProvider.Factory factory() {
        return factory;
    }

    @Override
    protected void onDestroy() {
        disposables.clear();
        super.onDestroy();
    }

    protected ApplicationComponent injector() {
        return ((App) getApplicationContext()).getApplicationComponent();
    }
}
