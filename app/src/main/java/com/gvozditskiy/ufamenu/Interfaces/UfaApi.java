package com.gvozditskiy.ufamenu.Interfaces;

import com.gvozditskiy.ufamenu.Parser.YmlResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Alexey on 13.01.2017.
 */

public interface UfaApi {
    @GET("/getyml")
    Call<YmlResponse> getData (@Query("key") String key);
}
