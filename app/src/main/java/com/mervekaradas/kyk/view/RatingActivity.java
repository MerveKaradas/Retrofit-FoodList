package com.mervekaradas.kyk.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mervekaradas.kyk.R;
import com.mervekaradas.kyk.databinding.ActivityRatingBinding;

import java.util.HashMap;
import java.util.Map;

public class RatingActivity extends AppCompatActivity {

    ActivityRatingBinding binding;

    private FirebaseFirestore firestore;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityRatingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        binding.btnSubmit.setOnClickListener(v -> {
            // Burada verileri kaydedebilir veya işleyebilirsiniz

            if(binding.ratingBarTaste.getRating() == 0 || binding.ratingBarSatiety.getRating() == 0 || binding.ratingBarHygiene.getRating() == 0){
                Toast.makeText(RatingActivity.this, "Lütfen değerlendirme adımlarını doldurun!", Toast.LENGTH_SHORT).show();
                return;
            }

            showAlertDialog();

        });

    }

    private void saveRatingToFirestore() {

        // Oturum açmış kullanıcıyı al
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid(); // Firebase Authentication'dan gelen kullanıcı kimliği

            Map<String, Object> rating = new HashMap<>();
            rating.put("tasteRating", binding.ratingBarTaste.getRating());
            rating.put("satietyRating", binding.ratingBarSatiety.getRating());
            rating.put("hygieneRating", binding.ratingBarHygiene.getRating());
            rating.put("note", binding.etNote.getText().toString());
            rating.put("date",getIntent().getStringExtra("date"));

            System.out.println("id " +userId);

            // Firestore'a yaz
            firestore.collection("users").document(userId).collection("ratings").add(rating)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(RatingActivity.this, "Değerlendirmeniz kaydedildi!", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> Toast.makeText(RatingActivity.this, "Değerlendirme kaydedilemedi: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(RatingActivity.this, "Lütfen oturum açın.", Toast.LENGTH_SHORT).show();
        }
    }


    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Değerlendirme Gönderiliyor");
        builder.setMessage("Değerlendirmeyi göndermek istiyor musunuz?");
        builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Evet butonuna tıklandığında yapılacak işlemler
                saveRatingToFirestore();

                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Hayır butonuna tıklandığında yapılacak işlemler
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}