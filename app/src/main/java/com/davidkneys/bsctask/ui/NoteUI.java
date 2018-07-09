package com.davidkneys.bsctask.ui;

import com.davidkneys.bsctask.api.Note;

public final class NoteUI {
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
