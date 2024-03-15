package com.example.gymtrackapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manageuser);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        userList = new ArrayList<>();

        adapter = new UserAdapter(userList);
        recyclerView.setAdapter(adapter);

        loadUsers();
    }

    private void loadUsers() {

        userList.clear();

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
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Log.e("ManageUserActivity", "Error fetching users: " + e.getMessage());
                });
    }

}
