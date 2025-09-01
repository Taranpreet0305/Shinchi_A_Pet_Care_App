package com.example.shinchi_apetcareapp;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import org.json.JSONObject;
import java.util.Locale;

public class PaymentPage extends AppCompatActivity implements PaymentResultListener {

    public static final String EXTRA_TOTAL_PRICE = "TOTAL_PRICE";
    private static final String TAG = "PaymentPage";
    private TextView totalAmountTextView;
    private TextView addressDetailsTextView;
    private Button payNowButton;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private double totalPrice = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_page);
        Checkout.preload(getApplicationContext());
        totalAmountTextView = findViewById(R.id.total_amount);
        addressDetailsTextView = findViewById(R.id.address_details);
        payNowButton = findViewById(R.id.pay_now_button);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        Intent intent = getIntent();
        totalPrice = intent.getDoubleExtra(EXTRA_TOTAL_PRICE, 0.0);
        totalAmountTextView.setText(String.format(Locale.getDefault(), "₹%.2f", totalPrice));
        fetchUserAddress();
        payNowButton.setOnClickListener(v -> startPayment());
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
                }
            }).addOnFailureListener(e -> {
                addressDetailsTextView.setText("Failed to load address.");
                Log.e(TAG, "Error fetching user address", e);
            });
        } else {
            addressDetailsTextView.setText("Please sign in to see your address.");
        }
    }

    private void startPayment() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "You need to be logged in to pay.", Toast.LENGTH_SHORT).show();
            return;
        }
        final Activity activity = this;
        final Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_RCRP7NkuZkb8Ed");
        try {
            JSONObject options = new JSONObject();
            options.put("name", "Shinchi Pet Care");
            options.put("description", "Order Payment");
            options.put("theme.color", "#3399cc");
            options.put("currency", "INR");
            options.put("amount", (int) (totalPrice * 100));
            JSONObject prefill = new JSONObject();
            prefill.put("email", currentUser.getEmail());
            options.put("prefill", prefill);
            checkout.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Error starting Razorpay Checkout", e);
        }
    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            Toast.makeText(this, "Payment Successful: " + razorpayPaymentID, Toast.LENGTH_SHORT).show();
            CartManager.getInstance().clearCart();
            Intent successIntent = new Intent(PaymentPage.this, successful_page.class);
            successIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(successIntent);
            finish();
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentSuccess", e);
        }
    }

    @Override
    public void onPaymentError(int code, String response) {
        try {
            Toast.makeText(this, "Payment failed: " + response, Toast.LENGTH_LONG).show();
            Intent failureIntent = new Intent(PaymentPage.this, unsuccessful_page.class);
            startActivity(failureIntent);
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentError", e);
        }
    }
}

