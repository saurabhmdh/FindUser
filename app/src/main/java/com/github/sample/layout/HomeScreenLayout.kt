package com.github.sample.layout

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.github.sample.core.models.GithubUser
import com.github.sample.core.models.UserRepositoryItem
import com.github.sample.viewmodel.SearchSuggestionViewModel
import timber.log.Timber


@Composable
fun SearchSuggestionScreen(viewModel: SearchSuggestionViewModel, onItemClick: (String) -> Unit) {

    Scaffold(content = {
        Column(modifier = Modifier.fillMaxSize()) {
            SearchSuggestionToolbar(viewModel, onItemClick)
        }
    })
}


@Composable
fun SearchSuggestionToolbar(viewModel: SearchSuggestionViewModel, onItemClick: (String) -> Unit) {
    val userSearchModelState by viewModel.searchModelState.collectAsState(initial = SearchSuggestionViewModel.SearchModelState.Empty)

    SearchBarUI(
        searchText = userSearchModelState.searchText,
        placeholderText = "Search",
        onSearchTextChanged = { viewModel.onSearchTextChanged(it) },
        onClearClick = { },
        onNavigateBack = {

        },
        matchesFound = userSearchModelState.suggestions.isEmpty().not(),
        results = {
            SearchSuggestions(
                userSearchModelState.suggestions,
                onItemClick
            )
        }
    ) {
        Timber.i("Need to display something..")
//        ShowSearchHistory(history.value, location.value, onRecentSearchClick, onLocationSelected)
    }
}

@Composable
fun SearchSuggestions(userList: List<GithubUser>,  onItemClick: (String) -> Unit) {
    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(userList) { user ->
            Column(modifier = Modifier.clickable { onItemClick.invoke(user.login) }) {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(2.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically) {
                    AsyncImage(
                        model = user.avatarUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .size(60.dp)
                            .padding(end = 2.dp)
                    )
                    Text(text = user.login)
                }
                Divider()
            }
        }
    }
}

