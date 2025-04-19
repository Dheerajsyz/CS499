package com.dheeraj.snhu_dheeraj_kollapaneni;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    private EditText etUsername, etPassword, etEmail, etPhone;
    private Button btnSignUp;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        btnSignUp = findViewById(R.id.btnSignUp);

        dbHelper = new DatabaseHelper(this);

        btnSignUp.setOnClickListener(v -> {
            String username = etUsername.getText().toString();
            String password = etPassword.getText().toString();
            String email = etEmail.getText().toString();
            String phone = etPhone.getText().toString();

            if (validateInput(username, password, email, phone)) {
                // Check if the username or email already exists
                if (dbHelper.checkUserExists(username, email)) {
                    Toast.makeText(SignUpActivity.this, "Username or email already exists", Toast.LENGTH_SHORT).show();
                } else {
                    if (dbHelper.addUser(username, password, email, phone)) {
                        Toast.makeText(SignUpActivity.this, "User registered successfully!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                        finish();
                    } else {
                        Toast.makeText(SignUpActivity.this, "Registration failed!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private boolean validateInput(String username, String password, String email, String phone) {
        // Username policy: must be at least 4 characters and alphanumeric
        if (TextUtils.isEmpty(username) || username.length() < 4 || !username.matches("[a-zA-Z0-9]+")) {
            Toast.makeText(this, "Username must be at least 4 characters and alphanumeric", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Password policy: at least 8 characters, contains uppercase, lowercase, digit, special character
        if (!isValidPassword(password)) {
            Toast.makeText(this, "Password must be at least 8 characters long, and contain an uppercase letter, lowercase letter, digit, and special character", Toast.LENGTH_LONG).show();
            return false;
        }

        // Email policy: valid email format
        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Phone policy: valid 10-digit phone number (assuming US phone number)
        if (TextUtils.isEmpty(phone) || !phone.matches("\\d{10}")) {
            Toast.makeText(this, "Please enter a valid 10-digit phone number", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private boolean isValidPassword(String password) {
        String passwordPattern = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[a-z])(?=.*[@#$%^&+=!]).{8,}$";
        Pattern pattern = Pattern.compile(passwordPattern);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
}
