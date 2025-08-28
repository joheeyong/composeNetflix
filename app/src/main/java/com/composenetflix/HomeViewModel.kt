package com.composenetflix

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.composenetflix.data.MovieRepository
import com.composenetflix.network.dto.MovieDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private const val TMDB_IMAGE_BASE = "https://image.tmdb.org/t/p/w500"

data class MovieUi(
    val id: Int,
    val title: String,
    val overview: String,
    val posterUrl: String?
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    repo: MovieRepository
) : ViewModel() {

    val movies: Flow<PagingData<MovieUi>> =
        repo.getPopularMoviesPaged()
            .map { pagingData ->
                pagingData.map { dto: MovieDto ->
                    MovieUi(
                        id = dto.id,
                        title = dto.title,
                        overview = dto.overview,
                        posterUrl = dto.posterPath?.let { TMDB_IMAGE_BASE + it }
                    )
                }
            }
            .cachedIn(viewModelScope)
}
