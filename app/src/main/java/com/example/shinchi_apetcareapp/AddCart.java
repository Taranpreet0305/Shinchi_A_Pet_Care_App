package com.example.shinchi_apetcareapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddCart extends AppCompatActivity implements CartUpdateListener {

    private TextView cartItemsTextView;
    private TextView totalPriceTextView;
    private ImageView orderButton;
    private double currentTotalPrice = 0.0;

    // Updated product prices to match the latest layouts and add Pet Care service
    private final Map<String, Double> productPrices = new HashMap<String, Double>() {{
        // Services
        put("Pet Care", 2500.00);

        // Food
        put("Cat Food", 350.00);
        put("Dog Food", 420.00);
        put("General Pet Food", 450.00);
        put("Fish Food", 470.00);
        // Grooming
        put("Dryer", 800.00);
        put("Pet Shampoo", 500.00);
        put("Nail Clipper", 350.00);
        put("Grooming Set", 300.00);
        // Toys
        put("Scratcher", 490.00);
        put("Cat Toy", 900.00);
        put("Pet Ball", 200.00);
        put("Mouse Toy", 350.00);
        // Meds
        put("Pain Relief", 500.00);
        put("First-Aid Kit", 550.00);
        put("Vaccination", 150.00);
        put("Antibiotics", 600.00);
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cart);

        cartItemsTextView = findViewById(R.id.cart_items_text_view);
        totalPriceTextView = findViewById(R.id.total_price_text_view);
        orderButton = findViewById(R.id.Orderbtn1);

        CartManager.getInstance().initialize(this);
        CartManager.getInstance().getCartItems(this);

        orderButton.setOnClickListener(v -> {
            if (currentTotalPrice <= 0) {
                Toast.makeText(this, "Your cart is empty.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (CartManager.getInstance().isUserLoggedIn()) {
                Intent intent = new Intent(AddCart.this, PaymentPage.class);
                // Using the constant from PaymentPage for reliability
                intent.putExtra(PaymentPage.EXTRA_TOTAL_PRICE, currentTotalPrice);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Please sign in to proceed.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AddCart.this, SignIn.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CartManager.getInstance().release();
    }

    @Override
    public void onCartUpdate(Map<String, Object> cartItems) {
        if (cartItems == null || cartItems.isEmpty()) {
            cartItemsTextView.setText("Your cart is empty.");
            totalPriceTextView.setText("Total: ₹0.00");
            currentTotalPrice = 0.0;
        } else {
            StringBuilder cartContent = new StringBuilder("Items in your cart:\n");
            double total = 0.0;
            for (Map.Entry<String, Object> entry : cartItems.entrySet()) {
                String itemName = entry.getKey();
                int quantity = ((Long) entry.getValue()).intValue();
                double price = productPrices.getOrDefault(itemName, 0.0);
                double subtotal = price * quantity;
                total += subtotal;

                cartContent.append("- ").append(itemName)
                        .append(" (x").append(quantity).append(") - ₹").append(String.format("%.2f", subtotal))
                        .append("\n");
            }
            currentTotalPrice = total;
            cartItemsTextView.setText(cartContent.toString());
            totalPriceTextView.setText("Total: ₹" + String.format("%.2f", total));
        }
    }

    @Override
    public void onCartError(String message) {
        Toast.makeText(this, "Error loading cart: " + message, Toast.LENGTH_LONG).show();
        cartItemsTextView.setText("Could not load cart items.");
    }
}

