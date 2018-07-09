package com.davidkneys.bsctask.ui.detail;

public class NoteDetailViewState {
    private final String title;
    private final boolean loading;

    public NoteDetailViewState(String title, boolean loading) {
        this.title = title;
        this.loading = loading;
    }

    public String getTitle() {
        return title;
    }

    public boolean isLoading() {
        return loading;
    }
}
