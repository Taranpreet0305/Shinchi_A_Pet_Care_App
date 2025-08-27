package com.example.shinchi_apetcareapp;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class NavHeaderMainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // This is just a basic example. You'll need to update your main activity's
        // onCreate method to call the new getCartCount method and update the UI.

        // Assuming you have a TextView in your nav header with id "cart_count_text"
        // TextView cartCountTextView = findViewById(R.id.cart_count_text);
        // int cartCount = CartManager.getCartCount(this);
        // cartCountTextView.setText(String.valueOf(cartCount));
    }
}