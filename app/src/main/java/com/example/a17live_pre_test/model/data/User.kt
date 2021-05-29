package com.example.a17live_pre_test.model.data

import com.google.gson.annotations.SerializedName

class User {
    @SerializedName("id")
    private val id: Int = 0

    @SerializedName("login")
    private var login: String = ""

    @SerializedName("avatar_url")
    private var avatar_url: String = ""

    fun getLogin(): String {
        return this.login
    }

    fun getId(): Int {
        return this.id
    }

    fun getAvatarUrl(): String {
        return this.avatar_url
    }
}