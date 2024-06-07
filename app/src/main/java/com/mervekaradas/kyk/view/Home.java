package com.mervekaradas.kyk.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mervekaradas.kyk.R;
import com.mervekaradas.kyk.databinding.ActivityHomeBinding;
import com.mervekaradas.kyk.model.MenuModel;
import com.mervekaradas.kyk.service.MenuAPI;
import com.mervekaradas.kyk.view.fragments.AksamMenuFragment;
import com.mervekaradas.kyk.view.fragments.SabahMenuFragment;
import com.mervekaradas.kyk.viewmodel.MenuViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Home extends AppCompatActivity {

    ActivityHomeBinding binding;

    ArrayList<MenuModel> menuListBreakfast;
    ArrayList<MenuModel> menuListDinner;

    private String BASE_URL = "https://raw.githubusercontent.com/";
    Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        Gson gson = new GsonBuilder().setLenient().create();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        loadDataBreakfast();
        loadDataDinner();

        setSupportActionBar(binding.main.findViewById(R.id.myToolBar));


        loadFragment(new SabahMenuFragment());


       binding.btnSabahFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new SabahMenuFragment());
            }
        });

        binding.btnAksamFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new AksamMenuFragment());
            }
        });

    }

    private void loadDataBreakfast(){


        MenuAPI menuAPI = retrofit.create(MenuAPI.class);
        MenuViewModel menuViewModel = new ViewModelProvider(this).get(MenuViewModel.class);

        Call<List<MenuModel>> call = menuAPI.getDataBreakfast();

        call.enqueue(new Callback<List<MenuModel>>() {

            @Override
            public void onResponse(Call<List<MenuModel>> call, Response<List<MenuModel>> response) {

                System.out.println(response.message());
                if (response.isSuccessful()) {
                    System.out.println("3");
                    menuListBreakfast = new ArrayList<>(response.body()); //(ArrayList<MenuModel>)response.body();
                    menuViewModel.setMenuListBreakfast(menuListBreakfast);

                    for (MenuModel menuModel : menuListBreakfast) {
                        System.out.println(menuModel.getDate());
                        System.out.println(menuModel.getMenuList());
                    }


                }
            }

            @Override
            public void onFailure(Call<List<MenuModel>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void loadDataDinner(){

        MenuAPI menuAPI = retrofit.create(MenuAPI.class);
        MenuViewModel menuViewModel = new ViewModelProvider(this).get(MenuViewModel.class);

        Call<List<MenuModel>> call = menuAPI.getDataDinner();

        call.enqueue(new Callback<List<MenuModel>>() {
            @Override
            public void onResponse(Call<List<MenuModel>> call, Response<List<MenuModel>> response) {

                System.out.println(response.message());
                if (response.isSuccessful()) {

                    menuListDinner = new ArrayList<>(response.body()); //(ArrayList<MenuModel>)response.body();
                    menuViewModel.setMenuListDinner(menuListDinner);

                    for (MenuModel menuModel : menuListDinner) {
                        System.out.println(menuModel.getDate());
                        System.out.println(menuModel.getMenuList());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<MenuModel>> call, Throwable t) {

                System.out.println(t.getMessage());
                System.out.println(t.getLocalizedMessage());
                t.printStackTrace();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.profile) {
            Toast.makeText(this, item.getTitle()+" Clicked!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, Profile.class);
            startActivity(intent);
        }
        else if (id == R.id.logout) {

            showAlertDialog();

        }
        return super.onOptionsItemSelected(item);
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, fragment);
        fragmentTransaction.commit();
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Uygulama Sonlandırılıyor");
        builder.setMessage("Çıkış yapmak istiyor musunuz?");
        builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Evet butonuna tıklandığında yapılacak işlemler
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(Home.this, Login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Tüm loginden sonra açılan tüm aktiviteler kapanır
                startActivity(intent);
                finish();

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