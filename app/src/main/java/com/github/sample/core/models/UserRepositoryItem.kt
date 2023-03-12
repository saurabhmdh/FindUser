package com.github.sample.core.models

import com.google.gson.annotations.SerializedName

data class UserRepositoryItem(
    @SerializedName("description")
    var description: String?,

    @SerializedName("language")
    var language: String?,

    @SerializedName("name")
    var name: String?,

    @SerializedName("html_url")
    var url: String?,

    @SerializedName("forks_count")
    var startCount: Int?,
)