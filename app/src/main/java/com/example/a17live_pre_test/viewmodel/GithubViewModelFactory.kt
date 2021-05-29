package com.example.a17live_pre_test.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.a17live_pre_test.model.DataModel

class GithubViewModelFactory : ViewModelProvider.Factory {

    private var mDataModel: DataModel? = null

    constructor() {
        this.mDataModel = DataModel()
    }

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            return UserViewModel(this.mDataModel!!) as T
        }
        throw IllegalArgumentException("Unknow ViewModel Class")
    }
}