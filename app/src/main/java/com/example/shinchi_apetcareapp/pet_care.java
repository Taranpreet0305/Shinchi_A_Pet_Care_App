package com.example.shinchi_apetcareapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class pet_care extends AppCompatActivity {

    private static final String TAG = "PetCareActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_care);

        // It's good practice to initialize this here too.
        CartManager.getInstance().initialize(this);

        TextView bookNowBtn = findViewById(R.id.book_now_btn);
        TextView priceTextView = findViewById(R.id.price_text_view);

        if (bookNowBtn != null && priceTextView != null) {
            bookNowBtn.setOnClickListener(v -> {
                if (CartManager.getInstance().isUserLoggedIn()) {

                    String priceString = priceTextView.getText().toString();
                    Log.d(TAG, "1. Original price string: '" + priceString + "'");

                    double priceValue = 0.0;
                    try {
                        // THIS IS THE FIX: This regex removes everything that is NOT a digit.
                        String numericString = priceString.replaceAll("\\D", "");
                        Log.d(TAG, "2. String after keeping only digits: '" + numericString + "'");

                        if (!numericString.isEmpty()) {
                            priceValue = Double.parseDouble(numericString);
                        }
                    } catch (NumberFormatException e) {
                        Log.e(TAG, "Could not parse the string to a double!", e);
                        return; // Stop if there's an error
                    }

                    Log.d(TAG, "3. Final parsed price: " + priceValue);

                    Intent intent = new Intent(pet_care.this, PaymentPage.class);
                    intent.putExtra(PaymentPage.EXTRA_TOTAL_PRICE, priceValue);
                    startActivity(intent);

                } else {
                    Intent intent = new Intent(pet_care.this, SignIn.class);
                    startActivity(intent);
                }
            });
        }
    }
}