package com.example.gymtrackapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.gymtrackapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private EditText nameEditText, studentEditText, cityEditText, emailEditText, passwordEditText, CpasswordEditText;
    private Button registerButton;
    private  ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nameEditText =findViewById(R.id.editTextName);
        studentEditText = findViewById(R.id.editStudentId);
        emailEditText = findViewById(R.id.editTextEmail);
        passwordEditText = findViewById(R.id.editTextPassword);
        CpasswordEditText = findViewById(R.id.editTextConfirmPassword);
        registerButton = findViewById(R.id.registerButton);
        progressBar = findViewById(R.id.progressBar);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameEditText.getText().toString();
                String studentId = studentEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String confirmPassword = CpasswordEditText.getText().toString();

                registerUser(name, studentId, email, password, confirmPassword);
            }
        });

    }

    public void goToLogin(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void registerUser(String name, String studentId, String email, String password, String confirmPassword) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();


        // Validate input fields
        if (name.isEmpty() || studentId.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }
        // Validate email format
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Invalid email address", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate password and confirm password match
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        registerButton.setEnabled(false);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            storeUserDataInFirestore(db, user.getUid(), name,email, studentId);

                            progressBar.setVisibility(View.GONE);
                            registerButton.setEnabled(true);

                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            Toast.makeText(RegisterActivity.this, "The email address is already in use by another account.", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            registerButton.setEnabled(true);
                        } else if (task.getException() instanceof FirebaseAuthWeakPasswordException) {
                            Toast.makeText(RegisterActivity.this, "Password should be at least 6 characters.", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            registerButton.setEnabled(true);
                        } else {
                            Toast.makeText(RegisterActivity.this, "Failed to create account. Please try again.", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            registerButton.setEnabled(true);
                        }
                    }
                });
    }


    private void storeUserDataInFirestore(FirebaseFirestore db, String userId, String name, String email,String studentId) {

        DocumentReference userRef = db.collection("users").document(userId);

        Map<String, Object> userData = new HashMap<>();
        userData.put("email",email);
        userData.put("name", name);
        userData.put("studentId", studentId);
        userData.put("registrationType", "member");
        userData.put("paymentStatus", "pending");

        userRef.set(userData)
                .addOnSuccessListener(aVoid -> {
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(RegisterActivity.this, "Failed to store user data. Please try again.", Toast.LENGTH_SHORT).show();
                });
    }

}