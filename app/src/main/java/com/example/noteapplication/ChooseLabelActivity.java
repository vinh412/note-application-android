package com.example.noteapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.noteapplication.Adapter.ChooseLabelRecyclerViewAdapter;
import com.example.noteapplication.Database.DataBaseHelper;
import com.example.noteapplication.Model.LabelModel;

import java.util.List;

public class ChooseLabelActivity extends AppCompatActivity {

    private RecyclerView chooseLabelRecyclerView;
    private ChooseLabelRecyclerViewAdapter chooseLabelRecyclerViewAdapter;
    private List<LabelModel> labelList;
    private List<LabelModel> labelListChecked;
    private DataBaseHelper dataBaseHelper;

    private int noteID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_label);

        dataBaseHelper = new DataBaseHelper(this);

        Intent intent = getIntent();
        noteID = intent.getIntExtra("id selected note", -1);

        chooseLabelRecyclerView = findViewById(R.id.choose_label_recyclerview);
        labelList = dataBaseHelper.getAllLabels();
        labelListChecked = dataBaseHelper.getLabelsOfNote(noteID);
        updateChooseLabelRecycler(labelList, labelListChecked);
    }

    private void updateChooseLabelRecycler(List<LabelModel> labelList, List<LabelModel> labelListChecked){
        chooseLabelRecyclerView.setHasFixedSize(true);
        chooseLabelRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        chooseLabelRecyclerViewAdapter = new ChooseLabelRecyclerViewAdapter(this, labelList, labelListChecked, itemClickListener);
        chooseLabelRecyclerView.setAdapter(chooseLabelRecyclerViewAdapter);
    }

    public void onBackButtonClick(View view){
        Intent intent = new Intent(ChooseLabelActivity.this, MainActivity.class);
        setResult(111, intent);
        finish();
    }

    public void onDoneButtonClick(View view){

    }

    private final ChooseLabelRecyclerViewAdapter.ItemClickListener itemClickListener = new ChooseLabelRecyclerViewAdapter.ItemClickListener() {
        @Override
        public void onClick(LabelModel labelModel, boolean isChecked) {
            if(isChecked){
                Toast.makeText(ChooseLabelActivity.this, "remove", Toast.LENGTH_SHORT).show();
                dataBaseHelper.removeRelationship(labelModel.getID(), noteID);
            }else{
                Toast.makeText(ChooseLabelActivity.this, "add", Toast.LENGTH_SHORT).show();
                dataBaseHelper.addRelationship(labelModel.getID(), noteID);
            }
        }
    };
}