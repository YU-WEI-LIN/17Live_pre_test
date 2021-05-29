package com.example.a17live_pre_test.model

import android.util.Log
import com.example.a17live_pre_test.model.data.ReqUserResponse
import com.example.a17live_pre_test.model.data.User
import com.example.a17live_pre_test.service.GithubService
import com.example.a17live_pre_test.service.ServiceManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DataModel {

    private var mGithubService: GithubService? = null

    constructor() {
        mGithubService = ServiceManager.instance!!.getGithubService()
    }

    fun searchUsers(query: String, page: Int, pageSize: Int, callback: OnDataReadyCallback) {

        mGithubService!!.searchUsers(query, page, pageSize).enqueue(object : Callback<ReqUserResponse> {
            override fun onFailure(call: Call<ReqUserResponse>?, t: Throwable?) {
                Log.e("DataModel Error", "searchRepositories error")
            }

            override fun onResponse(call: Call<ReqUserResponse>?, response: Response<ReqUserResponse>?) {
                if (response?.code() != 200)
                    callback.onDataReady(mutableListOf<User>(), 0)
                else
                    callback.onDataReady(response?.body()!!.getItems(), response?.body()!!.getTotal())
            }

        })
    }

    interface OnDataReadyCallback {
        fun onDataReady(data: List<User>, total: Int)
    }
}