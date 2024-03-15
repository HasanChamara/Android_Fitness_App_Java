package com.example.gymtrackapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gymtrackapp.R;
import com.example.gymtrackapp.models.Biometric;
import com.example.gymtrackapp.models.User;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class ViewUserDataActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private TextView userNameTextView, emailTextView, studentIdTextView, paymentStatusTextView;
    private TextView activityLevelTextView, genderTextView, ageTextView, weightTextView, heightTextView,goalTextView;

    private Button addWorkOutSchedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user_data);

        db = FirebaseFirestore.getInstance();


        userNameTextView = findViewById(R.id.userNameTextView);
        emailTextView = findViewById(R.id.emailTextView);
        studentIdTextView = findViewById(R.id.studentIdTextView);
        paymentStatusTextView = findViewById(R.id.paymentStatusTextView);
        activityLevelTextView = findViewById(R.id.activityLevelTextView);
        genderTextView = findViewById(R.id.genderTextView);
        ageTextView = findViewById(R.id.ageTextView);
        weightTextView = findViewById(R.id.weightTextView);
        heightTextView = findViewById(R.id.heightTextView);
        goalTextView = findViewById(R.id.goalTextView);

        addWorkOutSchedule = findViewById(R.id.workOutScheduleAdd);


        String userId = getIntent().getStringExtra("userId");

        fetchUserData(userId);

        fetchBiometricData(userId);

        addWorkOutSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewUserDataActivity.this, AddScheduleActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });
    }

    private void fetchUserData(String userId) {
        DocumentReference userRef = db.collection("users").document(userId);
        userRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {

                        User user = documentSnapshot.toObject(User.class);
                        if (user != null) {
                            userNameTextView.setText(user.getName());
                            emailTextView.setText(user.getEmail());
                            studentIdTextView.setText(user.getStudentId());
                            paymentStatusTextView.setText(user.getPaymentStatus());
                        }
                    } else {
                        Log.d("ViewUserDataActivity", "No such document");
                    }
                })
                .addOnFailureListener(e -> Log.e("ViewUserDataActivity", "Error fetching user data: " + e.getMessage()));
    }

    private void fetchBiometricData(String userId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference biometricDetailsRef = db.collection("biometricDetails");

        Query query = biometricDetailsRef.whereEqualTo("userID", userId);

        query.get().addOnSuccessListener(querySnapshot -> {
            for (QueryDocumentSnapshot documentSnapshot : querySnapshot) {
                Biometric biometric = documentSnapshot.toObject(Biometric.class);

                if (biometric != null) {
                    goalTextView.setText(biometric.getGoal());
                    activityLevelTextView.setText(biometric.getActivityLevel());
                    genderTextView.setText(biometric.getGender());
                    ageTextView.setText(String.valueOf(biometric.getAge()));
                    weightTextView.setText(String.valueOf(biometric.getWeight()));
                    heightTextView.setText(String.valueOf(biometric.getHeight()));
                }
            }
        }).addOnFailureListener(e -> {
            Log.e("ViewUserDataActivity", "Error getting biometric data: " + e.getMessage());
            Toast.makeText(this, "Error getting biometric data", Toast.LENGTH_SHORT).show();
        });

    }

}
