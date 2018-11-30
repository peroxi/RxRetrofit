package com.rxretro.model.retrofit

import com.rxretro.model.api.ApiConstants.REST_ENDPOINT
import com.rxretro.model.entity.Contributor
import com.rxretro.model.entity.Repository
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

object RetrofitHelper {
    val loggingInterceptor = HttpLoggingInterceptor()

    val retrofit = Retrofit.Builder()
        .baseUrl(REST_ENDPOINT)
        .client(OkHttpClient.Builder().addInterceptor(
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)).build())
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
    val githubAPI = retrofit.create(GithubAPI::class.java)

    interface GithubAPI {

        @GET("users/{user}/repos")
        fun listRepos(@Path("user") user: String?): Observable<List<Repository?>>

        @GET("repos/{user}/{repo}/contributors")
        fun listRepoContributors(
            @Path("user") user: String?,
            @Path("repo") repo: String?
        ): Observable<List<Contributor>>
    }
}
