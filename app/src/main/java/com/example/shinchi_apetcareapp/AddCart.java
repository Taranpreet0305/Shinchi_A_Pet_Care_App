package com.example.shinchi_apetcareapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

public class AddCart extends AppCompatActivity implements CartUpdateListener {

    private TextView cartItemsTextView;
    private TextView totalPriceTextView;
    private ImageView orderButton;

    // This is a mock price list. You should replace this with your actual product prices.
    private final Map<String, Double> productPrices = new HashMap<String, Double>() {{
        put("Cat Food", 100.00);
        put("Dog Food", 125.50);
        put("General Pet Food", 90.00);
        put("Fish Food", 57.75);
        put("Dryer", 500.00);
        put("Pet Shampoo", 150.25);
        put("Nail Clipper", 80.00);
        put("Grooming Set", 300.50);
        put("Scratcher", 250.00);
        put("Cat Toy", 75.00);
        put("Pet Ball", 40.00);
        put("Mouse Toy", 62.50);
        put("Pills Bottle", 180.00);
        put("First Aid Kit", 450.00);
        put("Vaccination", 750.00);
        put("Drug", 225.00);
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

        if (orderButton != null) {
            orderButton.setOnClickListener(v -> {
                if (CartManager.getInstance().isUserLoggedIn()) {
                    // User is logged in, navigate to PaymentPage
                    Intent intent = new Intent(AddCart.this, PaymentPage.class);
                    startActivity(intent);
                } else {
                    // User is not logged in, navigate to SignIn page
                    Intent intent = new Intent(AddCart.this, SignIn.class);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public void onCartUpdate(Map<String, Object> cartItems) {
        if (cartItems == null || cartItems.isEmpty()) {
            cartItemsTextView.setText("Your cart is empty.");
            totalPriceTextView.setText("Total: ₹0.00");
        } else {
            StringBuilder cartContent = new StringBuilder("Items in your cart:\n");
            double total = 0.0;
            for (Map.Entry<String, Object> entry : cartItems.entrySet()) {
                String itemName = entry.getKey();
                int quantity = ((Long) entry.getValue()).intValue(); // Firestore returns Long for numbers
                double price = productPrices.getOrDefault(itemName, 0.0); // Get price from our mock list
                double subtotal = price * quantity;
                total += subtotal;

                cartContent.append("- ").append(itemName)
                        .append(" (x").append(quantity).append(") - ₹").append(String.format("%.2f", subtotal))
                        .append("\n");
            }
            cartItemsTextView.setText(cartContent.toString());
            totalPriceTextView.setText("Total: ₹" + String.format("%.2f", total));
        }
    }
}