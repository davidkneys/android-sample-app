package com.davidkneys.bsctask.ui;

import android.arch.lifecycle.ViewModel;

import com.davidkneys.bsctask.api.Note;
import com.davidkneys.bsctask.service.OnlineChecker;

import java.util.Stack;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Common state shared in Activity scope used for communication with Activity Fragments. Holding also
 * trivial implementation of Activity Navigation supporting back stack navigation.
 *
 * @see <a href="https://developer.android.com/jetpack/">Android Jetpack</a>
 */
public class CommonState extends ViewModel {

    private OnlineChecker onlineChecker;

    public enum Screen {
        MAIN, DETAIL
    }

    private Stack<Screen> screenStack = new Stack<>();

    @Inject
    public CommonState(OnlineChecker onlineChecker) {
        this.onlineChecker = onlineChecker;

        screenStack.push(Screen.MAIN);
        this.currentScreen.onNext(screenStack.peek());
    }

    private BehaviorSubject<Note> selectedNote = BehaviorSubject.create();
    private BehaviorSubject<Screen> currentScreen = BehaviorSubject.create();

    public Observable<Boolean> observeOnlineState() {
        return onlineChecker.isOnline();
    }

    public void refreshData() {
        onlineChecker.setOnline(true);
    }

    /**
     * Handler of back press.
     *
     * @return false when back press should be left on the Android Framework, true if back press
     * is handled by the Application
     */
    public boolean backPressed() {
        screenStack.pop();
        if (screenStack.empty()) {
            return false;
        } else {
            this.currentScreen.onNext(screenStack.peek());
            return true;
        }
    }

    public Observable<Note> observeSelectedNote() {
        return selectedNote.observeOn(AndroidSchedulers.mainThread());
    }

    public void select(Note note) {
        this.selectedNote.onNext(note);
        this.screenStack.push(Screen.DETAIL);
        this.currentScreen.onNext(screenStack.peek());
    }

    public void clearSelection() {
        this.screenStack.pop();
        this.currentScreen.onNext(screenStack.peek());
        this.selectedNote.onNext(Note.NULL_NOTE);
    }

    public Observable<Screen> currentScreen() {
        return currentScreen;
    }

}
