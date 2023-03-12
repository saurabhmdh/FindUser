package com.github.sample.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.sample.core.models.UserRepositoryItem
import com.github.sample.core.models.GithubUser
import com.github.sample.repository.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchSuggestionViewModel
@Inject internal constructor(
    private val searchRepository: SearchRepository,
) : ViewModel() {

    private val _qrHistoryFlow = MutableStateFlow<List<GithubUser>>(emptyList())
    val qrHistoryFlow: StateFlow<List<GithubUser>> = _qrHistoryFlow
    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query
    private val _userRepoListFlow = MutableStateFlow<List<UserRepositoryItem>>(emptyList())
    val userRepoListFlow: StateFlow<List<UserRepositoryItem>> = _userRepoListFlow
    private val _userInfoFlow = MutableStateFlow<GithubUser?>(null)
    val userInfoFlow: StateFlow<GithubUser?> = _userInfoFlow


    private suspend fun searchRepository(keyword: String?): List<GithubUser> {
        if (keyword == null) return emptyList()
        val listResult = searchRepository.getSearchSuggestion(keyword)
        return if (listResult.isSuccessful) {
            listResult.body()?.items ?: emptyList()
        } else emptyList()
    }

    fun newSearch(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _qrHistoryFlow.value = searchRepository(keyword = query)
        }
    }

    fun onQueryChanged(query: String) {
        _query.value = query
        newSearch(query)
    }

    fun getUserRepositoryList(userName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = searchRepository.getUserRepositories(userName = userName)
            if (response.isSuccessful) {
                _userRepoListFlow.value = response.body()!!
            } else {
                _userRepoListFlow.value = emptyList()
            }
        }
    }

    fun getUserInfo(userName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = searchRepository.getUserInfo(userName = userName)
            if (response.isSuccessful) {
                _userInfoFlow.value = response.body()!!
            } else {
                _userInfoFlow.value = null
            }
        }
    }
}