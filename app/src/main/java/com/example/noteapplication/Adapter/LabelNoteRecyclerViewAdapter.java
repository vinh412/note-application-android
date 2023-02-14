package com.example.noteapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.noteapplication.Model.LabelModel;
import com.example.noteapplication.R;

import java.util.List;

public class LabelNoteRecyclerViewAdapter extends RecyclerView.Adapter<LabelNoteViewHolder> {
    private List<LabelModel> labelList;
    private Context context;
    private LayoutInflater layoutInflater;

    public LabelNoteRecyclerViewAdapter(Context context, List<LabelModel> labelList){
        this.labelList = labelList;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }
    @NonNull
    @Override
    public LabelNoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LabelNoteViewHolder(LayoutInflater.from(context).inflate(R.layout.recyclerview_label_note_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LabelNoteViewHolder holder, int position) {
        // set label via position
        LabelModel label = this.labelList.get(position);

        // bind data to viewholder
        holder.labelName.setText(label.getLabelName());
    }

    @Override
    public int getItemCount() {
        return this.labelList.size();
    }
}

class LabelNoteViewHolder extends RecyclerView.ViewHolder{
    CardView labelItem;
    TextView labelName;

    public LabelNoteViewHolder(@NonNull View itemView) {
        super(itemView);
        this.labelItem = (CardView) itemView.findViewById(R.id.label_note_item);
        this.labelName = (TextView) itemView.findViewById(R.id.label_note_name);
    }
}
