package com.example.shinchi_apetcareapp;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FieldValue;
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

        userCartRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                // If the document exists, update the item quantity.
                Map<String, Object> updates = new HashMap<>();
                updates.put(itemName, FieldValue.increment(quantity));
                userCartRef.update(updates)
                        .addOnSuccessListener(aVoid -> Toast.makeText(context, itemName + " added to cart!", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e -> {
                            Log.e(TAG, "Error updating cart", e);
                            Toast.makeText(context, "Failed to add " + itemName + " to cart.", Toast.LENGTH_SHORT).show();
                        });
            } else {
                // If the document doesn't exist, create it.
                Map<String, Object> newItem = new HashMap<>();
                newItem.put(itemName, quantity);
                userCartRef.set(newItem)
                        .addOnSuccessListener(aVoid -> Toast.makeText(context, itemName + " added to cart!", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e -> {
                            Log.e(TAG, "Error creating cart document", e);
                            Toast.makeText(context, "Failed to add " + itemName + " to cart.", Toast.LENGTH_SHORT).show();
                        });
            }
        }).addOnFailureListener(e -> {
            Log.e(TAG, "Error checking for cart document existence", e);
            Toast.makeText(context, "Failed to add " + itemName + " to cart.", Toast.LENGTH_SHORT).show();
        });
    }

    public void getCartItems(CartUpdateListener listener) {
        if (userCartRef == null) {
            Log.e(TAG, "Cart not initialized. User may not be logged in.");
            listener.onCartUpdate(new HashMap<>());
            return;
        }
        userCartRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                listener.onCartUpdate(documentSnapshot.getData());
            } else {
                listener.onCartUpdate(new HashMap<>());
            }
        }).addOnFailureListener(e -> {
            Log.e(TAG, "Error fetching cart items", e);
            listener.onCartUpdate(null);
        });
    }

    public void clearCart() {
        if (userCartRef != null) {
            userCartRef.delete()
                    .addOnSuccessListener(aVoid -> Toast.makeText(context, "Cart cleared successfully.", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Log.e(TAG, "Error clearing cart", e));
        }
    }

    public boolean isUserLoggedIn() {
        return mAuth.getCurrentUser() != null;
    }
}
