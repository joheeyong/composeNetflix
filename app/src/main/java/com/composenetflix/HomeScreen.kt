package com.composenetflix

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun HomeScreen(
    viewModel: HomeViewModel
) {
    val state by viewModel.uiState.collectAsState()

    when (val s = state) {
        is HomeUiState.Loading -> LoadingView()
        is HomeUiState.Error -> ErrorView(message = s.message, onRetry = viewModel::refresh)
        is HomeUiState.Success -> MovieGrid(movies = s.movies, onRefresh = viewModel::refresh)
    }
}

@Composable
private fun LoadingView() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
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
            OutlinedButton(onClick = onRetry) {
                Text("다시 시도")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MovieGrid(
    movies: List<MovieUi>,
    onRefresh: () -> Unit
) {
    Scaffold(
        topBar = {
            SmallTopAppBar(title = { Text("Composenetflix") })
        }
    ) { padding ->
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 140.dp),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(padding)
        ) {
            items(movies, key = { it.id }) { movie ->
                MovieCard(movie = movie)
            }
        }
    }
}

@Composable
private fun MovieCard(movie: MovieUi) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column {
            AsyncImage(
                model = movie.posterUrl,
                contentDescription = movie.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(2f / 3f) // 포스터 비율
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = movie.title,
                style = MaterialTheme.typography.titleSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = movie.overview,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
            Spacer(Modifier.height(8.dp))
        }
    }
}
