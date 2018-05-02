package com.example.vovak.weathertest.retrofit;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by vovak on 26.04.2018.
 */

public class ServiceRetrofit extends Application{
    public static final String API = "28245a95b52aaf54b5b542e4cda8c032";
    private static String BASE_URL = "https://api.openweathermap.org/data/2.5/";
    private static InterfaceRequests interfaceRequests;
    private Retrofit retrofit;

    @Override
    public void onCreate() {
        super.onCreate();

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);


        initRealm();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
        interfaceRequests = retrofit.create(InterfaceRequests.class);
    }

    public static InterfaceRequests getApi(){
        return interfaceRequests;
    }

    private void initRealm(){
        Realm.init(this);
    }
}
