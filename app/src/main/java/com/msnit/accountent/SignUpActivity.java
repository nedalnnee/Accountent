package com.msnit.accountent;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

        Button create = findViewById(R.id.create_user_account);
        EditText emailInput = findViewById(R.id.emailInputSignUp);
        EditText passwordInput = findViewById(R.id.passwordInputSignUp);


        create.setOnClickListener((e) -> createUser(emailInput, passwordInput));

    }

    private void createUser(EditText emailInput, EditText passwordInput) {

        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();

        if (email.trim().isBlank() || password.trim().isBlank()) {
            Toast.makeText(getApplicationContext(), "فاضي", Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        Toast.makeText(getApplicationContext(), "Done ", Toast.LENGTH_SHORT).show();
    }
}