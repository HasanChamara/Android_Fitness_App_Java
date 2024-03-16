package com.example.gymtrackapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gymtrackapp.R;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        statusTextView = findViewById(R.id.status_text_view);

            InitRequest req = new InitRequest();
            req.setMerchantId("1223594");
            req.setMerchantSecret("MTIxNjYwMjk3NjY3MjEzMTkxMjIzMzM1ODkwMDAyODY4NjA1ODA3");
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
                        String msg = "Payment successful. Transaction ID: " + response.getData();
                        statusTextView.setText(msg);

                        startActivity(new Intent(PaymentActivity.this, MealPlanActivity.class));
                    } else {
                        String errorMsg = "Payment failed. Reason: " + response.getData().getMessage();
                        statusTextView.setText(errorMsg);

                        Toast.makeText(PaymentActivity.this, "Payment failed", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    statusTextView.setText("Result: no response");
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                if (response != null) {
                    statusTextView.setText(response.toString());
                } else {
                    statusTextView.setText("User canceled the request");
                }
            }
        }

    }
}