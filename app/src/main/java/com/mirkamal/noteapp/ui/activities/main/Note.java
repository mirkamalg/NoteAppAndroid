package com.mirkamal.noteapp.ui.activities.main;

public class Note {

    private String header, body, editedDate, editedTime;

    public Note(String header, String body, String editedDate, String editedTime) {
        this.header = header;
        this.body = body;
        this.editedDate = editedDate;
        this.editedTime = editedTime;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getEditedDate() {
        return editedDate;
    }

    public void setEditedDate(String editedDate) {
        this.editedDate = editedDate;
    }

    public String getEditedTime() {
        return editedTime;
    }

    public void setEditedTime(String editedTime) {
        this.editedTime = editedTime;
    }
}
