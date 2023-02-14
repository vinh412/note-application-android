package com.example.noteapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.noteapplication.Database.DataBaseHelper;
import com.example.noteapplication.Model.LabelModel;
import com.example.noteapplication.NoteItemClickListener;
import com.example.noteapplication.Model.NoteModel;
import com.example.noteapplication.R;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxItemDecoration;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NoteRecyclerViewAdapter extends RecyclerView.Adapter<NoteViewHolder> {
    private List<NoteModel> notes;
    private Context context;
    private LayoutInflater layoutInflater;
    private NoteItemClickListener listener;

    public NoteRecyclerViewAdapter(Context context, List<NoteModel> notes, NoteItemClickListener listener) {
        this.notes = notes;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        return new NoteViewHolder(LayoutInflater.from(context).inflate(R.layout.recyclerview_note_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        // Set note via position
        NoteModel note = this.notes.get(position);

        // Bind data to viewholder
        holder.header.setText(note.getHeader());
        holder.content.setText(note.getContent());
        holder.dateCreated.setText("Date Created: " + note.getDateCreated());
        holder.lastModified.setText("Last Modified: " + note.getLastModified());

        holder.noteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCLick(notes.get(holder.getBindingAdapterPosition()));
            }
        });

        holder.noteItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                listener.onLongClick(notes.get(holder.getBindingAdapterPosition()), holder.noteItem);
                return true;
            }
        });

        holder.noteItem.setCardBackgroundColor(note.getBackGroundColor());

        // display label on note item
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        List<LabelModel> labelsOnNoteItem = dataBaseHelper.getLabelsOfNote(note.getID());
        holder.labelRecyclerView.setHasFixedSize(true);
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(context);

        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.FLEX_START);
        holder.labelRecyclerView.setLayoutManager(layoutManager);

        LabelNoteRecyclerViewAdapter labelNoteAdapter = new LabelNoteRecyclerViewAdapter(context, labelsOnNoteItem);
        holder.labelRecyclerView.setAdapter(labelNoteAdapter);

    }

    @Override
    public int getItemCount() {
        return this.notes.size();
    }

    public void filterList(List<NoteModel> filteredList){
        notes = filteredList;
        notifyDataSetChanged();
    }
}

class NoteViewHolder extends RecyclerView.ViewHolder {

    CardView noteItem;
    TextView header;
    TextView content;
    RecyclerView labelRecyclerView;
    TextView dateCreated;
    TextView lastModified;

    public NoteViewHolder(@NonNull View itemView) {
        super(itemView);
        this.noteItem = (CardView) itemView.findViewById(R.id.note_item);
        this.header = (TextView) itemView.findViewById(R.id.tv_header_item);
        this.content = (TextView) itemView.findViewById(R.id.tv_content_item);
        this.labelRecyclerView = (RecyclerView) itemView.findViewById(R.id.label_note_recyclerview);
        this.dateCreated = (TextView) itemView.findViewById(R.id.tv_date_created_item);
        this.lastModified = (TextView) itemView.findViewById(R.id.tv_last_modified_item);
    }
}
