package com.example.shinchi_apetcareapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox; // For Terms and Conditions
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shinchi_apetcareapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterPage extends AppCompatActivity {

    private static final String TAG = "RegisterPage";

    private EditText etName, etEmail, etPassword;
    private Button btnRegister;
    private TextView tvSignInLink; // Should be btnSignIn based on your XML
    private CheckBox cbTerms;      // For Terms and Conditions
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        mAuth = FirebaseAuth.getInstance();

        etName = findViewById(R.id.Name);
        etEmail = findViewById(R.id.Email);
        etPassword = findViewById(R.id.Password);
        btnRegister = findViewById(R.id.btnRegister);
        tvSignInLink = findViewById(R.id.btnSignIn); // Corrected ID from your XML
        cbTerms = findViewById(R.id.cbTerms);       // From your XML
        // progressBar = findViewById(R.id.your_progress_bar_id); // Initialize if you added one

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerNewUser();
            }
        });

        tvSignInLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterPage.this, SignIn.class);
                startActivity(intent);
                // finish(); // Optional: if you don't want users to go back to Register page
            }
        });
    }

    private void registerNewUser() {
        final String name = etName.getText().toString().trim();
        final String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            etName.setError("Name is required.");
            etName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email is required.");
            etEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Enter a valid email.");
            etEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password is required.");
            etPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            etPassword.setError("Password must be at least 6 characters long.");
            etPassword.requestFocus();
            return;
        }

        if (!cbTerms.isChecked()) {
            Toast.makeText(RegisterPage.this, "Please agree to the Terms and Conditions.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (progressBar != null) progressBar.setVisibility(View.VISIBLE);
        btnRegister.setEnabled(false);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (progressBar != null) progressBar.setVisibility(View.GONE);
                        btnRegister.setEnabled(true);

                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(RegisterPage.this, "Registration successful. Please complete your profile.", Toast.LENGTH_SHORT).show();

                            if (user != null) {
                                // Navigate to UserDetailsActivity (which will host UserDetails fragment)
                                Intent intent = new Intent(RegisterPage.this, UserDetails.class); // <--- THIS ACTIVITY NEEDS TO EXIST
                                intent.putExtra("USER_UID", user.getUid());
                                intent.putExtra("USER_NAME", name);
                                intent.putExtra("USER_EMAIL", email);
                                // Clear back stack so user can't navigate back to details/register pages once profile is complete
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish(); // Finish RegisterPage
                            } else {
                                Log.w(TAG, "createUserWithEmail:success but user is null");
                                Toast.makeText(RegisterPage.this, "Authentication succeeded but failed to retrieve user.", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterPage.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}

