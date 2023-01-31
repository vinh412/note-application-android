package com.example.noteapplication;

import androidx.cardview.widget.CardView;

import com.example.noteapplication.Model.NoteModel;

public interface NoteClickListener {
    void onCLick(NoteModel note);
    void onLongClick(NoteModel note, CardView cardView);
}
