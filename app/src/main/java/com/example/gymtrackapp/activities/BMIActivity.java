package com.example.gymtrackapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gymtrackapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class BMIActivity extends AppCompatActivity {

    TextView resultTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmiactivity);


        resultTextView = findViewById(R.id.resultTextView);


        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            String userID = currentUser.getUid();
            fetchBiometricDetailsAndCalculateBMI(userID);
        } else {
            Toast.makeText(BMIActivity.this, "You are not logged in. Please log in to save biometric details.", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchBiometricDetailsAndCalculateBMI(String userID) {


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference biometricDetailsRef = db.collection("biometricDetails");

        Query query = biometricDetailsRef.whereEqualTo("userID", userID);

        query.get().addOnSuccessListener(querySnapshot  -> {
            for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
                Double weightDouble = documentSnapshot.getDouble("weight");
                Double heightDouble = documentSnapshot.getDouble("height");

                if (weightDouble != null && heightDouble != null) {
                    float weight = weightDouble.floatValue();
                    float height = heightDouble.floatValue() / 100;

                    float bmi = calculateBMIValue(weight, height);
                    displayBMIResult(bmi);
                    return;
                }
            }
            resultTextView.setText("Biometric details not found for this user");
        }).addOnFailureListener(e -> {
            resultTextView.setText("Error fetching biometric details: " + e.getMessage());
        });
    }

    private float calculateBMIValue(float weight, float height) {
        return weight / (height * height);
    }

    private void displayBMIResult(float bmi) {
        String bmiCategory;
        if (bmi < 18.5) {
            bmiCategory = "Underweight";
        } else if (bmi < 25) {
            bmiCategory = "Normal weight";
        } else if (bmi < 30) {
            bmiCategory = "Overweight";
        } else {
            bmiCategory = "Obese";
        }

        String result = "BMI: " + String.format("%.2f", bmi) + "\nCategory: " + bmiCategory;
        resultTextView.setText(result);
    }
}