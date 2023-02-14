package com.example.noteapplication;

import com.example.noteapplication.Model.LabelModel;

public interface LabelItemClickListener {
    void onDeleteClick(LabelModel labelModel);
    void updateLabelNameClick(LabelModel labelModel, String newLabelName);
}
