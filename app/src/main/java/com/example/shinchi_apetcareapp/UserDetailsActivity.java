package com.example.shinchi_apetcareapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.shinchi_apetcareapp.R;

public class UserDetailsActivity extends AppCompatActivity {

    private static final String TAG = "UserDetailsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details); // Link to the layout file created above

        if (savedInstanceState == null) { // Ensure fragment is added only once
            // Retrieve the data passed from RegisterPage
            String uid = getIntent().getStringExtra("USER_UID");
            String name = getIntent().getStringExtra("USER_NAME");
            String email = getIntent().getStringExtra("USER_EMAIL");

            if (uid == null) {
                Log.e(TAG, "USER_UID is null. Cannot proceed to UserDetails fragment.");
                // Handle this error appropriately - maybe finish activity or show error message
                Toast.makeText(this, "Critical error: User session not found. Please try registering again.", Toast.LENGTH_LONG).show();
                finish(); // Close this activity as it cannot function without UID
                return;
            }

            // Create an instance of your UserDetails fragment
            // UserDetails fragment = new UserDetails(); // If not using newInstance factory method

            // Create a Bundle to pass arguments to the fragment
            // Bundle args = new Bundle();
            // args.putString("USER_UID", uid); // Using the same keys as RegisterPage for consistency
            // args.putString("USER_NAME", name);
            // args.putString("USER_EMAIL", email);
            // fragment.setArguments(args);

            // Or, preferably, use the newInstance factory method if defined in UserDetails.java
            com.example.shinchi_apetcareapp.UserDetails fragment = com.example.shinchi_apetcareapp.UserDetails.newInstance(uid, name, email);


            // Begin a transaction to replace the FrameLayout with the fragment
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container_user_details, fragment);
            // transaction.addToBackStack(null); // Optional: if you want this fragment to be on the back stack
            transaction.commit();
        }
    }
}
