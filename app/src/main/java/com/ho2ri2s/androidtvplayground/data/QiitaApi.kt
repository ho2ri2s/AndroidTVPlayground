package com.ho2ri2s.androidtvplayground.data

import com.ho2ri2s.androidtvplayground.model.PagedArticles

interface QiitaApi {
  suspend fun getArticles(
    userId: String,
    page: Int,
    limit: Int = 10
  ): PagedArticles
}