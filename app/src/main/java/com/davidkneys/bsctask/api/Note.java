package com.davidkneys.bsctask.api;

/**
 * Representation of JSON Note object coming from Server.
 */
public class Note {

    public static final Note NULL_NOTE = new Note(-1, "");

    private int id;
    private String title;

    public Note(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
