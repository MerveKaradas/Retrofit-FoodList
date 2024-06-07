package com.mervekaradas.kyk.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mervekaradas.kyk.model.MenuModel;

import java.util.List;

public class MenuViewModel extends ViewModel {

    //LiveData: Değeri dışarıdan değiştirilemez, sadece gözlemlenir.
    //MutableLiveData: Değeri değiştirilebilir ve gözlemlenebilir.
    private MutableLiveData<List<MenuModel>> menuListBreakfast;
    private MutableLiveData<List<MenuModel>> menuListDinner;

    private static final String TAG = "MenuViewModel"; //LOG İÇİN EKLEDİM
    public LiveData<List<MenuModel>> getMenuListBreakfast() {
        // Eğer menuListBreakfast henüz oluşturulmadıysa, yeni bir MutableLiveData örneği oluşturulur.
        if (menuListBreakfast == null) {
            menuListBreakfast = new MutableLiveData<>();
            Log.d(TAG, "menuListBreakfast MutableLiveData instance created.");
        }
        else {
            Log.d(TAG, "menuListBreakfast MutableLiveData instance already exists.");
        }
        return menuListBreakfast;
    }

    // setMenuListBreakfast metodu, ViewModel'deki veriyi güncellemek için kullanılır
    public void setMenuListBreakfast(List<MenuModel> menuList) {

        if (menuListBreakfast == null) {
            menuListBreakfast = new MutableLiveData<>();
            Log.d(TAG, "menuListBreakfast MutableLiveData instance created in setMenuListBreakfast.");
        }
        // menuListBreakfast güncellenir ve yeni değer atanır
        menuListBreakfast.setValue(menuList);
        Log.d(TAG, "menuListBreakfast updated with new data: " + menuList.toString());
    }

    public LiveData<List<MenuModel>> getMenuListDinenr() {
        // Eğer menuListDinner henüz oluşturulmadıysa, yeni bir MutableLiveData örneği oluşturulur.
        if (menuListDinner == null) {
            menuListDinner = new MutableLiveData<>();
            Log.d(TAG, "menuListDinner MutableLiveData instance created.");
        }
        else {
            Log.d(TAG, "menuListDinner MutableLiveData instance already exists.");
        }
        return menuListDinner;
    }

    public void setMenuListDinner(List<MenuModel> menuList) {

        if (menuListDinner == null) {
            menuListDinner = new MutableLiveData<>();
            Log.d(TAG, "menuListDinner MutableLiveData instance created in setMenuListDinner.");
        }
        // menuListDinner güncellenir ve yeni değer atanır
        menuListDinner.setValue(menuList);
        Log.d(TAG, "menuListDinner updated with new data: " + menuList.toString());
    }

}
