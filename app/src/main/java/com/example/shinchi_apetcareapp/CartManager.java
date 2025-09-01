package com.example.shinchi_apetcareapp;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class CartManager {
    private static final String TAG = "CartManager";
    private static CartManager instance;
    private final FirebaseFirestore db;
    private final FirebaseAuth mAuth;
    private DocumentReference userCartRef;
    private Context context;

    private FirebaseAuth.AuthStateListener authStateListener;

    private CartManager() {
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        setupAuthStateListener();
    }

    public static synchronized CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    private void setupAuthStateListener() {
        authStateListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                userCartRef = db.collection("users").document(user.getUid()).collection("cart").document("items");
                Log.d(TAG, "User authenticated. Cart reference updated for user: " + user.getUid());
            } else {
                userCartRef = null;
                Log.d(TAG, "User signed out. Cart reference cleared.");
            }
        };
    }

    public void initialize(Context ctx) {
        this.context = ctx.getApplicationContext();
        mAuth.addAuthStateListener(authStateListener);
    }

    public void release() {
        if (authStateListener != null) {
            mAuth.removeAuthStateListener(authStateListener);
        }
    }

    public void addItem(String itemName, int quantity) {
        if (userCartRef == null) {
            Log.e(TAG, "Cart not initialized. User may not be logged in.");
            Toast.makeText(context, "Please log in to add items to cart.", Toast.LENGTH_SHORT).show();
            return;
        }

        userCartRef.get().addOnSuccessListener(documentSnapshot -> {
            Map<String, Object> updates = new HashMap<>();
            updates.put(itemName, FieldValue.increment(quantity));
            if (documentSnapshot.exists()) {
                userCartRef.update(updates)
                        .addOnSuccessListener(aVoid -> Toast.makeText(context, itemName + " added to cart!", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e -> Log.e(TAG, "Error updating cart", e));
            } else {
                userCartRef.set(updates)
                        .addOnSuccessListener(aVoid -> Toast.makeText(context, itemName + " added to cart!", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e -> Log.e(TAG, "Error creating cart document", e));
            }
        }).addOnFailureListener(e -> Log.e(TAG, "Error checking for cart document", e));
    }

    public void getCartItems(CartUpdateListener listener) {
        if (userCartRef == null) {
            Log.e(TAG, "Cart not initialized. User may not be logged in.");
            listener.onCartUpdate(new HashMap<>());
            return;
        }
        userCartRef.addSnapshotListener((snapshot, e) -> {
            if (e != null) {
                Log.e(TAG, "Listen failed.", e);
                listener.onCartUpdate(null);
                return;
            }
            if (snapshot != null && snapshot.exists()) {
                listener.onCartUpdate(snapshot.getData());
            } else {
                listener.onCartUpdate(new HashMap<>());
            }
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