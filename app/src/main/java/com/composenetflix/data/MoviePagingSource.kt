package com.composenetflix.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.composenetflix.network.TmdbApi
import com.composenetflix.network.dto.MovieDto

class MoviePagingSource(
    private val api: TmdbApi
) : PagingSource<Int, MovieDto>() {

    override fun getRefreshKey(state: PagingState<Int, MovieDto>): Int? {
        // 가장 가까운 anchorPosition 기준으로 key 재계산
        return state.anchorPosition?.let { anchor ->
            val page = state.closestPageToPosition(anchor)
            page?.prevKey?.plus(1) ?: page?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieDto> {
        return try {
            val page = params.key ?: 1
            val response = api.getPopularMovies(page = page)

            LoadResult.Page(
                data = response.results,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (response.results.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
