package com.garrettv11.androidsample.managers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.garrettv11.androidsample.BuildConfig;
import com.garrettv11.androidsample.RandomNumberApplication;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by garrett.kim on 9/15/17.
 */

public class NetworkManager {
    private static Retrofit client;

    //this retrofit client comes with an interceptor that always attaches the auth token to various
    // requests by asking the AccountManager what the current auth token is
    public static Retrofit getClient() {
        if (client == null) {
            OkHttpClient defaultHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Interceptor.Chain chain) throws IOException {
                            //getAccessToken is your own accessToken(retrieve it by saving in shared preference or any other option )
                            if (AccountManager.getAuthToken().isEmpty()) {
                                return chain.proceed(chain.request());
                            }
                            Request authorisedRequest = chain.request().newBuilder()
                                    .addHeader("Authorization", AccountManager.getAuthToken()).build();
                            return chain.proceed(authorisedRequest);
                        }
                    }).build();
            client = new Retrofit.Builder()
                    .baseUrl(BuildConfig.API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(defaultHttpClient)
                    .build();
        }
        return client;
    }

    public static boolean isOnline() {
        RandomNumberApplication application = RandomNumberApplication.sharedApplication();
        ConnectivityManager cm =
                (ConnectivityManager) application.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}