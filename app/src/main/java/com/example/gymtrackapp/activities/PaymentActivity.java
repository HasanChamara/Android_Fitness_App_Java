package com.example.gymtrackapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gymtrackapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import lk.payhere.androidsdk.PHConfigs;
import lk.payhere.androidsdk.PHConstants;
import lk.payhere.androidsdk.PHMainActivity;
import lk.payhere.androidsdk.PHResponse;
import lk.payhere.androidsdk.model.InitRequest;
import lk.payhere.androidsdk.model.Item;
import lk.payhere.androidsdk.model.StatusResponse;

public class PaymentActivity extends AppCompatActivity {

    private static final int PAYHERE_REQUEST = 11001;
    private TextView statusTextView;
    private FirebaseFirestore db;

    //4916217501611292
    //5307732125531191


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        statusTextView = findViewById(R.id.status_text_view);

        InitRequest req = new InitRequest();
        req.setMerchantId("1226224");
        req.setMerchantSecret("NzA2NTg0MDI5Mjk5Mjk1Mzk4MjI4MTA4MjM3MDEzMjkyNDEzMjM=");
        req.setCurrency("LKR");
        req.setAmount(1000.00);
        req.setOrderId("230000123");
        req.setItemsDescription("Door bell wireless");
        req.setCustom1("This is the custom message 1");
        req.setCustom2("This is the custom message 2");
        req.getCustomer().setFirstName("Saman");
        req.getCustomer().setLastName("Perera");
        req.getCustomer().setEmail("samanp@gmail.com");
        req.getCustomer().setPhone("+94771234567");
        req.getCustomer().getAddress().setAddress("No.1, Galle Road");
        req.getCustomer().getAddress().setCity("Colombo");
        req.getCustomer().getAddress().setCountry("Sri Lanka");

        req.getCustomer().getDeliveryAddress().setAddress("No.2, Kandy Road");
        req.getCustomer().getDeliveryAddress().setCity("Kadawatha");
        req.getCustomer().getDeliveryAddress().setCountry("Sri Lanka");
        req.getItems().add(new Item(null, "Door bell wireless", 1, 1000.0));

        Intent intent = new Intent(PaymentActivity.this, PHMainActivity.class);
        intent.putExtra(PHConstants.INTENT_EXTRA_DATA, req);
        PHConfigs.setBaseUrl(PHConfigs.SANDBOX_URL);
        startActivityForResult(intent, PAYHERE_REQUEST);



    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PAYHERE_REQUEST && data != null && data.hasExtra(PHConstants.INTENT_EXTRA_RESULT)) {
            PHResponse<StatusResponse> response = (PHResponse<StatusResponse>) data.getSerializableExtra(PHConstants.INTENT_EXTRA_RESULT);
            if (resultCode == Activity.RESULT_OK) {
                if (response != null) {
                    if (response.isSuccess()) {
                        FirebaseAuth mAuth = FirebaseAuth.getInstance();
                        FirebaseUser currentUser = mAuth.getCurrentUser();
                        String userID = currentUser.getUid();
                        updatePaymentStatus(userID);

                        Toast.makeText(PaymentActivity.this, "Payment success", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(PaymentActivity.this, MealPlanActivity.class));
                    } else {

                        Toast.makeText(PaymentActivity.this, "Payment failed", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(PaymentActivity.this, "Payment failed", Toast.LENGTH_SHORT).show();
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                if (response != null) {
                    Toast.makeText(PaymentActivity.this, "Payment failed", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PaymentActivity.this, "User canceled the request", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }
    private void updatePaymentStatus(String userId) {
        db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("users").document(userId);

        userRef
                .update("paymentStatus", "success")
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(PaymentActivity.this, "Payment success", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(PaymentActivity.this, "Payment failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}