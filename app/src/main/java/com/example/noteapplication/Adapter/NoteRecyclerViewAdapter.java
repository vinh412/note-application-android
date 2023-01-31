package com.example.noteapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.noteapplication.NoteClickListener;
import com.example.noteapplication.Model.NoteModel;
import com.example.noteapplication.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NoteRecyclerViewAdapter extends RecyclerView.Adapter<NoteViewHolder> {
    private List<NoteModel> notes;
    private Context context;
    private LayoutInflater layoutInflater;
    private NoteClickListener listener;

    public NoteRecyclerViewAdapter(Context context, List<NoteModel> notes, NoteClickListener listener) {
        this.notes = notes;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
//        // Inflate view from recyclerview_item_layout.xml
//        View recyclerViewItem = layoutInflater.inflate(R.layout.recycleview_item_layout, parent, false);
//
//        recyclerViewItem.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                handleRecyclerItemClick( (RecyclerView)parent, v);
//            }
//        });
//        return new NoteViewHolder(recyclerViewItem);
        return new NoteViewHolder(LayoutInflater.from(context).inflate(R.layout.recycleview_item_layout, parent, false));
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

        int colorCode = getRandomColor();
        holder.noteItem.setCardBackgroundColor(holder.itemView.getResources().getColor(colorCode, null));
    }

    @Override
    public int getItemCount() {
        return this.notes.size();
    }

    private int getRandomColor(){
        List<Integer> colorCode = new ArrayList<>();

        colorCode.add(R.color.color1);
        colorCode.add(R.color.color2);
        colorCode.add(R.color.color3);
        colorCode.add(R.color.color4);
        colorCode.add(R.color.color5);

        Random random = new Random();
        int randomColor = random.nextInt(colorCode.size());
        return colorCode.get(randomColor);
    }
}

class NoteViewHolder extends RecyclerView.ViewHolder {

    CardView noteItem;
    TextView header;
    TextView content;
    TextView dateCreated;
    TextView lastModified;

    public NoteViewHolder(@NonNull View itemView) {
        super(itemView);
        this.noteItem = (CardView) itemView.findViewById(R.id.note_item);
        this.header = (TextView) itemView.findViewById(R.id.tv_header_item);
        this.content = (TextView) itemView.findViewById(R.id.tv_content_item);
        this.dateCreated = (TextView) itemView.findViewById(R.id.tv_date_created_item);
        this.lastModified = (TextView) itemView.findViewById(R.id.tv_last_modified_item);
    }
}
