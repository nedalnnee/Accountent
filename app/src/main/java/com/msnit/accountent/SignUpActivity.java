package com.msnit.accountent;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

        Button create = findViewById(R.id.create_user_account);
        EditText nameInput = findViewById(R.id.editTextTextPersonName);
        EditText emailInput = findViewById(R.id.emailInputSignUp);
        EditText passwordInput = findViewById(R.id.passwordInputSignUp);


        create.setOnClickListener((e) -> createUser(nameInput,emailInput, passwordInput));

    }

    private void createUser(EditText nameInput,EditText emailInput, EditText passwordInput) {

        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();

        if (email.trim().isBlank() || password.trim().isBlank()) {
            Toast.makeText(getApplicationContext(), "حقل فارغ!!", Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        setName(nameInput.getText().toString());
                        updateUI(user);
                    } else {
                        Toast.makeText(SignUpActivity.this, "فشل إنشاء حساب!",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setName(String name) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();


        user.updateProfile(profileUpdates);

    }

    private void updateUI(FirebaseUser user) {
        Toast.makeText(getApplicationContext(), "تم إنشاء حسابك - يمكنك الآن تسجيل الدخول ", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}