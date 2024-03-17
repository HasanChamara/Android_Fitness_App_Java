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


                            }
                        } else {
                            Toast.makeText(requireContext(), "Error getting documents", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }



}