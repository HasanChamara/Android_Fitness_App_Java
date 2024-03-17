package com.example.gymtrackapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.gymtrackapp.R;

public class CoachDashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_dash);
    }

    public void goToManageUser(View view) {
        Intent intent = new Intent(this, ManageUserActivity.class);
        startActivity(intent);
    }

    public void internalStatus(View view) {
        Intent intent = new Intent(this, internalStatusActivity.class);
        startActivity(intent);
    }

}