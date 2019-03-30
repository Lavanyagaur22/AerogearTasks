package com.lavanya.aerogearlibrary

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Field
import com.apollographql.apollo.api.Operation
import com.apollographql.apollo.cache.normalized.CacheKey
import com.apollographql.apollo.cache.normalized.CacheKeyResolver
import com.apollographql.apollo.cache.normalized.lru.EvictionPolicy
import com.apollographql.apollo.cache.normalized.lru.LruNormalizedCacheFactory
import com.apollographql.apollo.cache.normalized.sql.ApolloSqlHelper
import com.apollographql.apollo.cache.normalized.sql.SqlNormalizedCacheFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.nio.charset.Charset

object Utils {

    //To run on emulator
    private const val BASE_URL = "http://10.0.2.2:4000/graphql"
    private const val SQL_CACHE_NAME = "tasksDb"

    private var apClient: ApolloClient? = null
    private var httpClient: OkHttpClient? = null
    private var conflictInterceptor: Interceptor? = null

    fun getApolloClient(context: Context): ApolloClient? {

        val apolloSqlHelper = ApolloSqlHelper(context, SQL_CACHE_NAME)

        val cacheFactory = LruNormalizedCacheFactory(
            EvictionPolicy.NO_EVICTION, SqlNormalizedCacheFactory(apolloSqlHelper)
        )

        apClient?.let {
            return it
        } ?: kotlin.run {
            apClient = ApolloClient.builder()
                .okHttpClient(getOkhttpClient(context)!!)
                .normalizedCache(cacheFactory, cacheResolver())
                .serverUrl(BASE_URL)
                .build()
        }

        return apClient
    }

    private fun getOkhttpClient(context: Context): OkHttpClient? {

        val loggingInterceptor = HttpLoggingInterceptor()

        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        httpClient?.let {
            return it
        } ?: kotlin.run {
            httpClient = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(getConflictInterceptor(context))
                .build()
        }

        return httpClient

    }

    private fun getConflictInterceptor(context: Context): Interceptor? {

        conflictInterceptor?.let {
            return it
        } ?: kotlin.run {
            conflictInterceptor = Interceptor {
                val request = it.request()

                //all the HTTP work happens, producing a response to satisfy the request.
                val response = it.proceed(request)

                //https://stackoverflow.com/a/33862068/10189663
                val responseBody = response.body()
                val bufferedSource = responseBody?.source()
                bufferedSource?.request(Long.MAX_VALUE)
                val buffer = bufferedSource?.buffer()
                val responseBodyString = buffer?.clone()?.readString(Charset.forName("UTF-8")) ?: ""

                if (responseBodyString.contains("VoyagerConflict"))
                    showToast(context)

                return@Interceptor response
            }

        }
        return conflictInterceptor
    }

    private fun showToast(context: Context) {
        (context as AppCompatActivity).runOnUiThread {
            Toast.makeText(context, "Conflict Detected", Toast.LENGTH_SHORT).show()
        }
    }

    private fun cacheResolver(): CacheKeyResolver {

        val resolver = object : CacheKeyResolver() {

            override fun fromFieldRecordSet(field: Field, recordSet: MutableMap<String, Any>): CacheKey {

                Log.e("UtilClass", "fromFieldRecordSet ${(recordSet["id"] as String)}")
                if (recordSet.containsKey("id")) {
                    val typeNameAndIDKey = recordSet["__typename"].toString() + "." + recordSet["id"]
                    return CacheKey.from(typeNameAndIDKey)
                }
                return CacheKey.NO_KEY
            }

            // Use this resolver to customize the key for fields with variables: eg entry(repoFullName: $repoFullName).
            // This is useful if you want to make query to be able to resolved, even if it has never been run before.
            override fun fromFieldArguments(field: Field, variables: Operation.Variables): CacheKey {

                Log.e("UtilClass", "fromFieldArguments $variables")

                return CacheKey.NO_KEY
            }
        }

        return resolver
    }
}
