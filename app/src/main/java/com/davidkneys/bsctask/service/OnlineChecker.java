package com.davidkneys.bsctask.service;

import io.reactivex.Observable;

/**
 * Service providing stream responsible for announcing of changes in Online/Offline state.
 */
public interface OnlineChecker {

    Observable<Boolean> isOnline();

    void setOnline(boolean online);
}
