package com.example.a17live_pre_test.service

import com.example.a17live_pre_test.model.data.ReqUserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubService {
    @GET("search/users")
    fun searchUsers(@Query("q") query: String, @Query("page") page: Int, @Query("per_page") per_page: Int): Call<ReqUserResponse>
}