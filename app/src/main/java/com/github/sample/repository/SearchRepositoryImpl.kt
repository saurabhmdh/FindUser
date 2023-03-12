package com.github.sample.repository

import com.github.sample.core.api.GithubApi
import com.github.sample.core.models.GithubUser
import com.github.sample.core.models.GithubUserResponse
import com.github.sample.core.models.UserRepositoryItem
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchRepositoryImpl
@Inject constructor(private val services: GithubApi) : SearchRepository {
    override suspend fun getSearchSuggestion(keyword: String)
            : Response<GithubUserResponse> {
        return services.getUsersList(keyword)
    }

    override suspend fun getUserRepositories(userName: String): Response<List<UserRepositoryItem>> {
        return services.getUserRepos(userName)
    }

    override suspend fun getUserInfo(userName: String): Response<GithubUser> {
        return services.getUsersInfo(userName)
    }

}
