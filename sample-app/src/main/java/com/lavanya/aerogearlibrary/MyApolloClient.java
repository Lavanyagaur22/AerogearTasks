package com.lavanya.aerogearlibrary;

import com.apollographql.apollo.ApolloClient;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class MyApolloClient {

    private static final String BASE_URL = "http://192.168.0.105:4000/graphql";

    private static ApolloClient myApolloClient;

    public static ApolloClient getMyApolloClient() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient=new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();

        myApolloClient=ApolloClient.builder()
                .serverUrl(BASE_URL)
                .okHttpClient(okHttpClient)
                .build();

                return myApolloClient;

    }

}
