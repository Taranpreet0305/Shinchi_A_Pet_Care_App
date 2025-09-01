package com.example.shinchi_apetcareapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class pet_care extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_care);

        // Make sure to initialize the CartManager at the beginning of your app's lifecycle
        CartManager.getInstance().initialize(this);

        TextView bookNowBtn = findViewById(R.id.book_now_btn);

        if (bookNowBtn != null) {
            bookNowBtn.setOnClickListener(v -> {
                if (CartManager.getInstance().isUserLoggedIn()) {
                    // User is logged in, navigate to the payment page.
                    Intent intent = new Intent(pet_care.this, PaymentPage.class);
                    startActivity(intent);
                } else {
                    // User is not logged in, navigate to the sign-in page.
                    Intent intent = new Intent(pet_care.this, SignIn.class);
                    startActivity(intent);
                }
            });
        }
    }
}