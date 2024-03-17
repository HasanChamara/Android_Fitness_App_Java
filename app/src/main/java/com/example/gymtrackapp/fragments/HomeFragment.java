package com.example.gymtrackapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gymtrackapp.R;
import com.example.gymtrackapp.activities.BMIActivity;
import com.example.gymtrackapp.activities.MealPlanActivity;
import com.example.gymtrackapp.activities.PaymentActivity;
import com.example.gymtrackapp.activities.WorkoutActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FirebaseFirestore db;
    private LinearLayout statusLayout;
    private TextView gymStatusTextView;
    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);


       LinearLayout mealPlanButton = rootView.findViewById(R.id.mealPlanButton);
        LinearLayout BMIButton = rootView.findViewById(R.id.BMIButton);
        LinearLayout workOutButton = rootView.findViewById(R.id.workOutButton);
        statusLayout = rootView.findViewById(R.id.statusLayout);


        mealPlanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMealPlan();
            }
        });
        BMIButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToBMI();
            }
        });
        workOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goWorkOut();
            }
        });

        gymStatusTextView = rootView.findViewById(R.id.gymStatusTextView);
        db = FirebaseFirestore.getInstance();
        fetchAndDisplayGymStatus();

        return rootView;
    }

    public void goToMealPlan() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("userEmail", "");

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users")
                .whereEqualTo("email", userEmail)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                        String paymentStatus = document.getString("paymentStatus");

                        if ("pending".equals(paymentStatus)) {
                            Toast.makeText(requireContext(), "Please make a payment first", Toast.LENGTH_SHORT).show();
                            Intent paymentIntent = new Intent(requireContext(), PaymentActivity.class);
                            startActivity(paymentIntent);
                        } else {
                            Intent workoutPlanIntent = new Intent(requireContext(), MealPlanActivity.class);
                            startActivity(workoutPlanIntent);
                        }
                    } else {

                        Toast.makeText(requireContext(), "User not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(requireContext(), "failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
    public void goToBMI() {
            Intent bmiActivity = new Intent(requireContext(), BMIActivity.class);
            startActivity(bmiActivity);
    }
    public void goWorkOut() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("userEmail", "");

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users")
                .whereEqualTo("email", userEmail)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                        String paymentStatus = document.getString("paymentStatus");

                        if ("pending".equals(paymentStatus)) {
                            Toast.makeText(requireContext(), "Please make a payment first", Toast.LENGTH_SHORT).show();
                            Intent paymentIntent = new Intent(requireContext(), PaymentActivity.class);
                            startActivity(paymentIntent);
                        } else {
                            Intent workoutPlanIntent = new Intent(requireContext(), WorkoutActivity.class);
                            startActivity(workoutPlanIntent);
                        }
                    } else {

                        Toast.makeText(requireContext(), "User not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(requireContext(), "failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void fetchAndDisplayGymStatus() {
        db.collection("gymStatus")
                .document("status") // Assuming gym status is stored under the document named "status"
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String gymStatus = document.getString("status");
                                gymStatusTextView.setText(gymStatus);
                                if (gymStatus.equals("Overcrowd")) {
                                    statusLayout.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.overcrowd_background));
                                    gymStatusTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.overcrowd_text));
                                } else if (gymStatus.equals("Normal")) {
                                    statusLayout.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.normal_background));
                                    gymStatusTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.normal_text));
                                } else if (gymStatus.equals("Free")) {
                                    statusLayout.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.free_background));
                                    gymStatusTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.free_text));
                                }
                            } else {
                                Toast.makeText(requireContext(), "Gym status document does not exist", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(requireContext(), "Failed to fetch gym status", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}