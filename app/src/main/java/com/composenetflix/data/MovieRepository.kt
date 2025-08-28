package com.composenetflix.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.composenetflix.network.TmdbApi
import com.composenetflix.network.dto.MovieDto
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository @Inject constructor(
    private val api: TmdbApi
) {
    fun getPopularMoviesPaged(): Flow<PagingData<MovieDto>> {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { MoviePagingSource(api) }
        ).flow
    }
}
