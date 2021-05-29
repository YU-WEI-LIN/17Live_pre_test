package com.example.a17live_pre_test.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.a17live_pre_test.model.DataModel
import com.example.a17live_pre_test.model.data.ReqUserResponse
import com.example.a17live_pre_test.model.data.User

class UserViewModel : ViewModel, DataModel.OnDataReadyCallback {

    private val mUsers: MutableLiveData<List<User>> = MutableLiveData()
    private var mIsLoading: MutableLiveData<Boolean> = MutableLiveData()
    private var mDataModel: DataModel = DataModel()
    private var mTotal = 0

    constructor(dataModel: DataModel) : super() {
        this.mDataModel = dataModel
        this.mIsLoading.value = false
    }

    fun getUsers(): MutableLiveData<List<User>> {
        return mUsers
    }

    fun getUserTotalCount(): Int {
        return mTotal
    }

    fun isLoadingData(): MutableLiveData<Boolean> {
        return mIsLoading
    }

    fun searchUsers(query: String, page: Int, pageSize: Int) {
        this.mIsLoading.value = true
        mDataModel!!.searchUsers(query, page, pageSize, this)
    }

    //DataModel OnDataReady interface impl.
    override fun onDataReady(data: List<User>, total: Int) {
        mUsers.value = data
        mTotal = total
        this.mIsLoading.value = false
    }
}