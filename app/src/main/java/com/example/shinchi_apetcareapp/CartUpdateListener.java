package com.example.shinchi_apetcareapp;
import java.util.Map;

public interface CartUpdateListener {
    void onCartUpdate(Map<String, Object> cartItems);
    void onCartError(String errorMessage);
}
