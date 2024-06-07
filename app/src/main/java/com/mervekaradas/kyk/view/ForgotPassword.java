package com.mervekaradas.kyk.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.mervekaradas.kyk.R;
import com.mervekaradas.kyk.databinding.ActivityForgotPasswordBinding;

public class ForgotPassword extends AppCompatActivity {

    ActivityForgotPasswordBinding binding;
    FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        firebaseAuth = FirebaseAuth.getInstance();

       binding.btnReset.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               email = binding.editTextEmail.getText().toString();

               if (TextUtils.isEmpty(email)) {
                   Toast.makeText(ForgotPassword.this, "Lütfen email adresinizi giriniz", Toast.LENGTH_SHORT).show();
                   return;
               }
               else {
                   checkEmailAndProceed(email);
               }

           }
       });

    }

    private void ResetPassword(){
        binding.forgotPasswordProgressBar.setVisibility(View.VISIBLE);
        binding.btnReset.setVisibility(View.INVISIBLE);


        firebaseAuth.sendPasswordResetEmail(email)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(ForgotPassword.this, "Sifre yenileme maili gonderildi", Toast.LENGTH_SHORT).show();
                        binding.forgotPasswordProgressBar.setVisibility(View.INVISIBLE);
                        binding.btnReset.setVisibility(View.VISIBLE);
                        Intent intent = new Intent(ForgotPassword.this, Login.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ForgotPassword.this, "Sifre yenileme maili gonderilemedi! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        binding.forgotPasswordProgressBar.setVisibility(View.INVISIBLE);
                        binding.btnReset.setVisibility(View.VISIBLE);
                    }
                });
    }

    private void checkEmailAndProceed(String email) {

        // Check if email is already registered
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        firestore.collection("users").whereEqualTo("email", email).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null && querySnapshot.isEmpty()) {
                            // Bu e-posta adresi ile henüz kayıtlı bir kullanıcı yok
                            Toast.makeText(this, "Bu e-posta adresi ile kullanıcı kaydı bulunmamaktadır!", Toast.LENGTH_SHORT).show();
                        } else {
                            // Bu e-posta adresi ile zaten bir kullanıcı kayıtlı
                           ResetPassword();
                        }
                    } else {
                        Toast.makeText(this, "Error checking email", Toast.LENGTH_SHORT).show();
                    }
                });

    }
}