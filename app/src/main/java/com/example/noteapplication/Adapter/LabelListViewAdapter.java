package com.example.noteapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.noteapplication.LabelActivity;
import com.example.noteapplication.LabelItemClickListener;
import com.example.noteapplication.Model.LabelModel;
import com.example.noteapplication.R;

import java.util.ArrayList;
import java.util.List;

public class LabelListViewAdapter extends ArrayAdapter<LabelModel> {
    private List<LabelModel> labelList;
    private Context mContext;
    private LabelItemClickListener listener;

    private static class ViewHolder{
        ImageButton labelButton;
        EditText txtLabelName;
        ImageButton editButton;

        public ViewHolder() {
        }
    }
    public LabelListViewAdapter(Context context, List<LabelModel> items, LabelItemClickListener listener) {
        super(context, R.layout.listview_item_layout, items);
        this.mContext = context;
        this.listener = listener;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // get the data item for this position
        LabelModel labelModel = getItem(position);

        // check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.listview_item_layout, parent, false);

            viewHolder.labelButton = (ImageButton) convertView.findViewById(R.id.label_image_button) ;
            viewHolder.txtLabelName = (EditText) convertView.findViewById(R.id.label_name);
            viewHolder.editButton = (ImageButton) convertView.findViewById(R.id.edit_label_image_button);

            viewHolder.labelButton.setImageResource(R.drawable.ic_label);
            viewHolder.editButton.setImageResource(R.drawable.ic_edit);
            viewHolder.txtLabelName.setText(labelModel.getLabelName());

            result = convertView;

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        viewHolder.labelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "delete", Toast.LENGTH_SHORT).show();
                listener.onDeleteClick(labelModel);
            }
        });

        viewHolder.txtLabelName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    viewHolder.labelButton.setImageResource(R.drawable.ic_delete);
                    viewHolder.editButton.setImageResource(R.drawable.ic_done);
                    viewHolder.editButton.setVisibility(View.VISIBLE);
                }else{
                    viewHolder.labelButton.setImageResource(R.drawable.ic_label);
                    viewHolder.editButton.setVisibility(View.INVISIBLE);
                }
            }
        });

        viewHolder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentLabelName = viewHolder.txtLabelName.getText().toString();
                if(currentLabelName.isEmpty() || currentLabelName.equals(labelModel.getLabelName())){
                    viewHolder.txtLabelName.clearFocus();
                }else{
                    listener.updateLabelNameClick(labelModel, currentLabelName);
                    viewHolder.txtLabelName.clearFocus();
                }
            }
        });

        // return the completed view to render on screen
        return convertView;
    }
}
