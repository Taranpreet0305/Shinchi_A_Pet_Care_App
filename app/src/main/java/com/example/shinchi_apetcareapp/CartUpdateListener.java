package com.example.shinchi_apetcareapp;

/**
 * Interface for fragments to communicate with the hosting activity.
 * Fragments will use this to signal when an item should be added to the cart.
 */
public interface CartUpdateListener {
    void onAddToCart();
}
