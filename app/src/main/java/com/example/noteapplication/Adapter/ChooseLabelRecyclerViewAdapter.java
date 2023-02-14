package com.example.noteapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.noteapplication.Model.LabelModel;
import com.example.noteapplication.R;

import java.util.List;

public class ChooseLabelRecyclerViewAdapter extends RecyclerView.Adapter<LabelViewHolder> {
    private List<LabelModel> labelList;
    private List<LabelModel> labelListChecked;
    private Context context;
    private LayoutInflater layoutInflater;
    private ItemClickListener listener;

    public ChooseLabelRecyclerViewAdapter(Context context, List<LabelModel> labelList, List<LabelModel> labelListChecked, ItemClickListener listener){
        this.labelList = labelList;
        this.labelListChecked = labelListChecked;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    @NonNull
    @Override
    public LabelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LabelViewHolder(LayoutInflater.from(context).inflate(R.layout.recyclerview_choose_label_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LabelViewHolder holder, int position) {
        // set label via position
        LabelModel label = this.labelList.get(position);

        // Bind data to viewholder
        holder.labelName.setText(label.getLabelName());
        for(LabelModel labelModel : labelListChecked ){
            if(label.getID() == labelModel.getID())
                holder.labelCheckBox.setChecked(true);
        }
        holder.labelItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.labelCheckBox.isChecked()) {
                    listener.onClick(labelList.get(holder.getBindingAdapterPosition()), holder.labelCheckBox.isChecked());
                    holder.labelCheckBox.setChecked(false);
                }
                else{
                    listener.onClick(labelList.get(holder.getBindingAdapterPosition()), holder.labelCheckBox.isChecked());
                    holder.labelCheckBox.setChecked(true);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.labelList.size();
    }

    public interface ItemClickListener{
        void onClick(LabelModel labelModel, boolean isChecked);
    }
}

class LabelViewHolder extends RecyclerView.ViewHolder{
    CardView labelItem;
    TextView labelName;
    CheckBox labelCheckBox;

    public LabelViewHolder(View itemView){
        super(itemView);
        this.labelItem = (CardView) itemView.findViewById(R.id.choose_label_item);
        this.labelName = (TextView) itemView.findViewById(R.id.labelNameTextView);
        this.labelCheckBox = (CheckBox) itemView.findViewById(R.id.labelCheckBox);
    }
}
