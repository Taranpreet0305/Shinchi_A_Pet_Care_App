package com.example.shinchi_apetcareapp;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import java.util.HashMap;
import java.util.Map;

public class CartManager {

    private static final String TAG = "CartManager";
    private static CartManager instance;
    private final FirebaseFirestore db;
    private final FirebaseAuth mAuth;
    private DocumentReference userCartRef;
    private Context context;

    private CartManager() {
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }

    public static synchronized CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    public void initialize(Context ctx) {
        this.context = ctx;
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            userCartRef = db.collection("users").document(currentUser.getUid()).collection("cart").document("items");
        } else {
            Toast.makeText(context, "User not authenticated. Cart may not be persistent.", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "User not authenticated for cart operations.");
        }
    }

    public void addItem(String itemName, int quantity) {
        if (userCartRef == null) {
            Log.e(TAG, "Cart not initialized. User may not be logged in.");
            Toast.makeText(context, "Please log in to add items to cart.", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> item = new HashMap<>();
        item.put(itemName, quantity);

        userCartRef.set(item, SetOptions.merge())
                .addOnSuccessListener(aVoid -> Toast.makeText(context, itemName + " added to cart!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error adding item to cart", e);
                    Toast.makeText(context, "Failed to add " + itemName + " to cart.", Toast.LENGTH_SHORT).show();
                });
    }

    public void clearCart() {
        if (userCartRef != null) {
            userCartRef.delete()
                    .addOnSuccessListener(aVoid -> Toast.makeText(context, "Cart cleared successfully.", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Log.e(TAG, "Error clearing cart", e));
        }
    }
}
