package com.davidkneys.bsctask.ui.notelist;

import android.arch.lifecycle.ViewModel;

import com.davidkneys.bsctask.service.DataRepository;
import com.davidkneys.bsctask.service.SchedulerProvider;
import com.davidkneys.bsctask.ui.detail.NoteDetailVM;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Fragment providing with UI for list of all Notes.
 */
public class NotesVM extends ViewModel {

    public static class NotesListViewData {
        private boolean refreshing;
        private List<NoteDetailVM.NoteUI> data;

        public NotesListViewData(boolean refreshing, List<NoteDetailVM.NoteUI> data) {
            this.refreshing = refreshing;
            this.data = data;
        }

        public boolean isRefreshing() {
            return refreshing;
        }

        public List<NoteDetailVM.NoteUI> getData() {
            return data;
        }
    }

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

    public Observable<NotesListViewData> observeNotesListViewData() {
        return repository
                .notes()
                .observeOn(schedulerProvider.getAndroidMainThreadScheduler())
                .map(data -> new NotesListViewData(false, data))
                .startWith(new NotesListViewData(true, Collections.emptyList()));
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
