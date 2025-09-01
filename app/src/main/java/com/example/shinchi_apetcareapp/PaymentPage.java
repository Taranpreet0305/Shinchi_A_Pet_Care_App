package com.example.shinchi_apetcareapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Locale;

public class PaymentPage extends AppCompatActivity {

    public static final String EXTRA_TOTAL_PRICE = "com.example.shinchi_apetcareapp.TOTAL_PRICE";

    private TextView totalAmountTextView;
    private TextView addressDetailsTextView;
    private Button payNowButton;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_page);

        totalAmountTextView = findViewById(R.id.total_amount);
        addressDetailsTextView = findViewById(R.id.address_details);
        payNowButton = findViewById(R.id.pay_now_button);

        // Initialize Firebase instances
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Get the total price from the intent
        Intent intent = getIntent();
        double totalPrice = intent.getDoubleExtra(EXTRA_TOTAL_PRICE, 0.0);

        // Display the total price with the rupee symbol
        totalAmountTextView.setText(String.format(Locale.getDefault(), "₹%.2f", totalPrice));

        // Fetch user's address from Firestore
        fetchUserAddress();

        payNowButton.setOnClickListener(v -> {
            // Here you would implement actual payment processing logic
            // For now, we'll just show a success message and navigate
            Toast.makeText(PaymentPage.this, "Payment Successful!", Toast.LENGTH_SHORT).show();

            // Navigate to the successful page
            Intent successIntent = new Intent(PaymentPage.this, successful_page.class);
            startActivity(successIntent);
        });
    }

    private void fetchUserAddress() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            DocumentReference userRef = db.collection("users").document(currentUser.getUid());

            userRef.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    String address = documentSnapshot.getString("address");
                    if (address != null && !address.isEmpty()) {
                        addressDetailsTextView.setText(address);
                    } else {
                        addressDetailsTextView.setText("No address found. Please update your profile.");
                    }
                } else {
                    addressDetailsTextView.setText("User details not found.");
                    Log.e("PaymentPage", "User details document does not exist.");
                }
            }).addOnFailureListener(e -> {
                addressDetailsTextView.setText("Failed to load address.");
                Log.e("PaymentPage", "Error fetching user address: " + e.getMessage());
            });
        } else {
            addressDetailsTextView.setText("Please sign in to see your address.");
        }
    }
}
