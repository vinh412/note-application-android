package com.example.noteapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.noteapplication.Adapter.LabelListViewAdapter;
import com.example.noteapplication.Database.DataBaseHelper;
import com.example.noteapplication.Model.LabelModel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class LabelActivity extends AppCompatActivity {

    private DataBaseHelper dataBaseHelper = new DataBaseHelper(this);
    private List<LabelModel> labelList;
    private ListView labelListView;
    private static LabelListViewAdapter labelAdapter;

    private ImageButton addLabelBtn;
    private EditText txtAddLabel;
    private ImageButton doneAddLabelBtn;

    private final LabelItemClickListener labelItemClickListener = new LabelItemClickListener() {
        @Override
        public void onDeleteClick(LabelModel labelModel) {
            Toast.makeText(LabelActivity.this, "Remove label " + labelModel.getID() , Toast.LENGTH_SHORT).show();
            dataBaseHelper.deleteOne(labelModel);
            labelList.clear();
            labelList.addAll(dataBaseHelper.getAllLabels());
            labelAdapter.notifyDataSetChanged();
        }

        @Override
        public void updateLabelNameClick(LabelModel labelModel, String newLabelName) {
            dataBaseHelper.updateOne(labelModel, newLabelName);
            labelList.clear();
            labelList.addAll(dataBaseHelper.getAllLabels());
            labelAdapter.notifyDataSetChanged();
        }

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_label);

        addLabelBtn = findViewById(R.id.add_label_image_btn);
        txtAddLabel = findViewById(R.id.txt_add_label);
        doneAddLabelBtn = findViewById(R.id.done_label_image_btn);

//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
//        txtAddLabel.requestFocus();

        labelListView = findViewById(R.id.label_list_view);

        labelList = dataBaseHelper.getAllLabels();

        labelAdapter = new LabelListViewAdapter(getApplicationContext(), labelList, labelItemClickListener);
        labelListView.setAdapter(labelAdapter);

        txtAddLabel.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    addLabelBtn.setImageResource(R.drawable.ic_close);
                    doneAddLabelBtn.setImageResource(R.drawable.ic_done);
                    doneAddLabelBtn.setVisibility(View.VISIBLE);
                }else{
                    txtAddLabel.setText("");
                    addLabelBtn.setImageResource(R.drawable.ic_add);
                    doneAddLabelBtn.setVisibility(View.INVISIBLE);
                }
            }
        });

        addLabelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtAddLabel.getText().toString().isEmpty()){
                    txtAddLabel.requestFocus();
                    txtAddLabel.setText("");
                }else{
                    txtAddLabel.setText("");
                }
            }
        });

        doneAddLabelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LabelActivity.this, "Add new label", Toast.LENGTH_SHORT).show();
                String labelName = txtAddLabel.getText().toString();
                if(!labelName.isEmpty()){
                    LocalDateTime dateTime = LocalDateTime.now();
                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                    String formatterDate = dateTime.format(dateTimeFormatter);

                    LabelModel newLabel = new LabelModel(-1, labelName, formatterDate);
                    dataBaseHelper.addOne(newLabel);
                    labelList.clear();
                    labelList.addAll(dataBaseHelper.getAllLabels());
                    labelAdapter.notifyDataSetChanged();
                    txtAddLabel.clearFocus();
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                }
            }
        });



    }
}