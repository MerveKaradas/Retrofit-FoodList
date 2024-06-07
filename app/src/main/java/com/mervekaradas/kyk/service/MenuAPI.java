package com.mervekaradas.kyk.service;

import com.mervekaradas.kyk.model.MenuModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface MenuAPI {

    @GET("MerveKaradas/KYK-DataSet/main/sabahMenu.json")
    Call<List<MenuModel>> getDataBreakfast();

    @GET("MerveKaradas/KYK-DataSet/main/aksamMenu.json")
    Call<List<MenuModel>> getDataDinner();
}
