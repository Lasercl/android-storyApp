package com.example.myapplication.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.myapplication.data.remote.response.ListStoryItem
import com.example.myapplication.data.remote.retrofit.ApiService
import kotlin.collections.isNullOrEmpty

class StoryPagingSource(private val apiService: ApiService) : PagingSource<Int, ListStoryItem>() {
    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val responseData: List<ListStoryItem> = apiService.getStories(position,params.loadSize).listStory as List<ListStoryItem>
            LoadResult.Page(
                data = responseData,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (responseData.isNullOrEmpty()) null else position + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }
}