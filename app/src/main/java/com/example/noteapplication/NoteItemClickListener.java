package com.example.noteapplication;

import androidx.cardview.widget.CardView;

import com.example.noteapplication.Model.NoteModel;

public interface NoteItemClickListener {
    void onCLick(NoteModel note);
    void onLongClick(NoteModel note, CardView cardView);
}
