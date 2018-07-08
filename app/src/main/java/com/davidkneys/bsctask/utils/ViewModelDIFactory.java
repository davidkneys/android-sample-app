package com.davidkneys.bsctask.utils;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Custom factory used for the construction of ViewModels. It is using injected ViewModels defined in
 * {@link com.davidkneys.bsctask.di.ViewModelModule}
 */
public class ViewModelDIFactory implements ViewModelProvider.Factory {

    private final Map<Class<? extends ViewModel>, Provider<ViewModel>> vms;

    @Inject
    public ViewModelDIFactory(Map<Class<? extends ViewModel>, Provider<ViewModel>> vms) {
        this.vms = vms;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        try {
            return (T) vms.get(modelClass).get();
        } catch (Exception ex) {
            throw new RuntimeException("Did you forget to register VM in DI container?", ex);
        }
    }
}
