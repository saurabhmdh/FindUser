package com.github.sample.layout

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.github.sample.viewmodel.SearchSuggestionViewModel
import timber.log.Timber


@Composable
fun SearchSuggestionScreen(viewModel: SearchSuggestionViewModel, onItemClick: (String) -> Unit) {
    Scaffold(content = {
        Column(modifier = Modifier.fillMaxSize()) {
            SearchSuggestionToolbar(viewModel)
            UserList(viewModel = viewModel, onItemClick)
        }
    })
}

@Composable
fun UserList(viewModel: SearchSuggestionViewModel, onItemClick: (String) -> Unit) {
    val userList = viewModel.qrHistoryFlow.collectAsState().value
    val query = viewModel.query.collectAsState().value
    Timber.i("query = $query")
    Timber.i("list = ${userList.size}")
    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(userList) { user ->
            Column(modifier = Modifier.clickable { onItemClick.invoke(user.login) }) {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically) {
                    AsyncImage(
                        model = user.avatarUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .size(40.dp)
                            .padding(end = 8.dp)
                    )
                    Text(text = user.login)
                }
                Divider()
            }
        }
    }
}

@Composable
fun SearchSuggestionToolbar(viewModel: SearchSuggestionViewModel) {
    Row(modifier = Modifier.padding(16.dp)) {
        IconButton(onClick = {}) {
            Icon(painter = painterResource(id = android.R.drawable.ic_notification_clear_all),
                contentDescription = "")
        }
        Box(modifier = Modifier.weight(1f)) {
            SearchBar(query = "alok", viewModel = viewModel)
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun SearchBar(query: String, viewModel: SearchSuggestionViewModel) {
    val keyboardController = LocalSoftwareKeyboardController.current
    Row(modifier = Modifier.fillMaxWidth()) {
        TextField(
            value = query,
            onValueChange = { newValue ->
                viewModel.onQueryChanged(newValue)
            },
            modifier = Modifier
                .fillMaxWidth(fraction = 0.9f)
                .padding(8.dp),
            label = {
                Text("Search")
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search,
                autoCorrect = false,
                keyboardType = KeyboardType.Text,
            ),
            leadingIcon = {
                Icon(Icons.Filled.Search, null)
            },
            keyboardActions = KeyboardActions(onSearch = {
                viewModel.newSearch(query)
                keyboardController?.hide()
            })
        )
    }
}

