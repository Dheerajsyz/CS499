package com.dheeraj.snhu_dheeraj_kollapaneni;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class EventListActivity extends AppCompatActivity {

    private EventAdapter eventAdapter;
    private DatabaseHelper dbHelper;
    private List<Event> eventList;
    private DrawerLayout drawerLayout;
    private ActivityResultLauncher<Intent> addEditEventLauncher;

    private static final String TAG = "EventListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Setup drawer layout and toggle
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Initialize RecyclerView and FloatingActionButton
        dbHelper = new DatabaseHelper(this);
        RecyclerView recyclerViewEvents = findViewById(R.id.recyclerViewEvents);
        recyclerViewEvents.setLayoutManager(new LinearLayoutManager(this));

        addEditEventLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        loadEvents();  // Refresh events after adding/editing
                    }
                });

        FloatingActionButton fabAddEvent = findViewById(R.id.fabAddEvent);
        fabAddEvent.setOnClickListener(v -> {
            Intent intent = new Intent(EventListActivity.this, AddEditEventActivity.class);
            addEditEventLauncher.launch(intent);
        });

        // Load the events initially
        loadEvents();

        eventAdapter = new EventAdapter(eventList, new EventAdapter.OnEventClickListener() {
            @Override
            public void onEditClick(Event event) {
                Intent intent = new Intent(EventListActivity.this, AddEditEventActivity.class);
                intent.putExtra("event_id", event.getId());
                addEditEventLauncher.launch(intent);  // Launch for result
            }

            @Override
            public void onDeleteClick(Event event) {
                if (dbHelper.deleteEvent(event.getId())) {
                    Toast.makeText(EventListActivity.this, "Event deleted", Toast.LENGTH_SHORT).show();
                    loadEvents();  // Refresh the list after deletion
                } else {
                    Toast.makeText(EventListActivity.this, "Failed to delete event", Toast.LENGTH_SHORT).show();
                }
            }
        });
        recyclerViewEvents.setAdapter(eventAdapter);

        // Handle NavigationView item clicks with if-else
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_view_events) {
                Toast.makeText(EventListActivity.this, "View Events clicked", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.nav_sms_alerts) {
                startActivity(new Intent(EventListActivity.this, SmsPermissionActivity.class));
            } else if (id == R.id.nav_logout) {
                finish();
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    // This method loads the events from the database
    private void loadEvents() {
        eventList = new ArrayList<>();
        try (Cursor cursor = dbHelper.getAllEvents()) {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    Event event = new Event(
                            cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EVENT_ID)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EVENT_NAME)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EVENT_DATE)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EVENT_TIME))
                    );
                    eventList.add(event);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error loading events", e);
        }

        if (eventAdapter != null) {
            eventAdapter.updateEventList(eventList);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
