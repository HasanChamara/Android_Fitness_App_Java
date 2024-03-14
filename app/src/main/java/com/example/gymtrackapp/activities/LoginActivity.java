package com.example.gymtrackapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.gymtrackapp.MainActivity;
import com.example.gymtrackapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {
    private EditText emailEditText, passwordEditText;
    private Button loginButton;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        emailEditText = findViewById(R.id.editTextEmail);
        passwordEditText = findViewById(R.id.editTextPassword);
        loginButton = findViewById(R.id.loginButton);

        progressBar = findViewById(R.id.progressBar);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                loginUser(email, password);
            }
        });
    }

    private void loginUser(String email, String password) {

        if ( email.isEmpty() || password.isEmpty() ) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Invalid email address", Toast.LENGTH_SHORT).show();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        loginButton.setEnabled(false);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            checkRegistrationType(user.getUid() , user.getEmail());
                            progressBar.setVisibility(View.GONE);
                            loginButton.setEnabled(true);
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Invalid Email Or Password", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        loginButton.setEnabled(true);
                    }
                });
    }

    private void checkRegistrationType(String userId , String
            email ) {
        db.collection("users").document(userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null && document.exists()) {
                            String registrationType = document.getString("registrationType");

                            if ("member".equals(registrationType)) {
                                Intent customerIntent = new Intent(LoginActivity.this, MainActivity.class);
                                customerIntent.putExtra("userEmail", email);
                                customerIntent.putExtra("Id", userId);
                                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("userEmail", email);
                                editor.apply();

                                startActivity(customerIntent);
                            }
                            else if ("coach".equals(registrationType)) {
                                Intent chefIntent = new Intent(LoginActivity.this, CoachDashActivity.class);
                                chefIntent.putExtra("userEmail", email);
                                chefIntent.putExtra("Id", userId);
                                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("userEmail", email);
                                editor.apply();

                                startActivity(chefIntent);
                            }

                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public void goToRegister(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void goToCoachLogin(View view) {
        Intent intent = new Intent(this, CoachLoginActivity.class);
        startActivity(intent);
    }
}