package com.davidkneys.bsctask.service;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

@Singleton
public class OnlineCheckerImpl implements OnlineChecker {

    private BehaviorSubject<Boolean> onlineStream = BehaviorSubject.createDefault(true);

    @Inject
    public OnlineCheckerImpl() {
    }

    @Override
    public Observable<Boolean> isOnline() {
        return onlineStream;
    }

    @Override
    public void setOnline(boolean online) {
        if (onlineStream.getValue() != online) {
            onlineStream.onNext(online);
        }
    }
}
