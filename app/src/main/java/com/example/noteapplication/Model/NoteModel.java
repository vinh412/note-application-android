package com.example.noteapplication.Model;

import java.io.Serializable;

public class NoteModel implements Serializable {
    private int id;
    private int isPinned;
    private String header;
    private String content;
    private String dateCreated;
    private String lastModified;

    public NoteModel(int id, int isPinned, String header, String content, String dateCreated, String lastModified) {
        this.id = id;
        this.isPinned = isPinned;
        this.header = header;
        this.content = content;
        this.dateCreated = dateCreated;
        this.lastModified = lastModified;
    }

    public NoteModel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public int isPinned() {
        return isPinned;
    }

    public void setPinned(int pinned) {
        isPinned = pinned;
    }

    @Override
    public String toString() {
        return "NoteModel{" +
                "id=" + id +
                ", isPinned=" + isPinned +
                ", header='" + header + '\'' +
                ", content='" + content + '\'' +
                ", dateCreated='" + dateCreated + '\'' +
                ", lastModified='" + lastModified + '\'' +
                '}';
    }
}
