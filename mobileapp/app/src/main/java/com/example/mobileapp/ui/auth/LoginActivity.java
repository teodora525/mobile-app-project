package com.example.mobileapp.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mobileapp.MainActivity;
import com.example.mobileapp.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout tilEmailOrUsername, tilPassword;
    private TextInputEditText etEmailOrUsername, etPassword;
    private MaterialButton btnLogin;
    private ProgressBar progressBar;

    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        tilEmailOrUsername = findViewById(R.id.tilEmailOrUsername);
        tilPassword = findViewById(R.id.tilPassword);
        etEmailOrUsername = findViewById(R.id.etEmailOrUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        progressBar = findViewById(R.id.progressBar);

        btnLogin.setOnClickListener(v -> performLogin());

        findViewById(R.id.tvGoToRegister).setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });

        findViewById(R.id.tvForgotPassword).setOnClickListener(v -> {
            startActivity(new Intent(this, ForgotPasswordActivity.class));
        });
    }

    private void performLogin() {
        String input = etEmailOrUsername.getText() != null ? etEmailOrUsername.getText().toString().trim() : "";
        String password = etPassword.getText() != null ? etPassword.getText().toString().trim() : "";

        tilEmailOrUsername.setError(null);
        tilPassword.setError(null);

        if (TextUtils.isEmpty(input)) {
            tilEmailOrUsername.setError("Унесите имејл или корисничко име");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            tilPassword.setError("Унесите лозинку");
            return;
        }

        setLoading(true);

        // Спецификација: пријава може путем имејла или корисничког имена
        if (input.contains("@")) {
            // Унет је имејл директно
            signInWithEmail(input, password);
        } else {
            // Унето је корисничко име - прво проналазимо повезан имејл у бази
            db.collection("users")
                    .whereEqualTo("username", input)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (queryDocumentSnapshots.isEmpty()) {
                            setLoading(false);
                            tilEmailOrUsername.setError("Корисничко име није пронађено");
                        } else {
                            QueryDocumentSnapshot doc = (QueryDocumentSnapshot) queryDocumentSnapshots.getDocuments().get(0);
                            String email = doc.getString("email");
                            if (email != null) {
                                signInWithEmail(email, password);
                            } else {
                                setLoading(false);
                                tilEmailOrUsername.setError("Неважећи подаци о налогу");
                            }
                        }
                    })
                    .addOnFailureListener(e -> {
                        setLoading(false);
                        Toast.makeText(this, "Грешка при претрази: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void signInWithEmail(String email, String password) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    FirebaseUser user = authResult.getUser();
                    if (user != null) {
                        // Освежавамо статус како би проверили активациони мејл
                        user.reload().addOnCompleteListener(task -> {
                            setLoading(false);
                            if (user.isEmailVerified()) {
                                // Успешна пријава - прелазак на главни екран
                                goToMainActivity();
                            } else {
                                // Није активиран мејл
                                showNotVerifiedDialog(user);
                            }
                        });
                    } else {
                        setLoading(false);
                    }
                })
                .addOnFailureListener(e -> {
                    setLoading(false);
                    Toast.makeText(this, "Неуспешна пријава: Проверите податке (" + e.getMessage() + ")", Toast.LENGTH_LONG).show();
                });
    }

    private void showNotVerifiedDialog(FirebaseUser user) {
        new AlertDialog.Builder(this)
                .setTitle("Налог није активиран")
                .setMessage("Ваш имејл још увек није верификован. Молимо вас кликните на линк који смо вам послали на мејл.")
                .setPositiveButton("У реду", (dialog, which) -> auth.signOut())
                .setNeutralButton("Пошаљи поново мејл", (dialog, which) -> {
                    user.sendEmailVerification().addOnCompleteListener(t -> {
                        Toast.makeText(this, "Активациони мејл је поново послат.", Toast.LENGTH_SHORT).show();
                        auth.signOut();
                    });
                })
                .setCancelable(false)
                .show();
    }

    private void goToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void setLoading(boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        btnLogin.setEnabled(!isLoading);
    }
}
