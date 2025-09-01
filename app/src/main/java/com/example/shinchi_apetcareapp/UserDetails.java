package com.example.shinchi_apetcareapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import java.util.HashMap;
import java.util.Map;

public class UserDetails extends AppCompatActivity {

    private static final String TAG = "UserDetails";
    private EditText etUserName, etUserEmail, etUserContact, etUserDob, etUserAddress;
    private Button btnRegister;
    private String userUid;
    private FirebaseFirestore db;
    private boolean isEditing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_user_details);
        db = FirebaseFirestore.getInstance();
        etUserName = findViewById(R.id.UserName);
        etUserEmail = findViewById(R.id.UserEmail);
        etUserContact = findViewById(R.id.UserContact);
        etUserDob = findViewById(R.id.UserDob);
        etUserAddress = findViewById(R.id.UserAddress);
        btnRegister = findViewById(R.id.UDbtnRegister);

        Intent intent = getIntent();
        if (intent != null) {
            userUid = intent.getStringExtra("USER_UID");
            isEditing = intent.getBooleanExtra("IS_EDITING", false);
            String name = intent.getStringExtra("USER_NAME");
            String email = intent.getStringExtra("USER_EMAIL");
            if (isEditing) {
                fetchAndPopulateUserDetails(userUid);
                btnRegister.setText("Save Changes");
            } else {
                if (name != null) etUserName.setText(name);
                if (email != null) etUserEmail.setText(email);
            }
        }

        btnRegister.setOnClickListener(v -> saveUserDetails());
    }

    private void fetchAndPopulateUserDetails(String uid) {
        DocumentReference userRef = db.collection("users").document(uid);
        userRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                etUserName.setText(documentSnapshot.getString("name"));
                etUserEmail.setText(documentSnapshot.getString("email"));
                etUserContact.setText(documentSnapshot.getString("contact"));
                etUserDob.setText(documentSnapshot.getString("dob"));
                etUserAddress.setText(documentSnapshot.getString("address"));
            } else {
                Toast.makeText(this, "User data not found.", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> {
            Log.e(TAG, "Error fetching user data", e);
            Toast.makeText(this, "Failed to load user data.", Toast.LENGTH_SHORT).show();
        });
    }

    private void saveUserDetails() {
        String name = etUserName.getText().toString().trim();
        String email = etUserEmail.getText().toString().trim();
        String contact = etUserContact.getText().toString().trim();
        String dob = etUserDob.getText().toString().trim();
        String address = etUserAddress.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(contact) || TextUtils.isEmpty(dob) || TextUtils.isEmpty(address)) {
            Toast.makeText(this, "Please fill all fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> userDetails = new HashMap<>();
        userDetails.put("name", name);
        userDetails.put("email", email);
        userDetails.put("contact", contact);
        userDetails.put("dob", dob);
        userDetails.put("address", address);

        if (userUid != null) {
            DocumentReference userRef = db.collection("users").document(userUid);

            userRef.set(userDetails, SetOptions.merge())
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Details updated successfully!", Toast.LENGTH_SHORT).show();
                        if (isEditing) {
                            finish();
                        } else {
                            FirebaseAuth.getInstance().signOut();
                            Intent intent = new Intent(UserDetails.this, SignIn.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Error updating document", e);
                        Toast.makeText(this, "Failed to update details.", Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(this, "User authentication error. Please sign in again.", Toast.LENGTH_SHORT).show();
        }
    }
}
