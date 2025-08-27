package com.example.shinchi_apetcareapp; // Your package

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns; // For email validation
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

// If using Jetpack Navigation for the next step from here
// import androidx.navigation.fragment.NavHostFragment;

// Import your generated ViewBinding class for fragment_user_details.xml
import com.example.shinchi_apetcareapp.databinding.FragmentUserDetailsBinding;
// Data model is now defined below
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

// Data Model class defined within the same file as the Fragment
// This is not standard practice for larger projects but acceptable for smaller ones.
class UserDetailsFirestoreModel {
    private String name;
    private String email;
    private String contactNo;
    private String dob;
    private String address;

    // Required empty public constructor for Firestore deserialization
    public UserDetailsFirestoreModel() {
    }

    public UserDetailsFirestoreModel(String name, String email, String contactNo, String dob, String address) {
        this.name = name;
        this.email = email;
        this.contactNo = contactNo;
        this.dob = dob;
        this.address = address;
    }

    // --- Getters (Required by Firestore) ---
    public String getName() {
        return name;
    }
    public String getEmail() {
        return email;
    }
    public String getContactNo() {
        return contactNo;
    }
    public String getDob() {
        return dob;
    }
    public String getAddress() {
        return address;
    }

    // --- Setters (Optional, but can be useful) ---
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setContactNo(String contactNo) { this.contactNo = contactNo; }
    public void setDob(String dob) { this.dob = dob; }
    public void setAddress(String address) { this.address = address; }
}


public class UserDetails extends Fragment { // Class name matches the filename

    private static final String TAG = "UserDetailsFragment"; // Log tag

    // Argument keys (MUST MATCH what UserDetailsActivity uses, and what RegisterPage sends)
    private static final String ARG_USER_UID = "USER_UID";
    private static final String ARG_USER_NAME = "USER_NAME";
    private static final String ARG_USER_EMAIL = "USER_EMAIL";

    private FragmentUserDetailsBinding binding; // ViewBinding reference

    private FirebaseFirestore db;
    private String userUid;
    private String initialName;
    private String initialEmail;

    public UserDetails() {
        // Required empty public constructor
    }

    /**
     * Factory method to create a new instance of this fragment.
     * UserDetailsActivity should use this method to create the fragment instance.
     */
    public static UserDetails newInstance(String userUid, String userName, String userEmail) {
        UserDetails fragment = new UserDetails();
        Bundle args = new Bundle();
        args.putString(ARG_USER_UID, userUid);
        args.putString(ARG_USER_NAME, userName);
        args.putString(ARG_USER_EMAIL, userEmail);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();

        if (getArguments() != null) {
            userUid = getArguments().getString(ARG_USER_UID);
            initialName = getArguments().getString(ARG_USER_NAME);
            initialEmail = getArguments().getString(ARG_USER_EMAIL);
            Log.d(TAG, "Arguments received: UID=" + userUid + ", Name=" + initialName + ", Email=" + initialEmail);
        } else {
            Log.w(TAG, "Fragment arguments were null. This is unexpected if UserDetailsActivity sets them.");
            // As a less ideal fallback, though UserDetailsActivity should always set arguments.
            if (getActivity() != null && getActivity().getIntent() != null) {
                userUid = getActivity().getIntent().getStringExtra("USER_UID"); // Key must match RegisterPage
                initialName = getActivity().getIntent().getStringExtra("USER_NAME"); // Key must match RegisterPage
                initialEmail = getActivity().getIntent().getStringExtra("USER_EMAIL"); // Key must match RegisterPage
                Log.d(TAG, "Fallback from Activity Intent: UID=" + userUid + ", Name=" + initialName + ", Email=" + initialEmail);
            }
        }

        if (userUid == null) {
            Log.e(TAG, "Critical: userUid is null after onCreate. Cannot proceed with saving details.");
            // Further handling can be done in onViewCreated (e.g., disable form, show persistent error)
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment using ViewBinding
        binding = FragmentUserDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Pre-fill fields with data passed from RegisterPage/UserDetailsActivity
        if (initialName != null) {
            binding.UserName.setText(initialName);
        }
        if (initialEmail != null) {
            binding.UserEmail.setText(initialEmail);
            // Consider disabling email editing if it's the one used for auth:
            // binding.UserEmail.setEnabled(false); // If you want to prevent editing of the auth email
        }

        if (userUid == null) {
            Toast.makeText(getContext(), "Critical Error: User ID missing. Please restart registration.", Toast.LENGTH_LONG).show();
            Log.e(TAG, "User UID is null in onViewCreated. Form will be disabled.");
            if (binding.UDbtnRegister != null) { // Ensure button ID in XML is UDbtnRegister
                binding.UDbtnRegister.setEnabled(false); // Disable the register button
            }
            // You might want to disable all input fields as well
            binding.UserName.setEnabled(false);
            binding.UserEmail.setEnabled(false);
            binding.UserContact.setEnabled(false);
            binding.UserDob.setEnabled(false);
            binding.UserAddress.setEnabled(false);
            return; // Stop further execution in this method if UID is missing
        }

        // Set OnClickListener for the register button (ID from your fragment_user_details.xml should be UDbtnRegister)
        binding.UDbtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserDetailsToFirestore();
            }
        });
    }

    private void saveUserDetailsToFirestore() {
        String name = binding.UserName.getText().toString().trim();
        String email = binding.UserEmail.getText().toString().trim(); // This should ideally be the authenticated email
        String contactNo = binding.UserContact.getText().toString().trim();
        String dob = binding.UserDob.getText().toString().trim();
        String address = binding.UserAddress.getText().toString().trim();

        // --- Input Validations ---
        if (TextUtils.isEmpty(name)) {
            binding.UserName.setError("Name is required");
            binding.UserName.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.UserEmail.setError("Valid email is required");
            binding.UserEmail.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(contactNo)) {
            binding.UserContact.setError("Contact number is required");
            binding.UserContact.requestFocus();
            return;
        }
        // Basic DOB validation, consider adding more specific checks (e.g., format, valid date)
        if (TextUtils.isEmpty(dob)) {
            binding.UserDob.setError("Date of Birth is required");
            binding.UserDob.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(address)) {
            binding.UserAddress.setError("Address is required");
            binding.UserAddress.requestFocus();
            return;
        }
        // Consider adding more specific validations (e.g., phone number format using Patterns.PHONE, date format/validity)

        binding.UDbtnRegister.setEnabled(false); // Disable button during save
        // You could show a ProgressBar here

        // Use the UserDetailsFirestoreModel defined in this file
        UserDetailsFirestoreModel userDetailsModel = new UserDetailsFirestoreModel(name, email, contactNo, dob, address);

        if (userUid == null) { // Should have been caught earlier, but a final check before DB operation
            Log.e(TAG, "Critical error: userUid is null just before Firestore operation.");
            Toast.makeText(getContext(), "Cannot save details: User session error.", Toast.LENGTH_LONG).show();
            binding.UDbtnRegister.setEnabled(true);
            return;
        }

        db.collection("users").document(userUid) // Use the UID from Auth as document ID
                .set(userDetailsModel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "User details successfully written for UID: " + userUid);
                        Toast.makeText(getContext(), "Details Registered Successfully!", Toast.LENGTH_SHORT).show();
                        // Re-enable button on success, though typically you navigate away
                        // binding.UDbtnRegister.setEnabled(true);

                        // Navigate to MainActivity or your app's main content area
                        Intent intent = new Intent(getActivity(), MainActivity.class); // Ensure MainActivity exists
                        // Clear back stack so user can't navigate back to details/register pages
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        if (getActivity() != null) {
                            getActivity().finishAffinity(); // Finish UserDetailsActivity and any parent related to registration
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing user details for UID: " + userUid, e);
                        Toast.makeText(getContext(), "Error saving details: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        binding.UDbtnRegister.setEnabled(true); // Re-enable button on failure
                        // Hide ProgressBar here if shown
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Important to prevent memory leaks with ViewBinding in Fragments
    }
}

