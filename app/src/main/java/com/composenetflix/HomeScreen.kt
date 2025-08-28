package com.composenetflix

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    val movies = viewModel.movies.collectAsLazyPagingItems()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Composenetflix") }) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val refresh = movies.loadState.refresh) {
                is LoadState.Loading -> LoadingView()
                is LoadState.Error -> ErrorView(
                    message = refresh.error.message ?: "알 수 없는 오류",
                    onRetry = { movies.retry() }
                )
                is LoadState.NotLoading -> {
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 140.dp),
                        contentPadding = PaddingValues(8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(
                            count = movies.itemCount,
                            key = { index -> movies.peek(index)?.id ?: index }
                        ) { index ->
                            val movie = movies[index]
                            if (movie != null) {
                                MovieCard(movie)
                            }
                        }

                        // 하단 추가 로딩/에러 UI
                        when (val append = movies.loadState.append) {
                            is LoadState.Loading -> {
                                item(span = { GridItemSpan(maxLineSpan) }) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        contentAlignment = Alignment.Center
                                    ) { CircularProgressIndicator() }
                                }
                            }
                            is LoadState.Error -> {
                                item(span = { GridItemSpan(maxLineSpan) }) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 8.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text("더 불러오기 실패")
                                        Spacer(Modifier.height(8.dp))
                                        OutlinedButton(onClick = { movies.retry() }) {
                                            Text("다시 시도")
                                        }
                                    }
                                }
                            }
                            else -> Unit
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LoadingView() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) { CircularProgressIndicator() }
}

@Composable
private fun ErrorView(
    message: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "문제가 발생했어요 😢")
            Spacer(Modifier.height(8.dp))
            Text(text = message, style = MaterialTheme.typography.bodySmall)
            Spacer(Modifier.height(12.dp))
            OutlinedButton(onClick = onRetry) { Text("다시 시도") }
        }
    }
}

@Composable
private fun MovieCard(movie: MovieUi?) {
    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
        Column {
            AsyncImage(
                model = movie?.posterUrl,
                contentDescription = movie?.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(2f / 3f) // 포스터 비율
            )
            Spacer(Modifier.height(8.dp))
            movie?.let {
                Text(
                    text = it.title,
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
            Spacer(Modifier.height(6.dp))
            movie?.let {
                Text(
                    text = it.overview,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
            Spacer(Modifier.height(8.dp))
        }
    }
}
