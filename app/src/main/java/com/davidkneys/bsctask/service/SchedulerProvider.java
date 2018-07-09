package com.davidkneys.bsctask.service;

import io.reactivex.Scheduler;

/**
 * Helper provider of RxJava schedulers.
 */
public interface SchedulerProvider {
    Scheduler ui();
}
