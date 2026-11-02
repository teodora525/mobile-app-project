package com.example.recipeapp;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private TextInputEditText etUsername, etEmail, etPassword;
    private MaterialButton btnRegister;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicijalizacija Firebase alata
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Povezivanje sa XML elementima
        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);

        // Akcija na klik dugmeta
        btnRegister.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(MainActivity.this, "Sva polja su obavezna", Toast.LENGTH_SHORT).show();
                return;
            }

            // Provera da li lozinka ispunjava osnovne kriterijume (Firebase zahteva min 6 karaktera)
            if (password.length() < 6) {
                Toast.makeText(MainActivity.this, "Lozinka mora imati bar 6 karaktera", Toast.LENGTH_SHORT).show();
                return;
            }

            proveriKorisnickoImeIRegistruj(username, email, password);
        });
    }

    private void proveriKorisnickoImeIRegistruj(String username, String email, String password) {
        // Provera da li je korisničko ime jedinstveno u bazi
        db.collection("users").whereEqualTo("username", username).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (!task.getResult().isEmpty()) {
                            Toast.makeText(MainActivity.this, "Korisničko ime je već zauzeto", Toast.LENGTH_SHORT).show();
                        } else {
                            // Korisničko ime je slobodno, prelazimo na kreiranje naloga
                            kreirajNalog(email, password, username);
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Greška pri proveri baze", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void kreirajNalog(String email, String password, String username) {
        // Kreiranje korisnika preko Firebase Auth (ovime se proverava i jedinstvenost imejla)
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            posaljiAktivacioniMejl(user);
                            sacuvajKorisnikaUBazu(user.getUid(), username, email);
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Greška: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void posaljiAktivacioniMejl(FirebaseUser user) {
        user.sendEmailVerification()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Registracija uspešna! Proverite imejl za aktivaciju.", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void sacuvajKorisnikaUBazu(String uid, String username, String email) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("uid", uid);
        userMap.put("username", username);
        userMap.put("email", email);
        userMap.put("role", "korisnik"); // Dodela uloge običnog korisnika nakon potvrde
        userMap.put("publishedRecipesCount", 0);

        db.collection("users").document(uid).set(userMap)
                .addOnSuccessListener(aVoid -> Log.d("Firestore", "Korisnik uspešno sačuvan"))
                .addOnFailureListener(e -> Log.e("Firestore", "Greška pri čuvanju", e));
    }
}