package com.github.sample.repository

import com.github.sample.core.models.GithubUser
import com.github.sample.core.models.GithubUserResponse
import com.github.sample.core.models.UserRepositoryItem
import retrofit2.Response

interface SearchRepository {
    suspend fun getSearchSuggestion(keyword: String): Response<GithubUserResponse>

    suspend fun getUserRepositories(userName: String): Response<List<UserRepositoryItem>>

    suspend fun getUserInfo(userName: String): Response<GithubUser>
}
