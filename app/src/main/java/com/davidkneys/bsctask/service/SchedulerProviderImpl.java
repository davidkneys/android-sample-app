package com.davidkneys.bsctask.service;

import io.reactivex.Scheduler;

public class SchedulerProviderImpl implements SchedulerProvider {

    private Scheduler androidMainThreadScheduler;

    public SchedulerProviderImpl(Scheduler androidMainThreadScheduler) {
        this.androidMainThreadScheduler = androidMainThreadScheduler;
    }

    @Override
    public Scheduler getAndroidMainThreadScheduler() {
        return androidMainThreadScheduler;
    }
}
