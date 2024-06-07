package com.mervekaradas.kyk.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.mervekaradas.kyk.R;
import com.mervekaradas.kyk.adapter.RecyclerViewAdapter;
import com.mervekaradas.kyk.databinding.FragmentSabahmenuBinding;
import com.mervekaradas.kyk.model.MenuModel;
import com.mervekaradas.kyk.viewmodel.MenuViewModel;

import java.util.ArrayList;
import java.util.List;

public class SabahMenuFragment extends Fragment {

    FragmentSabahmenuBinding binding;
    private RecyclerViewAdapter recyclerViewAdapter;
    private MenuViewModel menuViewModel;
    private static final String TAG = "SabahMenuFragment";//LOG İÇİN EKLEDİM


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.d(TAG, "Fragment attached to Activity");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "Fragment onCreate called");
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: started");
       // return inflater.inflate(R.layout.fragment_sabahmenu, container, false); ******

        binding = FragmentSabahmenuBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
      //  View view = inflater.inflate(R.layout.fragment_sabahmenu, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "Fragment view created");

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        Log.d(TAG, "RecyclerView layout manager set.");

        menuViewModel = new ViewModelProvider(requireActivity()).get(MenuViewModel.class);
        Log.d(TAG, "ViewModelProvider initialized.");
        menuViewModel.getMenuListBreakfast().observe(getViewLifecycleOwner(), new Observer<List<MenuModel>>() {
            @Override
            public void onChanged(List<MenuModel> menuModels) {
                recyclerViewAdapter = new RecyclerViewAdapter((ArrayList<MenuModel>) menuModels);
                Log.d(TAG, "Observed data changed: " + menuModels);
                binding.recyclerView.setAdapter(recyclerViewAdapter);
                Log.d(TAG, "RecyclerView adapter set with new data.");
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "Fragment onStart called");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "Fragment onResume called");

        View view = getActivity().getCurrentFocus();
        if (view != null) {
            view.requestFocus();
            Log.d(TAG, "Requested focus for the current view");
        } else {
            Log.d(TAG, "No view currently has focus");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "Fragment onPause called");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "Fragment onStop called");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "Fragment onDestroyView called");
        binding = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Fragment onDestroy called");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "Fragment onDetach called");
    }

}
