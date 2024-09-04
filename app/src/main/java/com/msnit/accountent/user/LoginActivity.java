package com.msnit.accountent.user;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.msnit.accountent.R;
import com.msnit.accountent.groups.GroupsActivity;

import java.util.Locale;

public class LoginActivity extends AppCompatActivity {
    EditText emailInput;
    EditText passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        Button loginButton = findViewById(R.id.login);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);

        loginButton.setOnClickListener((view) -> signIn(emailInput, passwordInput));

        findViewById(R.id.go_to_login_from_sign_up_btn).setOnClickListener((view) -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });

        Button languageBtn = findViewById(R.id.language);
        languageBtn.setOnClickListener(view -> {
            Locale desiredLocale;
            if (languageBtn.getText().equals("EN"))
                desiredLocale = new Locale("AR");
            else
                desiredLocale = new Locale("EN");

            Configuration config = new Configuration();
            config.setLocale(desiredLocale);

            getResources().updateConfiguration(config, getResources().getDisplayMetrics());
            reCreate();
        });
    }

    private void reCreate() {
        recreate();
    }

    @Override
    public void onStart() {
        super.onStart();
//        if (currentUser != null) {
//            login();
//        }
    }


    private void signIn(EditText emailInput, EditText passwordInput) {
        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();

        if (email.trim().isBlank() || password.trim().isBlank()) {
            Toast.makeText(getApplicationContext(), "Empty", Toast.LENGTH_SHORT).show();
        }

    }

    private void failedLogin() {
        Toast.makeText(LoginActivity.this, "Authentication failed.",
                Toast.LENGTH_SHORT).show();
    }

    private void login() {
        Intent intent = new Intent(LoginActivity.this, GroupsActivity.class);
        startActivity(intent);
        finish();

    }

}