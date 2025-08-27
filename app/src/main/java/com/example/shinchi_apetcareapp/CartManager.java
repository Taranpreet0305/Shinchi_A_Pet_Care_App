package com.example.shinchi_apetcareapp;

import android.content.Context;
import android.content.SharedPreferences;

// This class handles the cart count using SharedPreferences for persistence.
public class CartManager {

    private static final String PREFS_NAME = "cart_prefs";
    private static final String CART_COUNT_KEY = "cart_count";

    // Saves the current cart count to SharedPreferences.
    public static void saveCartCount(Context context, int count) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(CART_COUNT_KEY, count);
        editor.apply();
    }

    // Retrieves the saved cart count from SharedPreferences.
    public static int getCartCount(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getInt(CART_COUNT_KEY, 0); // Default value is 0
    }

    // Increments the cart count and saves it.
    public static void incrementCartCount(Context context) {
        int currentCount = getCartCount(context);
        currentCount++;
        saveCartCount(context, currentCount);
    }
}