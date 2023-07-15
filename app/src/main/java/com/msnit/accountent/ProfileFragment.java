package com.msnit.accountent;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Locale;

public class ProfileFragment extends Fragment {
    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        TextView name = view.findViewById(R.id.profileFullName);
        TextView email = view.findViewById(R.id.profileEmail);
        Button logout = view.findViewById(R.id.logoutButton);
        AppCompatImageButton edit = view.findViewById(R.id.editButton);


        FirebaseUser user = auth.getCurrentUser();
        name.setText(user.getDisplayName());
        email.setText(user.getEmail());
        logout.setOnClickListener(v -> {
            auth.signOut();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        });

        Button languageBtn = view.findViewById(R.id.language);

        languageBtn.setOnClickListener(v -> {
            Locale desiredLocale;
            if (languageBtn.getText().equals("EN"))
                desiredLocale = new Locale("AR");
            else
                desiredLocale = new Locale("EN");

            Configuration config = new Configuration();
            config.setLocale(desiredLocale);

            getResources().updateConfiguration(config, getResources().getDisplayMetrics());
            getActivity().recreate();
        });

        return view;
    }
}