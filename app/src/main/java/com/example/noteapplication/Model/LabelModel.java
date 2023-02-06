package com.example.noteapplication.Model;

public class LabelModel {
    private int id;
    private String labelName;
    private String dateCreated;

    public LabelModel() {
    }

    public LabelModel(int id, String labelName, String dateCreated) {
        this.id = id;
        this.labelName = labelName;
        this.dateCreated = dateCreated;
    }

    public int getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
    }

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    @Override
    public String toString() {
        return "LabelModel{" +
                "id=" + id +
                ", labelName='" + labelName + '\'' +
                ", dateCreated='" + dateCreated + '\'' +
                '}';
    }
}
