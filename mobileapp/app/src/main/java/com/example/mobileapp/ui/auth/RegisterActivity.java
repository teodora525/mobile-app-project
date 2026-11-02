package com.example.mobileapp.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mobileapp.R;
import com.example.mobileapp.domain.model.User;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout tilUsername, tilEmail, tilPassword, tilRegion;
    private TextInputEditText etUsername, etEmail, etPassword, etRegion;
    private MaterialButton btnRegister;
    private ProgressBar progressBar;

    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        tilUsername = findViewById(R.id.tilUsername);
        tilEmail = findViewById(R.id.tilEmail);
        tilPassword = findViewById(R.id.tilPassword);
        tilRegion = findViewById(R.id.tilRegion);

        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etRegion = findViewById(R.id.etRegion);

        btnRegister = findViewById(R.id.btnRegister);
        progressBar = findViewById(R.id.progressBar);

        btnRegister.setOnClickListener(v -> performRegistration());

        findViewById(R.id.tvGoToLogin).setOnClickListener(v -> {
            finish(); // Vraca se na LoginActivity
        });
    }

    private void performRegistration() {
        String username = etUsername.getText() != null ? etUsername.getText().toString().trim() : "";
        String email = etEmail.getText() != null ? etEmail.getText().toString().trim() : "";
        String password = etPassword.getText() != null ? etPassword.getText().toString().trim() : "";
        String region = etRegion.getText() != null ? etRegion.getText().toString().trim() : "";

        // Validacija
        tilUsername.setError(null);
        tilEmail.setError(null);
        tilPassword.setError(null);
        tilRegion.setError(null);

        if (TextUtils.isEmpty(username)) {
            tilUsername.setError("Унесите корисничко име");
            return;
        }
        if (username.length() < 3) {
            tilUsername.setError("Корисничко име мора имати бар 3 карактера");
            return;
        }
        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmail.setError("Унесите валидну имејл адресу");
            return;
        }
        if (TextUtils.isEmpty(password) || password.length() < 6) {
            tilPassword.setError("Лозинка мора имати најмање 6 карактера");
            return;
        }
        if (TextUtils.isEmpty(region)) {
            tilRegion.setError("Унесите ваш регион или државу");
            return;
        }

        setLoading(true);

        // 1. Provera jedinstvenosti korisnickog imena u bazi
        db.collection("users")
                .whereEqualTo("username", username)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        setLoading(false);
                        tilUsername.setError("Корисничко име је већ заузето");
                    } else {
                        // Korisnicko ime je slobodno, kreiramo nalog u Firebase Auth
                        createAuthUser(username, email, password, region);
                    }
                })
                .addOnFailureListener(e -> {
                    setLoading(false);
                    Toast.makeText(this, "Грешка при провери података: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void createAuthUser(String username, String email, String password, String region) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    FirebaseUser firebaseUser = authResult.getUser();
                    if (firebaseUser != null) {
                        String uid = firebaseUser.getUid();
                        // 2. Kreiranje User entiteta i upis u Firestore
                        User user = new User(uid, username, email, "USER", region);
                        db.collection("users").document(uid).set(user)
                                .addOnSuccessListener(unused -> {
                                    // 3. Slanje verifikacionog mejla
                                    firebaseUser.sendEmailVerification()
                                            .addOnCompleteListener(task -> {
                                                setLoading(false);
                                                auth.signOut(); // Odjavljujemo dok ne potvrdi mejl
                                                showSuccessDialog();
                                            });
                                })
                                .addOnFailureListener(e -> {
                                    setLoading(false);
                                    Toast.makeText(this, "Грешка при чувању профила: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    setLoading(false);
                    Toast.makeText(this, "Регистрација није успела: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

    private void showSuccessDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Регистрација успешна")
                .setMessage("Послат вам је активациони имејл на вашу адресу. Молимо вас да кликнете на линк у мејлу како бисте активирали налог пре пријаве.")
                .setPositiveButton("У реду", (dialog, which) -> finish())
                .setCancelable(false)
                .show();
    }

    private void setLoading(boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        btnRegister.setEnabled(!isLoading);
    }
}
