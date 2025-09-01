package com.example.shinchi_apetcareapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Profile_Page extends AppCompatActivity {

    private static final String TAG = "Profile_Page";

    private TextView nameTextView, emailTextView, contactTextView, addressTextView;
    private Button editProfileButton;
    private TextView logoutButton;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String userUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        nameTextView = findViewById(R.id.Profile_Name);
        emailTextView = findViewById(R.id.Profile_Email);
        contactTextView = findViewById(R.id.Profile_Contact);
        addressTextView = findViewById(R.id.address_text);
        editProfileButton = findViewById(R.id.editButton);
        logoutButton = findViewById(R.id.logout_button);
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            userUid = currentUser.getUid();
            fetchUserProfile(userUid);
        } else {
            Toast.makeText(this, "User not signed in.", Toast.LENGTH_SHORT).show();
            // Redirect to sign-in page if no user is authenticated
            Intent intent = new Intent(Profile_Page.this, SignIn.class);
            startActivity(intent);
            finish();
        }
        editProfileButton.setOnClickListener(v -> {
            Intent intent = new Intent(Profile_Page.this, UserDetails.class);
            intent.putExtra("USER_UID", userUid);
            intent.putExtra("IS_EDITING", true);
            startActivity(intent);
        });
        logoutButton.setOnClickListener(v -> {
            mAuth.signOut();
            Toast.makeText(Profile_Page.this, "Signed out successfully.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Profile_Page.this, SignIn.class);
            startActivity(intent);
            finish();
        });
    }

    private void fetchUserProfile(String uid) {
        DocumentReference docRef = db.collection("users").document(uid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        nameTextView.setText(document.getString("name"));
                        emailTextView.setText(document.getString("email"));
                        contactTextView.setText(document.getString("contact"));
                        addressTextView.setText(document.getString("address"));
                    } else {
                        Log.d(TAG, "No such document");
                        Toast.makeText(Profile_Page.this, "User details not found.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                    Toast.makeText(Profile_Page.this, "Failed to load profile.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
