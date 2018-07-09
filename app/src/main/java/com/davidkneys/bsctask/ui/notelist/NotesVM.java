package com.davidkneys.bsctask.ui.notelist;

import android.arch.lifecycle.ViewModel;

import com.davidkneys.bsctask.service.DataRepository;
import com.davidkneys.bsctask.service.SchedulerProvider;

import java.util.Collections;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Fragment providing with UI for list of all Notes.
 */
public class NotesVM extends ViewModel {

    private DataRepository repository;
    private SchedulerProvider schedulerProvider;

    @Inject
    public NotesVM(DataRepository repository, SchedulerProvider schedulerProvider) {
        this.repository = repository;
        this.schedulerProvider = schedulerProvider;
    }

    public void refresh() {
        repository.invalidate();
    }

    public Observable<NotesViewState> observeViewState() {
        return repository
                .notes()
                .observeOn(schedulerProvider.ui())
                .map(data -> new NotesViewState(false, data))
                .startWith(new NotesViewState(true, Collections.emptyList()));
    }

    /**
     * @return true if being created, false if cannot be created because of invalid title
     */
    public boolean createNote(String title) {
        if (title == null || title.isEmpty()) {
            return false;
        } else {
            repository.createNote(title);
            return true;
        }
    }

}
