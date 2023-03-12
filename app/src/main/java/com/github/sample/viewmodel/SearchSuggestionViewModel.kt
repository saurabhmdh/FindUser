package com.github.sample.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.sample.core.models.UserRepositoryItem
import com.github.sample.core.models.GithubUser
import com.github.sample.repository.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class SearchSuggestionViewModel
@Inject internal constructor(
    private val searchRepository: SearchRepository,
) : ViewModel(), CoroutineScope {


    private val _userRepoListFlow = MutableStateFlow<List<UserRepositoryItem>>(emptyList())
    val userRepoListFlow: StateFlow<List<UserRepositoryItem>> = _userRepoListFlow
    private val _userInfoFlow = MutableStateFlow<GithubUser?>(null)
    val userInfoFlow: StateFlow<GithubUser?> = _userInfoFlow

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

    private val searchText: MutableStateFlow<String> = MutableStateFlow("")
    private var showProgressBar: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private var suggestions: MutableStateFlow<List<GithubUser>> = MutableStateFlow(emptyList())

    val searchModelState = combine(
        searchText,
        suggestions,
        showProgressBar
    ) { text, matchedUsers, showProgress ->

        SearchModelState(
            text,
            matchedUsers,
            showProgress
        )
    }

    data class SearchModelState(
        val searchText: String = "",
        val suggestions: List<GithubUser> = arrayListOf(),
        val showProgressBar: Boolean = false
    ) {
        companion object {
            val Empty = SearchModelState()
        }
    }

    var job: Job? = null

    private fun onKeywordChange(changedSearchText: String) {
        searchText.value = changedSearchText
        job?.cancel()
        if (changedSearchText.isEmpty()) {
            Timber.i("Saurabh search text is empty")
            suggestions.value = arrayListOf()
        } else {
            job = launch {
                delay(200L)
                val response = searchRepository.getSearchSuggestion(changedSearchText)
                if (response.isSuccessful) {
                    val data = response.body()
                    suggestions.value = data?.items ?: emptyList()
                } else {
                    suggestions.value = emptyList()
                }
            }
        }
    }

    fun onSearchTextChanged(changedSearchText: String) {
        onKeywordChange(changedSearchText)
    }

    fun onClearClick() {
        searchText.value = ""
        suggestions.value = arrayListOf()
    }

    override val coroutineContext: CoroutineContext
        get() =  Dispatchers.IO
}