package com.example.shinchi_apetcareapp; // Or your actual package

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView; // If you plan to load the profile image
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
// For loading images with Glide or Picasso (optional, add dependency if you use it)
// import com.bumptech.glide.Glide;

public class Profile_Page extends AppCompatActivity { // Renamed from ProfileActivity to match context

    private static final String TAG = "Profile_Page";

    private ShapeableImageView profileImage;
    private TextView welcomeText, profileName, profileEmail, profileContact, profileAddress;
    private TextView logoutButton; // Changed from Button to TextView to match XML

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        currentUser = mAuth.getCurrentUser();

        // Initialize Views
        profileImage = findViewById(R.id.profile_image);
        welcomeText = findViewById(R.id.welcome_text);
        profileName = findViewById(R.id.Profile_Name);
        profileEmail = findViewById(R.id.Profile_Email);
        profileContact = findViewById(R.id.Profile_Contact);
        profileAddress = findViewById(R.id.address_text); // Matches your XML ID
        logoutButton = findViewById(R.id.logout_button);


        if (currentUser == null) {
            // No user is signed in, redirect to SignInActivity
            Toast.makeText(this, "No user signed in. Redirecting to sign in.", Toast.LENGTH_LONG).show();
            navigateToSignIn();
            return; // Important to prevent further execution
        }

        // User is signed in, load their data
        loadUserProfile();

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Toast.makeText(Profile_Page.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
                navigateToSignIn();
            }
        });
    }

    private void loadUserProfile() {
        if (currentUser != null) {
            // Set email from Firebase Auth
            if (currentUser.getEmail() != null) {
                profileEmail.setText(currentUser.getEmail());
            } else {
                profileEmail.setText("Email not available");
            }

            // Fetch additional details from Firestore
            DocumentReference userDocRef = db.collection("users").document(currentUser.getUid());
            userDocRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        Log.d(TAG, "User data fetched: " + document.getData());
                        String name = document.getString("name"); // Assuming 'name' field in Firestore
                        String contact = document.getString("contact"); // Assuming 'contact' field
                        String address = document.getString("address"); // Assuming 'address' field
                        // String profileImageUrl = document.getString("profileImageUrl"); // Assuming 'profileImageUrl' field

                        if (name != null && !name.isEmpty()) {
                            profileName.setText(name);
                            welcomeText.setText("Welcome, " + name + "!"); // Update welcome text
                        } else {
                            profileName.setText("Name not set");
                            welcomeText.setText("Welcome!");
                        }

                        if (contact != null && !contact.isEmpty()) {
                            profileContact.setText(contact);
                        } else {
                            profileContact.setText("Contact not set");
                        }

                        if (address != null && !address.isEmpty()) {
                            profileAddress.setText(address);
                        } else {
                            profileAddress.setText("Address not set");
                        }

                        // Optional: Load profile image using Glide or Picasso
                        // if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                        // Glide.with(Profile_Page.this).load(profileImageUrl).circleCrop().into(profileImage);
                        // } else {
                        // profileImage.setImageResource(R.drawable.ologo); // Default image
                        // }

                    } else {
                        Log.d(TAG, "No such document for user details in Firestore");
                        // Set default texts if no document or fields are found
                        profileName.setText("Name not found");
                        welcomeText.setText("Welcome!");
                        profileContact.setText("Contact not found");
                        profileAddress.setText("Address not found");
                    }
                } else {
                    Log.e(TAG, "Error fetching user details from Firestore: ", task.getException());
                    Toast.makeText(Profile_Page.this, "Failed to load profile details.", Toast.LENGTH_SHORT).show();
                    // Set error texts
                    profileName.setText("Error loading name");
                    welcomeText.setText("Welcome!");
                    profileContact.setText("Error loading contact");
                    profileAddress.setText("Error loading address");
                }
            });
        }
    }

    private void navigateToSignIn() {
        Intent intent = new Intent(Profile_Page.this, SignIn.class);
        // Clear activity stack so user cannot go back to Profile_Page after logging out
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish(); // Finish Profile_Page activity
    }

    // Optional: Call this if you want to allow users to update their profile image
    // private void openImageChooser() {
    // Intent intent = new Intent();
    // intent.setType("image/*");
    // intent.setAction(Intent.ACTION_GET_CONTENT);
    // startActivityForResult(Intent.createChooser(intent, "Select Profile Image"), PICK_IMAGE_REQUEST);
    // }

    // @Override
    // protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    // super.onActivityResult(requestCode, resultCode, data);
    // if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
    // Uri imageUri = data.getData();
    // // Upload this imageUri to Firebase Storage and then update the URL in Firestore and load into ImageView
    // // e.g., uploadImageToFirebaseStorage(imageUri);
    // }
    // }
    // private static final int PICK_IMAGE_REQUEST = 1;
}
