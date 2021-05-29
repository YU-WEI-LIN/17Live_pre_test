package com.example.a17live_pre_test.model.data

import com.google.gson.annotations.SerializedName

class ReqUserResponse {

    @SerializedName("total_count")
    private var total: Int = 0

    @SerializedName("items")
    private var items: List<User>? = null

    fun getTotal(): Int {
        return this.total
    }

    fun setTotal(total: Int) {
        this.total = total
    }

    fun getItems(): List<User> {
        return this.items!!
    }

    fun setItems(items: List<User>) {
        this.items = items
    }
}