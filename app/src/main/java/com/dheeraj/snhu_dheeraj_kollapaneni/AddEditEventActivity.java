package com.dheeraj.snhu_dheeraj_kollapaneni;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.Calendar;

public class AddEditEventActivity extends AppCompatActivity {

    private EditText etEventName, etEventDate, etEventTime;
    private DatabaseHelper dbHelper;
    private int eventId = -1;  // Default -1 for new event
    private static final int SMS_PERMISSION_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        dbHelper = new DatabaseHelper(this);
        etEventName = findViewById(R.id.etEventName);
        etEventDate = findViewById(R.id.etEventDate);
        etEventTime = findViewById(R.id.etEventTime);
        Button btnSaveEvent = findViewById(R.id.btnSaveEvent);

        // Date picker setup
        etEventDate.setOnClickListener(v -> showDatePicker());

        // Time picker setup
        etEventTime.setOnClickListener(v -> showTimePicker());

        // Check if we are editing an existing event
        Intent intent = getIntent();
        if (intent.hasExtra("event_id")) {
            eventId = intent.getIntExtra("event_id", -1);  // Get the event ID from intent
            loadEventDetails(eventId);  // Load event details for editing
        }

        // Set save button click listener
        btnSaveEvent.setOnClickListener(v -> {
            String name = etEventName.getText().toString();
            String date = etEventDate.getText().toString();
            String time = etEventTime.getText().toString();

            if (eventId == -1) {
                // Add a new event
                boolean isInserted = dbHelper.addEvent(name, date, time);
                if (isInserted) {
                    sendSmsNotification(name);
                    Toast.makeText(AddEditEventActivity.this, "Event added", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AddEditEventActivity.this, "Failed to add event", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Update the existing event
                boolean isUpdated = dbHelper.updateEvent(eventId, name, date, time);
                if (isUpdated) {
                    sendSmsNotification(name);
                    Toast.makeText(AddEditEventActivity.this, "Event updated", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AddEditEventActivity.this, "Failed to update event", Toast.LENGTH_SHORT).show();
                }
            }

            // Return result to previous activity
            setResult(RESULT_OK);
            finish();
        });
    }

    // Show the date picker dialog
    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        @SuppressLint("SetTextI18n") DatePickerDialog datePickerDialog = new DatePickerDialog(
                AddEditEventActivity.this,
                (view, year1, month1, dayOfMonth) -> etEventDate.setText(dayOfMonth + "/" + (month1 + 1) + "/" + year1),
                year, month, day);
        datePickerDialog.show();
    }

    // Show the time picker dialog
    private void showTimePicker() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        @SuppressLint("SetTextI18n") TimePickerDialog timePickerDialog = new TimePickerDialog(
                AddEditEventActivity.this,
                (view, hourOfDay, minute1) -> etEventTime.setText(hourOfDay + ":" + minute1),
                hour, minute, true);
        timePickerDialog.show();
    }

    // Load the details of the event to edit
    private void loadEventDetails(int eventId) {
        Cursor cursor = dbHelper.getEvent(eventId);
        if (cursor != null && cursor.moveToFirst()) {
            etEventName.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EVENT_NAME)));
            etEventDate.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EVENT_DATE)));
            etEventTime.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EVENT_TIME)));
            cursor.close();
        }
    }

    // Send SMS notification after adding/updating an event
    private void sendSmsNotification(String eventName) {
        String phoneNumber = "5551234567";  // Replace with actual phone number, or get dynamically

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
            // Permission granted, send the SMS
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, "A new event has been created: " + eventName, null, null);
            Toast.makeText(this, "SMS sent to " + phoneNumber, Toast.LENGTH_SHORT).show();
        } else {
            // Request permission if not granted
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, SMS_PERMISSION_CODE);
        }
    }

    // Handle the result of permission request
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == SMS_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                String eventName = etEventName.getText().toString();
                sendSmsNotification(eventName);
            } else {
                Toast.makeText(this, "SMS permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
