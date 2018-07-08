package com.davidkneys.bsctask.api;

/**
 * Representation of JSON Request object used for creation or update of a note.
 */
public class PutNoteRequest {

    private String title;

    public PutNoteRequest(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
