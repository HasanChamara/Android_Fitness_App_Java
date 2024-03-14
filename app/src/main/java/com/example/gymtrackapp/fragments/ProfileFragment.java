package com.example.gymtrackapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.gymtrackapp.R;
import com.example.gymtrackapp.activities.LoginActivity;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView usernameTextView;
    private TextView emailTextView;
    private TextView PaymentTextView;
    private TextView StudentIdTextView;
    private Button logoutButton;

    private LinearLayout linearLayout;

    private ProgressBar loadingSpinner;
    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);


        usernameTextView = rootView.findViewById(R.id.usernameTextView);
        emailTextView = rootView.findViewById(R.id.TextEmail);
        PaymentTextView = rootView.findViewById(R.id.TextPaymentStatus);
        StudentIdTextView = rootView.findViewById(R.id.TextStudentId);
        logoutButton=rootView.findViewById(R.id.logoutbutton);
        linearLayout=rootView.findViewById(R.id.linearLayout);



       loadingSpinner = rootView.findViewById(R.id.loadingSpinner);

        loadingSpinner.setVisibility(View.VISIBLE);
        usernameTextView.setVisibility(View.GONE);
        linearLayout.setVisibility(View.GONE);

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("userEmail", "");
        fetchUsernameFromFirestore(email);


        logoutButton.setOnClickListener(v -> {
            sharedPreferences.edit().clear().apply();


            Intent intent = new Intent(requireContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            requireActivity().finish();
        });
        return rootView;
    }

    private void fetchUsernameFromFirestore(String userEmail) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users")
                .whereEqualTo("email", userEmail)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                        String username = document.getString("name");
                        String email = document.getString("email");
                        String payment = document.getString("paymentStatus");
                        String studentId = document.getString("studentId");
                        usernameTextView.setText(username);
                        emailTextView.setText(email);
                        PaymentTextView.setText(payment);
                        StudentIdTextView.setText(studentId);
                        
                    } else {
                        usernameTextView.setText("User not found");
                    }

                    loadingSpinner.setVisibility(View.GONE);
                    usernameTextView.setVisibility(View.VISIBLE);
                    linearLayout.setVisibility(View.VISIBLE);
                })
                .addOnFailureListener(e -> {
                    usernameTextView.setText("Error fetching username");

                    loadingSpinner.setVisibility(View.GONE);
                    usernameTextView.setVisibility(View.VISIBLE);
                    linearLayout.setVisibility(View.VISIBLE);
                });
    }
}