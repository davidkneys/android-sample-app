package com.davidkneys.bsctask.ui.detail;

import android.arch.lifecycle.ViewModel;

import com.davidkneys.bsctask.api.Note;
import com.davidkneys.bsctask.service.DataRepository;
import com.davidkneys.bsctask.service.OnlineChecker;
import com.davidkneys.bsctask.service.SchedulerProvider;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.subjects.BehaviorSubject;

public class NoteDetailVM extends ViewModel {

    private Logger logger = Logger.getLogger(NoteDetailVM.class.getName());

    private Disposable fetchingNote = Disposables.empty();
    private DataRepository repository;
    private OnlineChecker onlineChecker;
    private SchedulerProvider schedulerProvider;

    private Disposable checkerDisposable = Disposables.empty();

    private BehaviorSubject<NoteUI> selectedNote = BehaviorSubject.create();

    @Inject
    public NoteDetailVM(DataRepository repository, OnlineChecker onlineChecker, SchedulerProvider schedulerProvider) {
        this.repository = repository;
        this.onlineChecker = onlineChecker;
        this.schedulerProvider = schedulerProvider;
    }

    public Observable<NoteUI> observeSelectedNote() {
        return selectedNote.observeOn(schedulerProvider.getAndroidMainThreadScheduler());
    }

    public void fetchNote(Note note) {
        fetchingNote.dispose();
        fetchingNote = repository
                .getNote(note)
                .flatMap(data -> Single.just(new NoteUI(false, data)))
                .toObservable()
                .startWith(new NoteUI(true, note))
                .observeOn(schedulerProvider.getAndroidMainThreadScheduler())
                .subscribe(data -> {
                    selectedNote.onNext(data);
                }, throwable -> {
                    logger.log(Level.SEVERE, "", throwable);
                    onlineChecker.setOnline(false);
                }, () -> {
                    onlineChecker.setOnline(true);
                });
    }

    public void selectNote(Note note) {
        checkerDisposable = onlineChecker
                .isOnline()
                .subscribe(o -> fetchNote(note));
    }

    public void delete() {
        repository.removeNote(selectedNote.getValue().note);
    }

    public void update(String newTitle) {
        repository.updateNote(selectedNote.getValue().note, newTitle);
    }

    @Override
    protected void onCleared() {
        fetchingNote.dispose();
        checkerDisposable.dispose();
        super.onCleared();
    }

    public static final class NoteUI {
        private final boolean dirty;
        private final Note note;

        public NoteUI(boolean dirty, Note note) {
            this.dirty = dirty;
            this.note = note;
        }

        public boolean isDirty() {
            return dirty;
        }

        public Note getNote() {
            return note;
        }
    }
}
