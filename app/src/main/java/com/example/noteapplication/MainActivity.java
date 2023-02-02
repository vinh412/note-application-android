package com.example.noteapplication;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.noteapplication.Adapter.NoteRecyclerViewAdapter;
import com.example.noteapplication.Database.DataBaseHelper;
import com.example.noteapplication.Model.NoteModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener{

    public static final String KEY_WHAT_BUTTON = "what_button";
    public static final String VALUE_FAB_BUTTON = "fab_button";
    public static final String VALUE_NOTE_ITEM = "note_item";

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private FloatingActionButton floatingActionButton;

    private RecyclerView pinRecyclerView;
    private RecyclerView othersRecyclerView;
    private NoteRecyclerViewAdapter pinRecyclerViewAdapter;
    private NoteRecyclerViewAdapter othersRecyclerViewAdapter;

    private List<NoteModel> allNotes = new ArrayList<>();
    private List<NoteModel> pinnedNotes = new ArrayList<>();
    private List<NoteModel> otherNotes = new ArrayList<>();

    private NoteModel selectedNote;

    private final DataBaseHelper dataBaseHelper = new DataBaseHelper(this);

    private ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == RESULT_OK){
                        Intent intent = result.getData();
                        String whatButton = intent.getStringExtra(KEY_WHAT_BUTTON);
                        if(whatButton.equals(VALUE_FAB_BUTTON)){
                            NoteModel newNote = (NoteModel) intent.getSerializableExtra("note");
                            if(newNote.getHeader().isEmpty() && newNote.getContent().isEmpty()){
                                Toast.makeText(MainActivity.this, "Deleted empty note!", Toast.LENGTH_SHORT).show();
                            }else {
                                dataBaseHelper.addOne(newNote);
                                pinnedNotes.clear();
                                pinnedNotes.addAll(dataBaseHelper.getAllPinnedNotes());
                                pinRecyclerViewAdapter.notifyDataSetChanged();

                                otherNotes.clear();
                                otherNotes.addAll(dataBaseHelper.getAllOthersNotes());
                                othersRecyclerViewAdapter.notifyDataSetChanged();
                                Toast.makeText(MainActivity.this, "Add new note completed!", Toast.LENGTH_SHORT).show();
                            }
                        }else if(whatButton.equals(VALUE_NOTE_ITEM)){
                            NoteModel updateNote = (NoteModel) intent.getSerializableExtra("note");
                            if(selectedNote.isPinned() == updateNote.isPinned() && selectedNote.getHeader().equals(updateNote.getHeader()) && selectedNote.getContent().equals(updateNote.getContent())){
                                Toast.makeText(MainActivity.this, "Nothing changes!", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                dataBaseHelper.updateOne(selectedNote, updateNote.isPinned(), updateNote.getHeader(), updateNote.getContent(), updateNote.getLastModified());
                                Toast.makeText(MainActivity.this, "Save changes!", Toast.LENGTH_SHORT).show();

                                pinnedNotes.clear();
                                pinnedNotes.addAll(dataBaseHelper.getAllPinnedNotes());
                                pinRecyclerViewAdapter.notifyDataSetChanged();

                                otherNotes.clear();
                                otherNotes.addAll(dataBaseHelper.getAllOthersNotes());
                                othersRecyclerViewAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pinRecyclerView = this.findViewById(R.id.pinRecyclerView);
        othersRecyclerView = this.findViewById(R.id.othersRecyclerView);

        floatingActionButton = findViewById(R.id.fab);

        drawerLayout = findViewById(R.id.activity_main_drawer);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NoteActivity.class);
                intent.putExtra(KEY_WHAT_BUTTON, VALUE_FAB_BUTTON);
                activityResultLauncher.launch(intent);
            }
        });

        // get data from database and show them on the screen
        allNotes = dataBaseHelper.getAllNotes();
        pinnedNotes = dataBaseHelper.getAllPinnedNotes();
        otherNotes = dataBaseHelper.getAllOthersNotes();

        if(pinnedNotes.size() == 0){
            updateOthersRecycler(otherNotes);
        }else{
            updatePinRecycler(pinnedNotes);
            updateOthersRecycler(otherNotes);
        }
    }

    private void updatePinRecycler(List<NoteModel> notes) {
        pinRecyclerView.setHasFixedSize(true);
        pinRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        pinRecyclerViewAdapter = new NoteRecyclerViewAdapter(this, notes, noteClickListener);
        pinRecyclerView.setAdapter(pinRecyclerViewAdapter);
    }

    private void updateOthersRecycler(List<NoteModel> notes){
        othersRecyclerView.setHasFixedSize(true);
        othersRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        othersRecyclerViewAdapter = new NoteRecyclerViewAdapter(this, notes, noteClickListener);
        othersRecyclerView.setAdapter(othersRecyclerViewAdapter);
    }
    private final NoteClickListener noteClickListener = new NoteClickListener() {
        @Override
        public void onCLick(NoteModel note) {
            selectedNote = note;
            Intent intent = new Intent(MainActivity.this, NoteActivity.class);
            intent.putExtra(KEY_WHAT_BUTTON, VALUE_NOTE_ITEM);
            intent.putExtra("note_selected", selectedNote);
            activityResultLauncher.launch(intent);
        }

        @Override
        public void onLongClick(NoteModel note, CardView cardView) {
            selectedNote = note;
            showPopup(cardView);
        }
    };

    private void showPopup(CardView cardView) {
        PopupMenu popupMenu = new PopupMenu(this, cardView);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.show();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(drawerToggle.onOptionsItemSelected(item))
            return true;
        switch(item.getItemId()){
            case R.id.search:
                Toast.makeText(this, "Search button selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.about:
                Toast.makeText(this, "About button selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.help:
                Toast.makeText(this, "Help button selected", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete:
                dataBaseHelper.deleteOne(selectedNote);
                if(selectedNote.isPinned() == 1)
                    pinnedNotes.remove(selectedNote);
                else
                    otherNotes.remove(selectedNote);
                pinRecyclerViewAdapter.notifyDataSetChanged();
                othersRecyclerViewAdapter.notifyDataSetChanged();
                Toast.makeText(this, "Note deleted!", Toast.LENGTH_SHORT).show();
                return true;
        }
        return false;
    }

}