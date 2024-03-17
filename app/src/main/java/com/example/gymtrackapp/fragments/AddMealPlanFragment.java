package com.example.gymtrackapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gymtrackapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddMealPlanFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddMealPlanFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String userId;
    private FirebaseFirestore db;

    private TextView weightTextView;
    private TextView goalTextView;

    public AddMealPlanFragment(String userId) {
        this.userId = userId;
    }

    public static AddMealPlanFragment newInstance(String userId) {
        return new AddMealPlanFragment(userId);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_meal_plan, container, false);
        db = FirebaseFirestore.getInstance();

        weightTextView = rootView.findViewById(R.id.weightTextView);
        goalTextView = rootView.findViewById(R.id.goalTextView);

        fetchAndDisplayBiometricDetails();

        return rootView;
    }

    private void fetchAndDisplayBiometricDetails() {
        db.collection("biometricDetails")
                .whereEqualTo("userID", userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {

                                Object weightObject = document.get("weight");
                                Float weight = ((Double) weightObject).floatValue();
                                weightTextView.setText(String.valueOf(weight));

                                Object goalObject = document.get("goal");
                                String goal = (String) goalObject;
                                goalTextView.setText(goal);

                                // Call function to calculate calories and macros
                                calculateCaloriesAndMacros(weight, goal);
                            }
                        } else {
                            Toast.makeText(requireContext(), "Error getting documents", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void calculateCaloriesAndMacros(Float weight, String goal) {
        // Constants and calculation logic for calorie and macronutrient calculation go here
        // Copy the function calculateCaloriesAndMacros from previous response and paste it here
        // Remember to adjust the function to handle weight and goal parameters
        // Once added, this function will calculate and display calories and macros based on retrieved weight and goal

        // Constants for calorie calculation
        final int CALORIES_PER_GRAM_PROTEIN = 4;
        final int CALORIES_PER_GRAM_FAT = 9;
        final int CALORIES_PER_GRAM_CARBS = 4;

        // Constants for macronutrient ratios
        final float PROTEIN_RATIO_MUSCLE_BUILDING = 0.3f; // 30% of total calories from protein
        final float FAT_RATIO_MUSCLE_BUILDING = 0.25f;    // 25% of total calories from fat
        final float CARBS_RATIO_MUSCLE_BUILDING = 0.45f;  // 45% of total calories from carbs

        final float PROTEIN_RATIO_FAT_LOSS = 0.4f;        // 40% of total calories from protein
        final float FAT_RATIO_FAT_LOSS = 0.3f;            // 30% of total calories from fat
        final float CARBS_RATIO_FAT_LOSS = 0.3f;          // 30% of total calories from carbs

        // Variables to store calculated values
        float calorieIntake;
        float proteinGrams;
        float fatGrams;
        float carbGrams;

        // Calculate total calorie intake based on weight and goal
        if (goal.equals("Muscle building")) {
            calorieIntake = weight * 22; // Example formula for muscle building goal
        } else if (goal.equals("Fat loss")) {
            calorieIntake = weight * 12; // Example formula for fat loss goal
        } else {
            // Default case if goal is neither muscle building nor fat loss
            calorieIntake = weight * 20; // Example formula for maintenance
        }

        // Calculate macronutrients based on goal
        if (goal.equals("Muscle building")) {
            proteinGrams = calorieIntake * PROTEIN_RATIO_MUSCLE_BUILDING / CALORIES_PER_GRAM_PROTEIN;
            fatGrams = calorieIntake * FAT_RATIO_MUSCLE_BUILDING / CALORIES_PER_GRAM_FAT;
            carbGrams = calorieIntake * CARBS_RATIO_MUSCLE_BUILDING / CALORIES_PER_GRAM_CARBS;
        } else if (goal.equals("Fat loss")) {
            proteinGrams = calorieIntake * PROTEIN_RATIO_FAT_LOSS / CALORIES_PER_GRAM_PROTEIN;
            fatGrams = calorieIntake * FAT_RATIO_FAT_LOSS / CALORIES_PER_GRAM_FAT;
            carbGrams = calorieIntake * CARBS_RATIO_FAT_LOSS / CALORIES_PER_GRAM_CARBS;
        } else {
            // Default case if goal is neither muscle building nor fat loss
            // You can define different macronutrient ratios for maintenance
            proteinGrams = 0;
            fatGrams = 0;
            carbGrams = 0;
        }

        // Now you can use the calculated values as needed
        System.out.println("Calorie Intake: " + calorieIntake);
        System.out.println("Protein Grams: " + proteinGrams);
        System.out.println("Fat Grams: " + fatGrams);
        System.out.println("Carb Grams: " + carbGrams);

    }
}
