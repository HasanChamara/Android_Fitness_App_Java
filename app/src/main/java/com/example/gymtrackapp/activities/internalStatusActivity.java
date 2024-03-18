package com.example.gymtrackapp.activities;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.gymtrackapp.MainActivity;
import com.example.gymtrackapp.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class internalStatusActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internal_status);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        String[] statusOptions = {"Normal", "Overcrowd", "Free"};

        Spinner statusSpinner = findViewById(R.id.statusSpinner);
        Button changeStatusButton = findViewById(R.id.changeStatusButton);


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, statusOptions);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        statusSpinner.setAdapter(adapter);

        changeStatusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedStatus = statusSpinner.getSelectedItem().toString();

                Map<String, Object> statusData = new HashMap<>();
                statusData.put("status", selectedStatus);

                db.collection("gymStatus")
                        .document("status")
                        .set(statusData)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                startActivity(new Intent(internalStatusActivity.this, CoachDashActivity.class));
                                Toast.makeText(internalStatusActivity.this, "Status updated successfully", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(internalStatusActivity.this, "Failed to update status", Toast.LENGTH_SHORT).show();
                                Log.e(TAG, "Error updating status", e);
                            }
                        });
            }
        });

    }
}