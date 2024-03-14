package com.example.gymtrackapp;

import static androidx.core.content.ContentProviderCompat.requireContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.gymtrackapp.activities.LoginActivity;
import com.example.gymtrackapp.fragments.HomeFragment;
import com.example.gymtrackapp.fragments.ProfileFragment;
import com.example.gymtrackapp.fragments.SettingFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationViewCustomer);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        SharedPreferences sharedPreferences = this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("userEmail", "");

        if (email.isEmpty()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerCustomer, new HomeFragment())
                .commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment selectedFragment = null;

        switch (item.getItemId()) {
            case R.id.action_setting:
                selectedFragment = new SettingFragment();
                break;
            case R.id.action_home:
                selectedFragment = new HomeFragment();
                break;
            case R.id.action_profile:
                selectedFragment = new ProfileFragment();
                break;
        }

        if (selectedFragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainerCustomer, selectedFragment)
                    .commit();
            return true;
        }

        return false;
    }
}
