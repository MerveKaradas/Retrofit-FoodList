package com.mervekaradas.kyk.view;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.mervekaradas.kyk.R;
import com.mervekaradas.kyk.databinding.ActivityRegisterBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Register extends AppCompatActivity {

    ActivityRegisterBinding binding;
    ArrayList<String> cities;
    ArrayList<String> dormitory;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register(view);
            }
        });

    }


    private void register(View view) {

        String userNameSurname = binding.etNameUsername.getText().toString().trim();
        String city = binding.autoCompleteTwCity.getText().toString().trim();
        String studentdormitory = binding.autoCompleteTwStudentDormitory.getText().toString().trim();
        String email = binding.etEmail.getText().toString().trim();
        String phone = binding.etPhoneNumber.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();

        if(TextUtils.isEmpty(userNameSurname) || TextUtils.isEmpty(city) || TextUtils.isEmpty(studentdormitory) || TextUtils.isEmpty(email) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(password)){
            Toast.makeText(this, "Lütfen tüm alanları doldurunuz.", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (password.length() < 6) {
            Toast.makeText(this, "Şifreniz en az 6 karakter olmalıdır!", Toast.LENGTH_LONG).show();
        }
        else{
            checkEmailAndProceed(userNameSurname, email, phone, password, city, studentdormitory);

        }
    }

    private void checkEmailAndProceed(String userNameSurname, String email, String phone, String password, String city, String studentdormitory) {

        // Check if email is already registered
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        firestore.collection("users").whereEqualTo("email", email).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null && querySnapshot.isEmpty()) {
                            // Bu e-posta adresi ile henüz kayıtlı bir kullanıcı yok
                            proceedEmailVerification(userNameSurname, email, phone, password, city, studentdormitory);
                        } else {
                            // Bu e-posta adresi ile zaten bir kullanıcı kayıtlı
                            Toast.makeText(this, "Bu e-posta zaten kayıtlı!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "E-posta kontrol edilirken hata oluştu!", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void proceedEmailVerification(String userNameSurname, String email, String phone, String password, String city, String studentdormitory) {
        firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Log.d("TAG", "createUserWithEmail:success");

                            firebaseAuth.getCurrentUser().sendEmailVerification()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                // Email verification sent
                                                Toast.makeText(Register.this, "Kullanıcı başarıyla kaydoldu. E-posta doğrulaması gönderildi. Lütfen e-posta kimliğini doğrulayın!", Toast.LENGTH_SHORT).show();
                                                addUserToFirestore(userNameSurname, email, phone, password, city, studentdormitory);
                                            } else {
                                                // Email verification failed
                                                Toast.makeText(Register.this, "E-posta doğrulaması başarısız oldu!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });



                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Register.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void addUserToFirestore(String userNameSurname, String email, String phone, String password, String city, String studentdormitory) {

        String uid = firebaseAuth.getCurrentUser().getUid();
        HashMap<String, Object> userMap = new HashMap<>();

        userMap.put("name", userNameSurname);
        userMap.put("email", email);
        userMap.put("phone", phone);
        userMap.put("city", city);
        userMap.put("studentdormitory", studentdormitory);

        firestore.collection("users").document(uid)
                .set(userMap)
                .addOnSuccessListener(aVoid -> {
                    Log.d("TAG", "DocumentSnapshot added with ID: " + uid);
                })
                .addOnFailureListener(e -> Log.w("TAG", "Error adding document", e));
    }


    @Override
    protected void onResume() {
        super.onResume();

        cities = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.cities)));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.dropdown_item_city, cities);
        binding.autoCompleteTwCity.setAdapter(adapter);


        binding.autoCompleteTwCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                updateStudentDormitory(parent.getItemAtPosition(position).toString());
                Toast.makeText(Register.this, parent.getItemAtPosition(position).toString() +" yurtları listeleniyor...", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void updateStudentDormitory(String city) {
        binding.autoCompleteTwStudentDormitory.setText(null);
        if (city.equals("Ankara")) {
            dormitory = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.ankara_studentdormitory)));
            binding.autoCompleteTwStudentDormitory.setAdapter(new ArrayAdapter<String>(this, R.layout.dropdown_item_studentdormitory, getResources().getStringArray(R.array.ankara_studentdormitory)));

            binding.autoCompleteTwStudentDormitory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(Register.this,"Yurdunuz : " + parent.getItemAtPosition(position).toString() , Toast.LENGTH_SHORT).show();
                }
            });
        } else if (city.equals("İstanbul")) {
            dormitory = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.istanbul_studentdormitory)));
            binding.autoCompleteTwStudentDormitory.setAdapter(new ArrayAdapter<String>(this, R.layout.dropdown_item_studentdormitory, getResources().getStringArray(R.array.istanbul_studentdormitory)));

            binding.autoCompleteTwStudentDormitory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(Register.this,"Yurdunuz : " + parent.getItemAtPosition(position).toString() , Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}