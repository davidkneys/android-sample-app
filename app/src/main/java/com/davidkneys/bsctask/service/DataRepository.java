package com.davidkneys.bsctask.service;

import com.davidkneys.bsctask.api.BscApi;
import com.davidkneys.bsctask.api.Note;
import com.davidkneys.bsctask.api.PutNoteRequest;
import com.davidkneys.bsctask.ui.NoteUI;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Repository used for getting data from Network Api. Repository supports trivial implementation
 * of optimistic UI and in future can implement offline first approach with the support of device
 * database.
 * <p>
 * Repository is trying to support Offline/Online synchronization, but unfortunately this is nearly
 * an impossible feature to get right without the support of a Server or some sort of queuing and
 * batching of requests. Single requests works fine, but the whole thing starts to be buggy when
 * more than 1 requests are involved at one time due to the different order of incoming responses.
 */
@Singleton
public class DataRepository {

    private Logger logger = Logger.getLogger(DataRepository.class.getName());

    private BscApi api;
    private OnlineChecker onlineChecker;

    private BehaviorSubject<List<NoteUI>> notes = BehaviorSubject.create();

    @Inject
    public DataRepository(BscApi api, OnlineChecker onlineChecker) {
        this.api = api;
        this.onlineChecker = onlineChecker;

        onlineChecker.isOnline().subscribe(online -> {
            if (online) {
                invalidate();
            }
        });
    }

    /**
     * Stream of fresh notes data. Subscribing to this Observable guarantees having the newest data possible.
     */
    public Observable<List<NoteUI>> notes() {
        return notes;
    }

    public void invalidate() {
        refreshObs()
                .subscribeOn(Schedulers.io())
                .toObservable()
                .subscribe(new DataObserver(notes, onlineChecker, logger));
    }

    private Single<List<NoteUI>> refreshObs() {
        return api
                .getNotes()
                .toObservable()
                .flatMap(Observable::fromIterable)
                .map(note -> new NoteUI(false, note))
                .toSortedList((o1, o2) -> o2.getNote().getId() - o1.getNote().getId());
    }

    public void createNote(String title) {
        final NoteUI pending = new NoteUI(true, new Note((notes.hasValue() ? notes.getValue().size() : 0) + 1000, title));

        List<NoteUI> dirty;
        if (notes.hasValue()) {
            dirty = new ArrayList<>(notes.getValue());
        } else {
            dirty = new ArrayList<>();
        }
        dirty.add(0, pending);

        api
                .createNote(new PutNoteRequest(title))
                .flatMap(note -> refreshObs())
                .toObservable()
                .startWith(dirty)
                .subscribeOn(Schedulers.io())
                .subscribe(new DataObserver(notes, onlineChecker, logger));
    }


    public void removeNote(Note note) {
        List<NoteUI> dirtyNotes = Observable
                .fromIterable(notes.getValue())
                .map(noteUI -> {
                    if (noteUI.getNote().getId() == note.getId()) {
                        return new NoteUI(true, noteUI.getNote());
                    } else {
                        return noteUI;
                    }
                }).toList().blockingGet();

        api
                .removeNote(note.getId())
                .flatMap(o -> refreshObs())
                .toObservable()
                .startWith(dirtyNotes)
                .subscribeOn(Schedulers.io())
                .subscribe(new DataObserver(notes, onlineChecker, logger));
    }

    public void updateNote(Note note, String newTitle) {
        List<NoteUI> dirtyNotes = Observable
                .fromIterable(notes.getValue())
                .map(noteUI -> {
                    if (noteUI.getNote().getId() == note.getId()) {
                        return new NoteUI(true, new Note(noteUI.getNote().getId(), newTitle));
                    } else {
                        return noteUI;
                    }
                }).toList().blockingGet();

        api
                .updateNote(note.getId(), new PutNoteRequest(newTitle))
                .flatMap(data -> refreshObs())
                .toObservable()
                .startWith(dirtyNotes)
                .subscribeOn(Schedulers.io())
                .subscribe(new DataObserver(notes, onlineChecker, logger));
    }

    public Single<Note> getNote(Note selectedNote) {
        return api
                .getNote(selectedNote.getId())
                .subscribeOn(Schedulers.io());
    }

    private static class DataObserver implements Observer<List<NoteUI>> {

        private BehaviorSubject<List<NoteUI>> source;
        private OnlineChecker onlineChecker;
        private Logger logger;

        public DataObserver(BehaviorSubject<List<NoteUI>> source, OnlineChecker onlineChecker, Logger logger) {
            this.source = source;
            this.onlineChecker = onlineChecker;
            this.logger = logger;
        }

        @Override
        public void onSubscribe(Disposable d) {

        }

        @Override
        public void onNext(List<NoteUI> noteUIS) {
            source.onNext(noteUIS);
        }

        @Override
        public void onError(Throwable e) {
            logger.log(Level.SEVERE, "Network error", e);
            onlineChecker.setOnline(false);
        }

        @Override
        public void onComplete() {
            onlineChecker.setOnline(true);
        }
    }
}
