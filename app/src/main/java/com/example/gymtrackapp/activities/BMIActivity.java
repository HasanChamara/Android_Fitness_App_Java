package com.example.gymtrackapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gymtrackapp.R;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.DecimalFormat;

public class BMIActivity extends AppCompatActivity {

    TextView resultTextView;
    TextView bmiValue;
    TextView categoryTextView;
    TextView importantTextView;
    TextView messageTextView;
    MaterialCardView bmiCardView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmiactivity);


        resultTextView = findViewById(R.id.resultTextView);
        bmiValue = findViewById(R.id.bmiValue);
        categoryTextView = findViewById(R.id.categoryTextView);
        importantTextView = findViewById(R.id.importentTextView);
        messageTextView = findViewById(R.id.messageTextView);
        bmiCardView = findViewById(R.id.bmiCardView);


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
            resultTextView.setText("Biometric details not found for this user please fill your biometric details");
        }).addOnFailureListener(e -> {
            resultTextView.setText("Error fetching biometric details: " + e.getMessage());
        });
    }

    private float calculateBMIValue(float weight, float height) {
        return weight / (height * height);
    }

    private void displayBMIResult(float bmi) {
        String bmiCategory;
        int textColor = 0;
        String bmiMessage;
        String important;
        int borderColor = 0;

        if (bmi < 18.5) {
            bmiCategory = "Underweight";
            textColor = ContextCompat.getColor(this, R.color.yellow_color);
            important = "Important";
            bmiMessage = "You are underweight. Please consider consulting a healthcare professional.";
            borderColor = ContextCompat.getColor(this, R.color.yellow_color);

        } else if (bmi < 25) {
            bmiCategory = "Normal";
            textColor = ContextCompat.getColor(this, R.color.green_color);
            important = "Important";
            bmiMessage = "Congratulations! You have a normal BMI. Keep up the good work!";
            borderColor = ContextCompat.getColor(this, R.color.green_color);

        } else if (bmi < 30) {
            bmiCategory = "Overweight";
            textColor = ContextCompat.getColor(this, R.color.orange_red_color);
            important = "Important";
            bmiMessage = "You are overweight. Consider making healthy lifestyle changes.";
            borderColor = ContextCompat.getColor(this, R.color.orange_red_color);

        } else {
            bmiCategory = "Obese";
            textColor = ContextCompat.getColor(this, R.color.cadmium_red_color);
            important = "Important";
            bmiMessage = "You are obese. It's important to prioritize your health. Please consult a healthcare professional.";
            borderColor = ContextCompat.getColor(this, R.color.cadmium_red_color);
        }

        float floatValue = bmi; // Example float value
        DecimalFormat decimalFormat = new DecimalFormat("#.#"); // Define the format
        String formattedBmiValue = decimalFormat.format(floatValue);
        bmiValue.setText(formattedBmiValue);
        bmiValue.setTextColor(textColor);
        categoryTextView.setText(bmiCategory);
        categoryTextView.setTextColor(textColor);
        importantTextView.setText(important);
        importantTextView.setTextColor(textColor);
        messageTextView.setText(bmiMessage);
        messageTextView.setTextColor(textColor);
        bmiCardView.setStrokeColor(borderColor);
    }
}