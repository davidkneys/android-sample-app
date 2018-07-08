package com.davidkneys.bsctask.utils;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.support.v4.app.Fragment;

import com.davidkneys.bsctask.App;
import com.davidkneys.bsctask.di.ApplicationComponent;
import com.davidkneys.bsctask.ui.CommonState;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Base Fragment with handy methods supporting proper disposing of RxJava subscriptions.
 * Class has also utility methods to get common state, factory. Also provides App with injector from
 * {@link com.davidkneys.bsctask.App} class.
 */
public abstract class BaseFragment extends Fragment {

    private CompositeDisposable disposables = new CompositeDisposable();
    private CommonState commonState = null;

    @Inject
    ViewModelProvider.Factory factory;

    protected void subscribe(Disposable d) {
        disposables.add(d);
    }

    @Override
    public void onDestroyView() {
        disposables.clear();
        super.onDestroyView();
    }

    protected CommonState commonState() {
        if (commonState == null) {
            commonState = ViewModelProviders.of(getActivity()).get(CommonState.class);
        }
        return commonState;
    }

    protected ViewModelProvider.Factory factory() {
        return factory;
    }

    protected ApplicationComponent injector() {
        return ((App) getContext().getApplicationContext()).getApplicationComponent();
    }
}
