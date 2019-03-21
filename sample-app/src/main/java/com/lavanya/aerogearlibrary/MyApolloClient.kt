package com.lavanya.aerogearlibrary

import com.apollographql.apollo.ApolloClient
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

class MyApolloClient {

    companion object {

        //BASE_URL is of the format http://your_ip_adress:4000/graphql
        private val BASE_URL = "http://192.168.0.105:4000/graphql"

        private var myApolloClient: ApolloClient? = null

        fun getMyApolloClient(): ApolloClient? {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()

            myApolloClient = ApolloClient.builder()
                .serverUrl(BASE_URL)
                .okHttpClient(okHttpClient)
                .build()

            return myApolloClient
        }
    }
}