package com.lavanya.aerogearlibrary

import android.app.Application
import android.util.Log
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Field
import com.apollographql.apollo.api.Operation
import com.apollographql.apollo.cache.normalized.CacheKey
import com.apollographql.apollo.cache.normalized.CacheKeyResolver
import com.apollographql.apollo.cache.normalized.lru.EvictionPolicy
import com.apollographql.apollo.cache.normalized.lru.LruNormalizedCacheFactory
import com.apollographql.apollo.cache.normalized.sql.ApolloSqlHelper
import com.apollographql.apollo.cache.normalized.sql.SqlNormalizedCacheFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

class MyApolloClient : Application() {

    //Implemented normalised disk cache

    companion object {

        //BASE_URL is of the format http://your_ip_adress:4000/graphql
        private val BASE_URL = "http://192.168.0.106:4000/graphql"
        private var myApolloClient: ApolloClient? = null
        private val SQL_CACHE_NAME = "tasksdb"

        fun getMyApolloClient(): ApolloClient? {
            return myApolloClient
        }
    }

    override fun onCreate() {
        super.onCreate()
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        val apolloSqlHelper = ApolloSqlHelper(this, SQL_CACHE_NAME)

        val cacheFactory = LruNormalizedCacheFactory(
            EvictionPolicy.NO_EVICTION, SqlNormalizedCacheFactory(apolloSqlHelper)
        )

        val resolver = object : CacheKeyResolver() {

            override fun fromFieldRecordSet(field: Field, recordSet: MutableMap<String, Any>): CacheKey {

                Log.e("Application", "fromFieldRecordSet ${(recordSet["id"] as String)}")
                if (recordSet.containsKey("id")) {
                    val typeNameAndIDKey = recordSet["__typename"].toString() + "." + recordSet["id"]
                    return CacheKey.from(typeNameAndIDKey)
                }
                return CacheKey.NO_KEY
            }

            // Use this resolver to customize the key for fields with variables: eg entry(repoFullName: $repoFullName).
            // This is useful if you want to make query to be able to resolved, even if it has never been run before.
            override fun fromFieldArguments(field: Field, variables: Operation.Variables): CacheKey {

                Log.e("Application", "fromFieldArguments $variables")

                return CacheKey.NO_KEY
            }

        }

        //Build the apollo client
        myApolloClient = ApolloClient.builder()
            .serverUrl(BASE_URL)
            .normalizedCache(cacheFactory, resolver)
            .okHttpClient(okHttpClient)
            .build()

    }
}