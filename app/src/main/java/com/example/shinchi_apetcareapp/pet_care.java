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
        CartManager.getInstance().initialize(this);
        TextView bookNowBtn = findViewById(R.id.book_now_btn);
        if (bookNowBtn != null) {
            bookNowBtn.setOnClickListener(v -> {
                if (CartManager.getInstance().isUserLoggedIn()) {
                    Intent intent = new Intent(pet_care.this, PaymentPage.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(pet_care.this, SignIn.class);
                    startActivity(intent);
                }
            });
        }
    }
}