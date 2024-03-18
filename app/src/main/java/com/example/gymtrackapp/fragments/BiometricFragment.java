package com.example.gymtrackapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.gymtrackapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BiometricFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BiometricFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Spinner spinnerGoal ,spinnerActivityLevel;
    private RadioGroup radioGroupGender;
    private RadioButton radioButtonMale;
    private RadioButton radioButtonFemale;
    private EditText editTextAge;
    private EditText editTextWeight;
    private EditText editTextHeight;

    private ProgressBar progressBar;

    private Button submitButton;
    public BiometricFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BiometricFragment newInstance(String param1, String param2) {
        BiometricFragment fragment = new BiometricFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_setting, container, false);

        spinnerGoal = rootView.findViewById(R.id.spinnerGoal);
        spinnerActivityLevel = rootView.findViewById(R.id.spinnerGoalActivityLevel);

        radioGroupGender = rootView.findViewById(R.id.radioGroupGender);
        radioButtonMale = rootView.findViewById(R.id.radioButtonMale);
        radioButtonFemale = rootView.findViewById(R.id.radioButtonFemale);

        editTextAge = rootView.findViewById(R.id.editTextAge);
        editTextWeight = rootView.findViewById(R.id.editTextWeight);
        editTextHeight = rootView.findViewById(R.id.editTextHeight);

        progressBar = rootView.findViewById(R.id.progressBar);

        submitButton = rootView.findViewById(R.id.submitButton);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(), R.array.goal_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGoal.setAdapter(adapter);

        View firstItemViewAdapterGoal = adapter.getView(0, null, spinnerGoal);
        firstItemViewAdapterGoal.setEnabled(false);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(requireContext(), R.array.activity_level, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerActivityLevel.setAdapter(adapter2);

        View firstItemViewAdapter2 = adapter2.getView(0, null, spinnerActivityLevel);
        firstItemViewAdapter2.setEnabled(false);

        rootView.findViewById(R.id.submitButton).setOnClickListener(v -> {
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser != null) {
                String userID = currentUser.getUid();
                saveBiometricDetails(userID);
            } else {
                Toast.makeText(requireContext(), "You are not logged in. Please log in to save biometric details.", Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;

    }

    private void saveBiometricDetails(String userID) {
        progressBar.setVisibility(View.VISIBLE);
        submitButton.setEnabled(false);

        String goal = spinnerGoal.getSelectedItem().toString();
        String activityLevel = spinnerActivityLevel.getSelectedItem().toString();
        String gender = ((RadioButton) getView().findViewById(radioGroupGender.getCheckedRadioButtonId())).getText().toString();
        String ageStr = editTextAge.getText().toString();
        String weightStr = editTextWeight.getText().toString();
        String heightStr = editTextHeight.getText().toString();

        if (TextUtils.isEmpty(ageStr) || TextUtils.isEmpty(weightStr) || TextUtils.isEmpty(heightStr)) {
            Toast.makeText(requireContext(), "Please fill in all fields.", Toast.LENGTH_SHORT).show();

            progressBar.setVisibility(View.GONE);
            submitButton.setEnabled(true);

            return;
        }

        int age = Integer.parseInt(ageStr);
        double weight = Double.parseDouble(weightStr);
        double height = Double.parseDouble(heightStr);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference biometricDetailsRef = db.collection("biometricDetails");


        biometricDetailsRef.whereEqualTo("userID", userID).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {

                        DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                        String docID = documentSnapshot.getId();

                        Map<String, Object> biometricDetails = new HashMap<>();
                        biometricDetails.put("goal", goal);
                        biometricDetails.put("activityLevel", activityLevel);
                        biometricDetails.put("gender", gender);
                        biometricDetails.put("age", age);
                        biometricDetails.put("weight", weight);
                        biometricDetails.put("height", height);
                        biometricDetails.put("userID", userID);

                        biometricDetailsRef.document(docID)
                                .set(biometricDetails)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(requireContext(), "Biometric details updated successfully!", Toast.LENGTH_SHORT).show();
                                    clearFieldsAndEnableButton();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(requireContext(), "Error updating biometric details: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    clearFieldsAndEnableButton();
                                });
                    } else {

                        Map<String, Object> biometricDetails = new HashMap<>();
                        biometricDetails.put("userID", userID);
                        biometricDetails.put("goal", goal);
                        biometricDetails.put("activityLevel", activityLevel);
                        biometricDetails.put("gender", gender);
                        biometricDetails.put("age", age);
                        biometricDetails.put("weight", weight);
                        biometricDetails.put("height", height);


                        biometricDetailsRef.add(biometricDetails)
                                .addOnSuccessListener(documentReference -> {
                                    Toast.makeText(requireContext(), "Biometric details saved successfully!", Toast.LENGTH_SHORT).show();
                                    clearFieldsAndEnableButton();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(requireContext(), "Error saving biometric details: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    clearFieldsAndEnableButton();
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(requireContext(), "Error checking biometric details: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    clearFieldsAndEnableButton();
                });
    }

    private void clearFieldsAndEnableButton() {
        spinnerGoal.setSelection(0);
        spinnerActivityLevel.setSelection(0);
        editTextAge.setText("");
        editTextWeight.setText("");
        editTextHeight.setText("");
        radioButtonMale.setChecked(true);

        progressBar.setVisibility(View.GONE);
        submitButton.setEnabled(true);
    }



}