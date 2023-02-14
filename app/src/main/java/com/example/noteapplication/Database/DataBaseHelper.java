package com.example.noteapplication.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.noteapplication.Model.LabelModel;
import com.example.noteapplication.Model.NoteModel;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {

    public DataBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // database values
    public static final String DATABASE_NAME = "notes.db";
    public static final int DATABASE_VERSION = 2;
    public static final String COLUMN_ID = "ID";

    // note table
    public static final String NOTE_TABLE = "NOTE_TABLE";
    public static final String COLUMN_PIN = "IS_PINNED";
    public static final String COLUMN_HEADER = "HEADER";
    public static final String COLUMN_CONTENT = "CONTENT";
    public static final String COLUMN_DATE_CREATED = "DATE_CREATED";
    public static final String COLUMN_LAST_MODIFIED = "LAST_MODIFIED";
    public static final String COLUMN_BACKGROUND_COLOR = "BACKGROUND_COLOR";

    // label table
    public static final String LABEL_TABLE = "LABEL_TABLE";
    public static final String COLUMN_LABEL_NAME = "LABEL_NAME";

    // label note table
    public static final String LABEL_NOTE_TABLE = "LABEL_NOTE_TABLE";
    public static final String COLUMN_LABEL_ID = "LABEL_ID";
    public static final String COLUMN_NOTE_ID = "NOTE_ID";

    private static final String CREATE_NOTE_TABLE = "CREATE TABLE " +
            NOTE_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_PIN + " INTEGER, " +
            COLUMN_HEADER + " TEXT, " +
            COLUMN_CONTENT + " TEXT, " +
            COLUMN_DATE_CREATED + " TEXT, " +
            COLUMN_LAST_MODIFIED + " TEXT, " +
            COLUMN_BACKGROUND_COLOR + " INTEGER)";

    private static final String ALTER_NOTE_TABLE_1 = "ALTER TABLE "
            + NOTE_TABLE + " ADD COLUMN " + COLUMN_BACKGROUND_COLOR + " INTEGER;";

    private static final String CREATE_LABEL_TABLE = "CREATE TABLE " +
            LABEL_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_LABEL_NAME + " TEXT, " + COLUMN_DATE_CREATED + " TEXT)";

    private static final String CREATE_LABEL_NOTE_TABLE = "CREATE TABLE " +
            LABEL_NOTE_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_LABEL_ID + " INTEGER REFERENCES " + LABEL_TABLE + "(" + COLUMN_ID + "), " +
            COLUMN_NOTE_ID + " INTEGER REFERENCES " + NOTE_TABLE + "(" + COLUMN_ID + "))";

    // this is called the first time a database is accessed. There should be code in here to create a new database
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_NOTE_TABLE);
        db.execSQL(CREATE_LABEL_TABLE);
        db.execSQL(CREATE_LABEL_NOTE_TABLE);
    }

    // this is called if the database version number changes. It prevents previous users apps from breaking when you change the database design.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion < 2){
            db.execSQL(ALTER_NOTE_TABLE_1);
            db.execSQL(CREATE_LABEL_TABLE);
            db.execSQL(CREATE_LABEL_NOTE_TABLE);
        }
    }

    public boolean addOne(NoteModel note){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_PIN, note.isPinned());
        cv.put(COLUMN_HEADER, note.getHeader());
        cv.put(COLUMN_CONTENT, note.getContent());
        cv.put(COLUMN_DATE_CREATED, note.getDateCreated());
        cv.put(COLUMN_LAST_MODIFIED, note.getLastModified());
        cv.put(COLUMN_BACKGROUND_COLOR, note.getBackGroundColor());
        long insert = db.insert(NOTE_TABLE, null, cv);
        if(insert == -1)
            return false;
        else
            return true;
    }

    public boolean addOne(LabelModel label){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_LABEL_NAME, label.getLabelName());
        cv.put(COLUMN_DATE_CREATED, label.getDateCreated());

        long insert = db.insert(LABEL_TABLE, null, cv);
        if(insert == -1)
            return false;
        else
            return true;
    }

    public List<NoteModel> getAllNotes(){

        List<NoteModel> notes = new ArrayList<>();

        // get data from the database
        String queryString = "SELECT * FROM " + NOTE_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);
        if(cursor.moveToFirst()){
            // loop through the cursor (result set) and create new note objects then put them into the list
            do {
                int id = cursor.getInt(0);
                int isPinned = cursor.getInt(1);
                String header = cursor.getString(2);
                String content = cursor.getString(3);
                String dateCreated = cursor.getString(4);
                String lastModified = cursor.getString(5);
                int backgroundColor = cursor.getInt(6);
                NoteModel note = new NoteModel(id, isPinned, header, content, dateCreated, lastModified, backgroundColor);
                notes.add(note);
            }while (cursor.moveToNext());
        }
        else {
            // failure, do not add anything to the list.
        }

        // close both the cursor and the db when done.
        cursor.close();
        db.close();

        return notes;
    }

    public List<LabelModel> getAllLabels(){
        List<LabelModel> labels = new ArrayList<>();

        // get data from the database
        String queryString = "SELECT * FROM " + LABEL_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);
        if(cursor.moveToFirst()){
            // loop through the cursor (result set) and create new note objects then put them into the list
            do {
                int id = cursor.getInt(0);
                String labelName = cursor.getString(1);
                String dateCreated = cursor.getString(2);

                LabelModel label = new LabelModel(id, labelName, dateCreated);
                labels.add(label);
            }while (cursor.moveToNext());
        }
        else {
            // failure, do not add anything to the list.
        }

        // close both the cursor and the db when done.
        cursor.close();
        db.close();

        return labels;
    }

    public List<NoteModel> getAllPinnedNotes(){

        List<NoteModel> notes = new ArrayList<>();

        // get data from the database
        String queryString = "SELECT * FROM " + NOTE_TABLE + " WHERE " + COLUMN_PIN + " = " + 1;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);
        if(cursor.moveToFirst()){
            // loop through the cursor (result set) and create new note objects then put them into the list
            do {
                int id = cursor.getInt(0);
                int isPinned = cursor.getInt(1);
                String header = cursor.getString(2);
                String content = cursor.getString(3);
                String dateCreated = cursor.getString(4);
                String lastModified = cursor.getString(5);
                int backgroundColor = cursor.getInt(6);
                NoteModel note = new NoteModel(id, isPinned, header, content, dateCreated, lastModified, backgroundColor);
                notes.add(note);
            }while (cursor.moveToNext());
        }
        else {
            // failure, do not add anything to the list.
        }

        // close both the cursor and the db when done.
        cursor.close();
        db.close();

        return notes;
    }

    public List<NoteModel> getAllOthersNotes(){

        List<NoteModel> notes = new ArrayList<>();

        // get data from the database
        String queryString = "SELECT * FROM " + NOTE_TABLE + " WHERE " + COLUMN_PIN + " = " + 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);
        if(cursor.moveToFirst()){
            // loop through the cursor (result set) and create new note objects then put them into the list
            do {
                int id = cursor.getInt(0);
                int isPinned = cursor.getInt(1);
                String header = cursor.getString(2);
                String content = cursor.getString(3);
                String dateCreated = cursor.getString(4);
                String lastModified = cursor.getString(5);
                int backgroundColor = cursor.getInt(6);
                NoteModel note = new NoteModel(id, isPinned, header, content, dateCreated, lastModified, backgroundColor);
                notes.add(note);
            }while (cursor.moveToNext());
        }
        else {
            // failure, do not add anything to the list.
        }

        // close both the cursor and the db when done.
        cursor.close();
        db.close();

        return notes;
    }

    public NoteModel getNoteByID(int noteID){
        // get data from the database
        String queryString = "SELECT * FROM " + NOTE_TABLE + " WHERE " + COLUMN_ID + " = " + noteID;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);
        cursor.moveToFirst();
        int id = cursor.getInt(0);
        int isPinned = cursor.getInt(1);
        String header = cursor.getString(2);
        String content = cursor.getString(3);
        String dateCreated = cursor.getString(4);
        String lastModified = cursor.getString(5);
        int backgroundColor = cursor.getInt(6);
        // close both the cursor and the db when done
        cursor.close();
        db.close();

        return new NoteModel(id, isPinned, header, content, dateCreated, lastModified, backgroundColor);
    }

    public LabelModel getLabelByID(int labelID){
        // get data from the database
        String queryString = "SELECT * FROM " + LABEL_TABLE + " WHERE " + COLUMN_ID + " = " + labelID;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);
        cursor.moveToFirst();
        int id = cursor.getInt(0);
        String labelName = cursor.getString(1);
        String dateCreated = cursor.getString(2);
        // close both the cursor and the db when done
        cursor.close();
        db.close();

        return new LabelModel(id, labelName, dateCreated);
    }
    public boolean updateOne(NoteModel note, int isPinned, String newHeader, String newContent, String lastModified, int backgroundColorCode){
        // find note in the database. if it is found, update it and return true.
        // if it is not found, return false.
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "UPDATE " + NOTE_TABLE + " SET " +
                COLUMN_PIN + " = '" + isPinned + "', " +
                COLUMN_HEADER + " = '" + newHeader + "', " +
                COLUMN_CONTENT + " = '" + newContent + "', " +
                COLUMN_LAST_MODIFIED + " = '" + lastModified + "', " +
                COLUMN_BACKGROUND_COLOR + " = '" + backgroundColorCode +
                "' WHERE " + COLUMN_ID + " = " + note.getID();
        Cursor cursor = db.rawQuery(queryString, null);
        if(cursor.moveToFirst()){
            return true;
        }else{
            return false;
        }
    }

    public boolean updateOne(LabelModel label, String newLabelName){
        // find note in the database. if it is found, update it and return true.
        // if it is not found, return false.
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "UPDATE " + LABEL_TABLE + " SET " +
                COLUMN_LABEL_NAME + " = '" + newLabelName +
                "' WHERE " + COLUMN_ID + " = " + label.getID();
        Cursor cursor = db.rawQuery(queryString, null);
        if(cursor.moveToFirst()){
            return true;
        }else{
            return false;
        }
    }

    public boolean deleteOne(NoteModel note){
        // find note in the database. if it is found, delete it and return true.
        // if it is not found, return false.
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM " + NOTE_TABLE + " WHERE " + COLUMN_ID + " = " + note.getID();

        Cursor cursor = db.rawQuery(queryString, null);
        if(cursor.moveToFirst()){
            return true;
        }else{
            return false;
        }
    }

    public boolean deleteOne(LabelModel label){
        // find label in the database. if it is found, delete it and return true.
        // if it is not found, return false.
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM " + LABEL_TABLE + " WHERE " + COLUMN_ID + " = " + label.getID();

        Cursor cursor = db.rawQuery(queryString, null);
        if(cursor.moveToFirst()){
            return true;
        }else{
            return false;
        }
    }

    public List<LabelModel> getLabelsOfNote(int noteID){
        List<LabelModel> result = new ArrayList<>();

        String queryString = "SELECT * FROM " + LABEL_NOTE_TABLE + " WHERE " + COLUMN_NOTE_ID + " = " + noteID;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);
        if(cursor.moveToFirst()){
            // loop through the cursor (result set) and create new note objects then put them into the list
            do {
                int labelID = cursor.getInt(1);
                LabelModel label = getLabelByID(labelID);
                result.add(label);
            }while (cursor.moveToNext());
        }
        else {
            // failure, do not add anything to the list.
        }

        // close both the cursor and the db when done.
        cursor.close();
        db.close();

        return result;
    }

    public boolean addRelationship(int labelID, int noteID){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_LABEL_ID, labelID);
        cv.put(COLUMN_NOTE_ID, noteID);

        long insert = db.insert(LABEL_NOTE_TABLE, null, cv);
        if(insert == -1)
            return false;
        else
            return true;
    }

    public boolean removeRelationship(int labelID, int noteID){
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM " + LABEL_NOTE_TABLE + " WHERE " + COLUMN_LABEL_ID + " = " + labelID + " AND " + COLUMN_NOTE_ID + " = " + noteID;

        Cursor cursor = db.rawQuery(queryString, null);
        if(cursor.moveToFirst()){
            return true;
        }else{
            return false;
        }
    }
}
