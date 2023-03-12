package com.github.sample.core.models

import com.google.gson.annotations.SerializedName

data class GithubUser(
    @SerializedName("login") val login: String,
    @SerializedName("avatar_url") val avatarUrl: String?,
    @SerializedName("followers") val followers: Int?,
    @SerializedName("following") val following: Int?
)