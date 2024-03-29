package com.example.noteapplication;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.noteapplication.Adapter.NoteRecyclerViewAdapter;
import com.example.noteapplication.Database.DataBaseHelper;
import com.example.noteapplication.Model.LabelModel;
import com.example.noteapplication.Model.NoteModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    public static final String KEY_WHAT_BUTTON = "what_button";
    public static final String VALUE_FAB_BUTTON = "fab_button";
    public static final String VALUE_NOTE_ITEM = "note_item";

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle drawerToggle;
    private FloatingActionButton floatingActionButton;

    private RecyclerView pinRecyclerView;
    private RecyclerView othersRecyclerView;
    private NoteRecyclerViewAdapter pinRecyclerViewAdapter;
    private NoteRecyclerViewAdapter othersRecyclerViewAdapter;

    private List<NoteModel> allNotes = new ArrayList<>();
    private List<NoteModel> pinnedNotes = new ArrayList<>();
    private List<NoteModel> otherNotes = new ArrayList<>();

    private List<LabelModel> allLabels = new ArrayList<>();

    private NoteModel selectedNote;

    private final DataBaseHelper dataBaseHelper = new DataBaseHelper(this);

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

        allLabels = dataBaseHelper.getAllLabels();

        if(pinnedNotes.size() == 0){
            updateOthersRecycler(otherNotes);
        }else{
            updatePinRecycler(pinnedNotes);
            updateOthersRecycler(otherNotes);
        }

        NavigationView navigationView = (NavigationView)findViewById(R.id.navigation_view);
//        navigationView.setNavigationItemSelectedListener(this);
        onCreateLabelMenuNav();
    }

    // get note from NoteActivity
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

                                otherNotes.clear();
                                otherNotes.addAll(dataBaseHelper.getAllOthersNotes());
                                Toast.makeText(MainActivity.this, "Add new note completed!", Toast.LENGTH_SHORT).show();
                            }
                        }else if(whatButton.equals(VALUE_NOTE_ITEM)){
                            NoteModel updateNote = (NoteModel) intent.getSerializableExtra("note");
                            if(selectedNote.isPinned() == updateNote.isPinned() &&
                                    selectedNote.getHeader().equals(updateNote.getHeader()) &&
                                    selectedNote.getContent().equals(updateNote.getContent()) &&
                                    selectedNote.getBackGroundColor() == updateNote.getBackGroundColor()){
                                Toast.makeText(MainActivity.this, "Nothing changes!", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                dataBaseHelper.updateOne(selectedNote, updateNote.isPinned(), updateNote.getHeader(), updateNote.getContent(), updateNote.getLastModified(), updateNote.getBackGroundColor());
                                Toast.makeText(MainActivity.this, "Save changes!", Toast.LENGTH_SHORT).show();

                                pinnedNotes.clear();
                                pinnedNotes.addAll(dataBaseHelper.getAllPinnedNotes());

                                otherNotes.clear();
                                otherNotes.addAll(dataBaseHelper.getAllOthersNotes());
                            }
                        }
                        pinRecyclerViewAdapter.notifyDataSetChanged();
                        othersRecyclerViewAdapter.notifyDataSetChanged();
                    }else if(result.getResultCode() == 111){
                        pinRecyclerViewAdapter.notifyDataSetChanged();
                        othersRecyclerViewAdapter.notifyDataSetChanged();
                    }
                }
            });

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
    private final NoteItemClickListener noteClickListener = new NoteItemClickListener() {
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
        MenuItem item = popupMenu.getMenu().findItem(R.id.pin_unpin);
        if(selectedNote.isPinned() == 1){
            item.setTitle("Unpin");
        }else{
            item.setTitle("Pin");
        }
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

    private void filter(String newText) {
        List<NoteModel> filteredPinList = new ArrayList<>();
        List<NoteModel> filteredOtherList = new ArrayList<>();
        for(NoteModel singleNote : allNotes){
            if(singleNote.getHeader().toLowerCase().contains(newText.toLowerCase()) ||
            singleNote.getContent().toLowerCase().contains(newText.toLowerCase())){
                if(singleNote.isPinned() == 1)
                    filteredPinList.add(singleNote);
                else
                    filteredOtherList.add(singleNote);
            }
            pinRecyclerViewAdapter.filterList(filteredPinList);
            othersRecyclerViewAdapter.filterList(filteredOtherList);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(drawerToggle.onOptionsItemSelected(item))
            return true;
        switch(item.getItemId()){
            case R.id.search:
                SearchView searchView = (SearchView) item.getActionView();
                searchView.setQueryHint("Type here to search");

                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        filter(newText);
                        return true;
                    }
                });
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

            case R.id.label_popup_menu:
                Intent intent = new Intent(MainActivity.this, ChooseLabelActivity.class);
                intent.putExtra("id selected note", selectedNote.getID());
                activityResultLauncher.launch(intent);
                return true;

            case R.id.pin_unpin:
                if(selectedNote.isPinned() == 1){
                    selectedNote.setPinned(0);
                    dataBaseHelper.updateOne(selectedNote, 0, selectedNote.getHeader(), selectedNote.getContent(), selectedNote.getLastModified(), selectedNote.getBackGroundColor());
                    pinnedNotes.remove(selectedNote);
                    otherNotes.add(selectedNote);
                    Toast.makeText(this, "Unpin", Toast.LENGTH_SHORT).show();
                }else{
                    selectedNote.setPinned(1);
                    dataBaseHelper.updateOne(selectedNote, 1, selectedNote.getHeader(), selectedNote.getContent(), selectedNote.getLastModified(), selectedNote.getBackGroundColor());
                    otherNotes.remove(selectedNote);
                    pinnedNotes.add(selectedNote);
                    Toast.makeText(this, "Pin", Toast.LENGTH_SHORT).show();
                }
                pinRecyclerViewAdapter.notifyDataSetChanged();
                othersRecyclerViewAdapter.notifyDataSetChanged();

        }
        return false;
    }

    private void onCreateLabelMenuNav(){
        NavigationView navView = (NavigationView) findViewById(R.id.navigation_view);

        allLabels.clear();
        allLabels.addAll(dataBaseHelper.getAllLabels());

        Menu navMenu = navView.getMenu();
        Menu labelMenu = navMenu.getItem(2).getSubMenu();

        for(LabelModel label : allLabels){
            MenuItem newLabelItem = labelMenu.add(Menu.NONE, Menu.NONE, Menu.NONE, label.getLabelName());
            newLabelItem.setIcon(R.drawable.ic_label);
            newLabelItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(@NonNull MenuItem item) {
                    Toast.makeText(MainActivity.this, newLabelItem.getTitle(), Toast.LENGTH_SHORT).show();
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.activity_main_drawer);
                    drawer.closeDrawer(GravityCompat.START);
                    return true;
                }
            });
        }

        MenuItem addLabelItem = labelMenu.add(0,0,0,"Thêm nhãn");
        addLabelItem.setIcon(R.drawable.ic_add);
        addLabelItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item) {
                Intent intent = new Intent(MainActivity.this, LabelActivity.class);
                activityResultLauncher.launch(intent);
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.activity_main_drawer);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        navView.invalidate();
    }

//    @Override
//    public boolean onNavigationItemSelected(MenuItem item) {
//        switch (item.getItemId()){
//            case R.id.nav_add_label:
//                Intent intent = new Intent(MainActivity.this, LabelActivity.class);
//                startActivity(intent);
//        }
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.activity_main_drawer);
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
//    }
}















