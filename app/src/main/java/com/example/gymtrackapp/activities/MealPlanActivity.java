package com.example.gymtrackapp.activities;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gymtrackapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class MealPlanActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private TextView calorieIntakeTextView;
    private TextView proteinGramsTextView;
    private TextView fatGramsTextView;
    private TextView carbGramsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_plan);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        calorieIntakeTextView = findViewById(R.id.calorieIntakeTextView);
        proteinGramsTextView = findViewById(R.id.proteinGramsTextView);
        fatGramsTextView = findViewById(R.id.fatGramsTextView);
        carbGramsTextView = findViewById(R.id.carbGramsTextView);

        // Get the current user
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            getMealPlan(userId);
        } else {
            // User is not logged in
            // Redirect to login screen or handle appropriately
        }
    }

    private void getMealPlan(String userId) {
        db.collection("MealPlan")
                .whereEqualTo("userId", userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                // Retrieve meal plan data from the document
                                float calorieIntake = document.getDouble("calorieIntake").floatValue();
                                float proteinGrams = document.getDouble("proteinGrams").floatValue();
                                float fatGrams = document.getDouble("fatGrams").floatValue();
                                float carbGrams = document.getDouble("carbGrams").floatValue();

                                // Set the text views with the retrieved meal plan data
                                calorieIntakeTextView.setText(String.valueOf(calorieIntake));
                                proteinGramsTextView.setText(String.valueOf(proteinGrams));
                                fatGramsTextView.setText(String.valueOf(fatGrams));
                                carbGramsTextView.setText(String.valueOf(carbGrams));
                            }
                        } else {
                            Log.d("MealPlanActivity", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}
