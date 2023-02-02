package com.example.noteapplication.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.noteapplication.Model.NoteModel;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String NOTE_TABLE = "NOTE_TABLE";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_PIN = "IS_PINNED";
    public static final String COLUMN_HEADER = "HEADER";
    public static final String COLUMN_CONTENT = "CONTENT";
    public static final String COLUMN_DATE_CREATED = "DATE_CREATED";
    public static final String COLUMN_LAST_MODIFIED = "LAST_MODIFIED";

    public DataBaseHelper(@Nullable Context context) {
        super(context, "notes.db", null, 1);
    }

    // this is called the first time a database is accessed. There should be code in here to create a new database
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + NOTE_TABLE +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PIN + " INTEGER, " +
                COLUMN_HEADER + " TEXT, " +
                COLUMN_CONTENT + " TEXT, " +
                COLUMN_DATE_CREATED + " TEXT, " +
                COLUMN_LAST_MODIFIED + " TEXT)";

        db.execSQL(createTableStatement);
    }

    // this is called if the database version number changes. It prevents previous users apps from breaking when you change the database design.
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }

    public boolean addOne(NoteModel note){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_PIN, note.isPinned());
        cv.put(COLUMN_HEADER, note.getHeader());
        cv.put(COLUMN_CONTENT, note.getContent());
        cv.put(COLUMN_DATE_CREATED, note.getDateCreated());
        cv.put(COLUMN_LAST_MODIFIED, note.getLastModified());

        long insert = db.insert(NOTE_TABLE, null, cv);
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

                NoteModel note = new NoteModel(id, isPinned, header, content, dateCreated, lastModified);
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

                NoteModel note = new NoteModel(id, isPinned, header, content, dateCreated, lastModified);
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

                NoteModel note = new NoteModel(id, isPinned, header, content, dateCreated, lastModified);
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

        // close both the cursor and the db when done
        cursor.close();
        db.close();

        return new NoteModel(id, isPinned, header, content, dateCreated, lastModified);
    }
    public boolean updateOne(NoteModel note, int isPinned, String newHeader, String newContent, String lastModified){
        // find note in the database. if it is found, update it and return true.
        // if it is not found, return false.
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "UPDATE " + NOTE_TABLE + " SET " +
                COLUMN_PIN + " = '" + isPinned + "', " +
                COLUMN_HEADER + " = '" + newHeader + "', " +
                COLUMN_CONTENT + " = '" + newContent + "', " +
                COLUMN_LAST_MODIFIED + " = '" + lastModified +
                "' WHERE " + COLUMN_ID + " = " + note.getId();
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
        String queryString = "DELETE FROM " + NOTE_TABLE + " WHERE " + COLUMN_ID + " = " + note.getId();

        Cursor cursor = db.rawQuery(queryString, null);
        if(cursor.moveToFirst()){
            return true;
        }else{
            return false;
        }
    }

    public void dropTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DROP TABLE " + NOTE_TABLE;
        db.rawQuery(queryString, null);
    }
}
