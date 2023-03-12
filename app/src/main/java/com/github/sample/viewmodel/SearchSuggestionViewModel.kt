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

    private val _qrHistoryFlow = MutableStateFlow<List<GithubUser>>(emptyList())
    val qrHistoryFlow: StateFlow<List<GithubUser>> = _qrHistoryFlow
    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query
    private val _userRepoListFlow = MutableStateFlow<List<UserRepositoryItem>>(emptyList())
    val userRepoListFlow: StateFlow<List<UserRepositoryItem>> = _userRepoListFlow
    private val _userInfoFlow = MutableStateFlow<GithubUser?>(null)
    val userInfoFlow: StateFlow<GithubUser?> = _userInfoFlow
//
//
//    private suspend fun searchRepository(keyword: String?): List<GithubUser> {
//        if (keyword == null) return emptyList()
//        val listResult = searchRepository.getSearchSuggestion(keyword)
//        return if (listResult.isSuccessful) {
//            listResult.body()?.items ?: emptyList()
//        } else emptyList()
//    }
//
//    fun newSearch(query: String) {
//        viewModelScope.launch(Dispatchers.IO) {
//            _qrHistoryFlow.value = searchRepository(keyword = query)
//        }
//    }
//
//    fun onQueryChanged(query: String) {
//        _query.value = query
//        newSearch(query)
//    }
//
//    fun getUserRepositoryList(userName: String) {
//        viewModelScope.launch(Dispatchers.IO) {
//            val response = searchRepository.getUserRepositories(userName = userName)
//            if (response.isSuccessful) {
//                _userRepoListFlow.value = response.body()!!
//            } else {
//                _userRepoListFlow.value = emptyList()
//            }
//        }
//    }
//
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