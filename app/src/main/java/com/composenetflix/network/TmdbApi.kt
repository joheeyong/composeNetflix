package com.composenetflix.network

import com.composenetflix.network.dto.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface TmdbApi {

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("page") page: Int = 1,
        @Query("language") language: String = "en-US"
    ): MovieResponse
}
