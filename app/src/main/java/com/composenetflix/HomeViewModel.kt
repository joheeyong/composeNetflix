package com.composenetflix

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.composenetflix.data.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private const val TMDB_IMAGE_BASE = "https://image.tmdb.org/t/p/w500"

data class MovieUi(
    val id: Int,
    val title: String,
    val overview: String,
    val posterUrl: String?
)

sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Success(val movies: List<MovieUi>) : HomeUiState
    data class Error(val message: String) : HomeUiState
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repo: MovieRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        _uiState.value = HomeUiState.Loading
        viewModelScope.launch {
            runCatching {
                repo.fetchPopularMovies(page = 1).map { dto ->
                    MovieUi(
                        id = dto.id,
                        title = dto.title,
                        overview = dto.overview,
                        posterUrl = dto.posterPath?.let { TMDB_IMAGE_BASE + it }
                    )
                }
            }.onSuccess { list ->
                _uiState.value = HomeUiState.Success(list)
            }.onFailure { e ->
                _uiState.value = HomeUiState.Error(e.message ?: "알 수 없는 오류")
            }
        }
    }
}
