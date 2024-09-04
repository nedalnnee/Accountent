package com.msnit.accountent.user;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.msnit.accountent.R;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        Button create = findViewById(R.id.create_user_account);
        EditText nameInput = findViewById(R.id.editTextTextPersonName);
        EditText emailInput = findViewById(R.id.emailInputSignUp);
        EditText passwordInput = findViewById(R.id.passwordInputSignUp);


        create.setOnClickListener((e) -> createUser(nameInput, emailInput, passwordInput));

    }

    private void createUser(EditText nameInput, EditText emailInput, EditText passwordInput) {

        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();

        if (email.trim().isBlank() || password.trim().isBlank()) {
            Toast.makeText(getApplicationContext(), "حقل فارغ!!", Toast.LENGTH_SHORT).show();
            return;
        }

    }

    private void setName(String name) {

    }

    private void updateUI(Object user) {
        Toast.makeText(getApplicationContext(), "تم إنشاء حسابك - يمكنك الآن تسجيل الدخول ", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}