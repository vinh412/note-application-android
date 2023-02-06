package com.example.noteapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.noteapplication.Model.LabelModel;
import com.example.noteapplication.R;

import java.util.List;

public class LabelListViewAdapter extends ArrayAdapter<LabelModel> {
    private int resourceLayout;
    private Context mContext;

    public LabelListViewAdapter(Context context, int resource, List<LabelModel> items) {
        super(context, resource, items);
        this.resourceLayout = resource;
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(mContext);
            v = vi.inflate(resourceLayout, null);
        }

        LabelModel p = getItem(position);

        if (p != null) {
            TextView tt1 = (TextView) v.findViewById(R.id.label_name);

            if (tt1 != null) {
                tt1.setText(p.getLabelName());
            }
        }

        return v;
    }
}
