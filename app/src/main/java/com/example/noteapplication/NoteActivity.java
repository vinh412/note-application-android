package com.example.noteapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.noteapplication.Model.NoteModel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class NoteActivity extends AppCompatActivity implements CallBackColor{

    private ConstraintLayout noteLayout;
    private TextView tv_header;
    private TextView tv_content;
    private ImageButton backButton, pinButton, doneButton, colorLensButton;
    private String whatButton;
    private NoteModel note;

    private CallBackColor callBackColor;
    private int backgoundColorCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        noteLayout = findViewById(R.id.note_layout);
        tv_header = findViewById(R.id.tv_header);
        tv_content = findViewById(R.id.tv_content);
        backButton = findViewById(R.id.backBtn);
        pinButton = findViewById(R.id.pinBtn);
        doneButton = findViewById(R.id.doneBtn);
        colorLensButton = findViewById(R.id.colorLensBtn);

        callBackColor = this;

        Intent intent = getIntent();
        whatButton = intent.getStringExtra(MainActivity.KEY_WHAT_BUTTON);
        if(whatButton.equals(MainActivity.VALUE_NOTE_ITEM)){
            note = (NoteModel) intent.getSerializableExtra("note_selected");
            tv_header.setText(note.getHeader());
            tv_content.setText(note.getContent());
            backgoundColorCode = note.getBackGroundColor();
            getWindow().getDecorView().setBackgroundColor(note.getBackGroundColor());
            if(note.isPinned() == 1)
                pinButton.setImageResource(R.drawable.ic_pin);
            else
                pinButton.setImageResource(R.drawable.ic_unpin);
        }else if(whatButton.equals(MainActivity.VALUE_FAB_BUTTON)){
            backgoundColorCode = Color.WHITE;
            getWindow().getDecorView().setBackgroundColor(backgoundColorCode);
            note = new NoteModel(-1, 0, "", "", "", "", Color.WHITE);
        }

        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                finish();
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get date time
                LocalDateTime dateTime = LocalDateTime.now();
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                String formatterDate = dateTime.format(dateTimeFormatter);

                note.setHeader(tv_header.getText().toString());
                note.setContent(tv_content.getText().toString());
                note.setLastModified(formatterDate);
                note.setBackGroundColor(backgoundColorCode);
                if(whatButton.equals(MainActivity.VALUE_FAB_BUTTON))
                    note.setDateCreated(formatterDate);
                Intent intent = new Intent(NoteActivity.this, MainActivity.class);
                intent.putExtra(MainActivity.KEY_WHAT_BUTTON,whatButton);
                intent.putExtra("note", note);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

        pinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(note.isPinned() == 1){
                    pinButton.setImageResource(R.drawable.ic_unpin);
                    note.setPinned(0);
                    Toast.makeText(NoteActivity.this, "Unpin", Toast.LENGTH_SHORT).show();
                }
                else{
                    pinButton.setImageResource(R.drawable.ic_pin);
                    note.setPinned(1);
                    Toast.makeText(NoteActivity.this, "Pin", Toast.LENGTH_SHORT).show();
                }
            }
        });

        colorLensButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheet bottomSheet = new BottomSheet(callBackColor);
                bottomSheet.show(getSupportFragmentManager(), "TAG");
            }
        });
    }

    @Override
    public void setColorCode(int colorCode) {
        backgoundColorCode = colorCode;
        getWindow().getDecorView().setBackgroundColor(backgoundColorCode);
    }
}