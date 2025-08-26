package com.composenetflix.data

import com.composenetflix.network.TmdbApi
import com.composenetflix.network.dto.MovieDto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository @Inject constructor(
    private val api: TmdbApi
) {
    suspend fun fetchPopularMovies(page: Int = 1): List<MovieDto> {
        return api.getPopularMovies(page).results
    }
}
