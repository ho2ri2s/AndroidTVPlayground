package com.ho2ri2s.androidtvplayground

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ho2ri2s.androidtvplayground.data.QiitaApi
import com.ho2ri2s.androidtvplayground.model.Article
import com.ho2ri2s.androidtvplayground.model.UserId
import javax.inject.Inject

class QiitaPagingSource @Inject constructor(
  private val qiitaApi: QiitaApi,
  private val userId: UserId,
) : PagingSource<Int, Article>() {

  override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
    return try {
      val nextPage = params.key ?: 1
      val response = qiitaApi.getArticles(
        userId = userId.id,
        page = nextPage,
        limit = 10,
      )
      LoadResult.Page(
        data = response.articles,
        prevKey = null,
        nextKey = response.nextPage,
      )
    } catch (e: Throwable) {
      LoadResult.Error(e)
    }
  }

  override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
    return state.anchorPosition?.let { anchorPosition ->
      val anchorPage = state.closestPageToPosition(anchorPosition)
      anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
    }
  }
}