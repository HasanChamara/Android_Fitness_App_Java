package com.example.gymtrackapp.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gymtrackapp.R;
import com.example.gymtrackapp.adapters.AddedExerciseAdapter;
import com.example.gymtrackapp.models.WorkoutPlan;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddWorkoutScheduleFragment extends Fragment {
    private String userId;
    private Spinner daySpinner;
    private Button addButton , submitExercise;
    private List<List<String>> exerciseList = new ArrayList<>();
    private RecyclerView addedExerciseRecyclerView;
    private AddedExerciseAdapter addedExerciseAdapter;

    private List<WorkoutPlan> workoutPlanList = new ArrayList<>();

    public AddWorkoutScheduleFragment(String userId) {
        this.userId = userId;
    }
    public static AddWorkoutScheduleFragment newInstance(String userId) {
        return new AddWorkoutScheduleFragment(userId);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_workout_schedule, container, false);

        addButton = rootView.findViewById(R.id.addButton);
        submitExercise = rootView.findViewById(R.id.submitExercise);

        addedExerciseRecyclerView = rootView.findViewById(R.id.addedExerciseRecyclerView);

        if (addedExerciseRecyclerView != null) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
            addedExerciseRecyclerView.setLayoutManager(layoutManager);
            addedExerciseAdapter = new AddedExerciseAdapter(exerciseList);
            addedExerciseRecyclerView.setAdapter(addedExerciseAdapter);
        }



        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddWorkoutPlanDialog();
            }
        });
        submitExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveExercisesToFirebase(userId);
            }
        });
        return rootView;
    }

    private void showAddWorkoutPlanDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_exercise, null);
        builder.setView(dialogView);

        final Spinner exerciseSpinner = dialogView.findViewById(R.id.exerciseSpinner);
        final Spinner repsSpinner = dialogView.findViewById(R.id.repsSpinner);
        final Spinner setsSpinner = dialogView.findViewById(R.id.setsSpinner);
        final Spinner daySpinner = dialogView.findViewById(R.id.daySpinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(), R.array.exercises_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        exerciseSpinner.setAdapter(adapter);

        ArrayAdapter<CharSequence> setAdapter = ArrayAdapter.createFromResource(requireContext(), R.array.set, android.R.layout.simple_spinner_item);
        setAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        setsSpinner.setAdapter(setAdapter);

        ArrayAdapter<CharSequence> repAdapter = ArrayAdapter.createFromResource(requireContext(), R.array.reps, android.R.layout.simple_spinner_item);
        repAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        repsSpinner.setAdapter(repAdapter);

        List<String> daysOfWeek = Arrays.asList(getResources().getStringArray(R.array.days_of_week));
        ArrayAdapter<String> dayAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, daysOfWeek);
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daySpinner.setAdapter(dayAdapter);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String exerciseName = exerciseSpinner.getSelectedItem().toString();
                String reps = repsSpinner.getSelectedItem().toString();
                String sets = setsSpinner.getSelectedItem().toString();
                String selectedDay = daySpinner.getSelectedItem().toString();

                if (!exerciseName.isEmpty() && !reps.isEmpty() && !sets.isEmpty()) {

                    List<String> exercise = new ArrayList<>();
                    exercise.add(exerciseName);
                    exercise.add(reps);
                    exercise.add(sets);
                    exercise.add(selectedDay);
                    exerciseList.add(exercise);

                    addedExerciseAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void saveExercisesToFirebase(String userId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        WriteBatch batch = db.batch();

        CollectionReference exercisesRef = db.collection("exercises");

        for (List<String> exercise : exerciseList) {
            DocumentReference exerciseRef = exercisesRef.document();

            Map<String, Object> exerciseData = new HashMap<>();
            exerciseData.put("name", exercise.get(0));
            exerciseData.put("reps", Integer.parseInt(exercise.get(1)));
            exerciseData.put("sets", Integer.parseInt(exercise.get(2)));
            exerciseData.put("day", exercise.get(3));
            exerciseData.put("userId", userId);

            batch.set(exerciseRef, exerciseData);
        }

        batch.commit()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(requireContext(), "Exercises saved successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(requireContext(), "Failed to save exercises: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
