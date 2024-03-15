package com.example.gymtrackapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.gymtrackapp.R;
import com.example.gymtrackapp.adapters.UserAdapter;
import com.example.gymtrackapp.models.User;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ManageUserActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private UserAdapter adapter;
    private List<User> userList;
    private ProgressBar loadingProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manageuser);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadingProgressBar = findViewById(R.id.loadingProgressBar);

        userList = new ArrayList<>();

        adapter = new UserAdapter(userList, this);
        recyclerView.setAdapter(adapter);

        loadUsers();

    }

    private void loadUsers() {

        userList.clear();

        loadingProgressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection("users");

        usersRef.get()
                .addOnSuccessListener(querySnapshot -> {
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        String userId = document.getId();
                        String name = document.getString("name");
                        String email = document.getString("email");
                        String studentId = document.getString("studentId");
                        String paymentStatus = document.getString("paymentStatus");

                        User user = new User(userId, name, email, studentId, paymentStatus);
                        userList.add(user);
                        loadingProgressBar.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    loadingProgressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    Log.e("ManageUserActivity", "Error fetching users: " + e.getMessage());
                });
    }

}
