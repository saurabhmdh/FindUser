package com.github.sample.layout


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.github.sample.R
import com.github.sample.core.models.GithubUser
import com.github.sample.core.models.UserRepositoryItem
import com.github.sample.ui.theme.black33
import com.github.sample.ui.theme.whiteBCG
import com.github.sample.viewmodel.SearchSuggestionViewModel
import timber.log.Timber
import kotlin.math.ceil
import kotlin.math.floor

@Composable
fun UserInfoScreen(
    userName: String,
    modifier: Modifier = Modifier,
    viewModel: SearchSuggestionViewModel,
    onBackClick: () -> Unit,
    onItemClick: (String) -> Unit,
) {
    viewModel.getUserRepositoryList(userName)
    viewModel.getUserInfo(userName)
    Scaffold(content = {
        Column(modifier = Modifier.fillMaxSize()) {
            UserToolBar(title = userName, onNavigationIconClick = onBackClick)
            UserInfoBody(modifier = modifier, viewModel = viewModel, onItemClick = onItemClick)
        }
    })
}

@Composable
private fun UserToolBar(title: String, onNavigationIconClick: () -> Unit) {
    TopAppBar(
        title = { Text(title) },
        backgroundColor = Color.Blue,
        contentColor = Color.White,
        navigationIcon = {
            IconButton(onClick = { onNavigationIconClick() }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    modifier = Modifier,
                    contentDescription = "",
                    tint = Color.White
                )
            }
        }
    )
}

@Composable
private fun UserInfoBody(
    modifier: Modifier = Modifier,
    viewModel: SearchSuggestionViewModel,
    onItemClick: (String) -> Unit,
) {
    val userRepoList = viewModel.userRepoListFlow.collectAsState().value
    val userInfo = viewModel.userInfoFlow.collectAsState().value

    LazyColumn(modifier = modifier.padding(16.dp)) {
        item {
            if (userInfo != null) {
                UserInfoScreen(user = userInfo)
            }
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Repositories", style = TextStyle(black33, 18.sp,
                    FontWeight.Bold), textAlign = TextAlign.Center)
        }
        items(userRepoList) { repo ->
            UserRepositoryItem(repo, onItemClick)
        }
    }
}

@Composable
private fun UserRepositoryItem(repo: UserRepositoryItem, onItemClick: (String) -> Unit) {
    Surface(shape = RoundedCornerShape(5.dp),
        border = BorderStroke(1.dp, whiteBCG),
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 8.dp)) {
        Column(modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable {
                onItemClick.invoke(repo.url.orEmpty())
            }) {
            Text(text = repo.name.orEmpty(), style = TextStyle(black33, 18.sp, FontWeight.Bold))
            repo.startCount?.toDouble()?.let { RatingBar(rating = it) }
            Text(text = repo.language.orEmpty(),
                style = TextStyle(black33, 12.sp, FontWeight.Normal))
            Text(text = repo.description.orEmpty(),
                style = TextStyle(black33, 12.sp, FontWeight.Normal))
        }
    }
}

@Composable
private fun UserInfoScreen(user: GithubUser) {
    Surface(shape = RoundedCornerShape(5.dp),
        border = BorderStroke(1.dp, whiteBCG),
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 8.dp)) {
        Column {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = user.avatarUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(120.dp)
                        .padding(end = 8.dp)
                )
                Column {
                    Text(text = user.login, style = TextStyle(black33, 16.sp, FontWeight.Bold))
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = user.login, style = TextStyle(black33, 12.sp, FontWeight.Normal))
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(modifier = Modifier
                        .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically) {
                        Icon(painter = painterResource(id = R.drawable.ic_follow),
                            contentDescription = "")
                        Text(text = "${user.followers?.or(0)} followers . ${user.following?.or(0)} following",
                            modifier = Modifier.padding(start = 8.dp))
                    }
                }
            }
            Divider()
        }
    }
}


@Composable
fun RatingBar(
    modifier: Modifier = Modifier,
    rating: Double = 0.0,
    stars: Int = 5,
    starsColor: Color = Color.Red,
) {
    val filledStars = floor(rating).toInt()
    val unfilledStars = (stars - ceil(rating)).toInt()
    Row(modifier = modifier) {
        repeat(filledStars) {
            Icon(imageVector = Icons.Outlined.Star, contentDescription = null, tint = starsColor)
        }
        repeat(unfilledStars) {
            Icon(
                imageVector = Icons.Outlined.Star,
                contentDescription = null,
                tint = Color.LightGray
            )
        }
    }
}
