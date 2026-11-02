package com.example.mobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mobileapp.ui.auth.LoginActivity;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class    MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        // Провера сесије: ако није улогован или није потврдио имејл, шаљемо га на LoginActivity
        if (currentUser == null || !currentUser.isEmailVerified()) {
            goToLogin();
            return;
        }

        setContentView(R.layout.activity_main);

        TextView tvUserEmail = findViewById(R.id.tvUserEmail);
        MaterialButton btnLogout = findViewById(R.id.btnLogout);

        if (currentUser.getEmail() != null) {
            tvUserEmail.setText("Пријављени сте као: " + currentUser.getEmail());
        }

        btnLogout.setOnClickListener(v -> {
            auth.signOut();
            goToLogin();
        });
    }

    private void goToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
