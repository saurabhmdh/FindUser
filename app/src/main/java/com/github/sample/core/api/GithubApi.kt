package com.github.sample.core.api

import com.github.sample.core.models.GithubUser
import com.github.sample.core.models.GithubUserResponse
import com.github.sample.core.models.UserRepositoryItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubApi {
    @GET("search/users")
    suspend fun getUsersList(
        @Query("q") keyword: String,
    ): Response<GithubUserResponse>

    @GET("users/{username}")
    suspend fun getUsersInfo(
        @Path("username") username: String,
    ): Response<GithubUser>

    @GET("users/{username}/repos")
    suspend fun getUserRepos(
        @Path("username") username: String,
    ): Response<List<UserRepositoryItem>>
}