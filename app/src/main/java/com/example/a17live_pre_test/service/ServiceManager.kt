package com.example.a17live_pre_test.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ServiceManager private constructor() {

    private var mRetrofit: Retrofit = createRetrofit()

    companion object {
        var instance = ServiceManager()
    }


    private fun createRetrofit(): Retrofit {
        return Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    fun getGithubService(): GithubService {
        return mRetrofit.create(GithubService::class.java)
    }
}