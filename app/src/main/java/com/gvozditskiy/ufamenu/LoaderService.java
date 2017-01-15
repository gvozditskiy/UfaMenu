package com.gvozditskiy.ufamenu;

import android.app.IntentService;
import android.content.Intent;
import android.os.Process;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.gvozditskiy.ufamenu.Interfaces.UfaApi;
import com.gvozditskiy.ufamenu.Parser.Category;
import com.gvozditskiy.ufamenu.Parser.Offer;
import com.gvozditskiy.ufamenu.Parser.Param;
import com.gvozditskiy.ufamenu.Parser.YmlResponse;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

import static com.gvozditskiy.ufamenu.Constants.BASE_URL;
import static com.gvozditskiy.ufamenu.Constants.KEY;

/**
 * Created by Alexey on 13.01.2017.
 */

public class LoaderService extends IntentService {
    private OkHttpClient getHttpClient;

    private UfaApi ufaApi;
    private Retrofit retrofit;

    public LoaderService() {
        super("LoaderService");

    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        getHttpClient = new OkHttpClient.Builder().connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES)
                .build();
        Log.d("service",Thread.currentThread().getName());

        getDataFromRest();
    }


    private void getDataFromRest() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(getHttpClient)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();
        ufaApi = retrofit.create(UfaApi.class);


        ufaApi.getData(KEY).enqueue(new Callback<YmlResponse>() {
            @Override
            public void onResponse(Call<YmlResponse> call, Response<YmlResponse> response) {
                try {
                    TableUtils.clearTable(HelperFactory.getHelper().getConnectionSource(), Category.class);
                    TableUtils.clearTable(HelperFactory.getHelper().getConnectionSource(), Offer.class);
                    TableUtils.clearTable(HelperFactory.getHelper().getConnectionSource(), Param.class);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                for (Category category: response.body().getShop().getCategories()) {
                    try {
                        HelperFactory.getHelper().getDaoCAtegory().create(category);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                // TODO: 15.01.2017 Разобраться в затупе (потеря фреймов на записи offers и params в таблицу) 
                for (Offer offer: response.body().getShop().getOffers()) {
                    try {
                        HelperFactory.getHelper().getDaoOffer().create(offer);
                        if (offer.getParams()!=null) {
                            for (Param param: offer.getParams()) {
                                param.setOfferId(offer.getId());
                                HelperFactory.getHelper().getDaoParam().create(param);
                            }
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                LocalBroadcastManager.getInstance(getApplicationContext())
                        .sendBroadcast(createLocalIntent(Constants.STATUS_OK));
            }

            @Override
            public void onFailure(Call<YmlResponse> call, Throwable t) {
                LocalBroadcastManager.getInstance(getApplicationContext())
                        .sendBroadcast(createLocalIntent(Constants.STATUS_ERROR));
            }
        });
    }



    private Intent createLocalIntent(String status) {
        Intent localIntent = new Intent(Constants.BROADCAST_ACTION);
        localIntent.putExtra(Constants.EXTRA_STATUS, status);
        return localIntent;
    }

}
