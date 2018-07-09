package com.davidkneys.bsctask.ui.notelist;

import com.davidkneys.bsctask.ui.NoteUI;

import java.util.List;

public class NotesViewState {
    private final boolean refreshing;
    private final List<NoteUI> data;

    public NotesViewState(boolean refreshing, List<NoteUI> data) {
        this.refreshing = refreshing;
        this.data = data;
    }

    public boolean isRefreshing() {
        return refreshing;
    }

    public List<NoteUI> getData() {
        return data;
    }
}
