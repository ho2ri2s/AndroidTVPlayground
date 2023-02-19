package com.ho2ri2s.androidtvplayground.data

import com.ho2ri2s.androidtvplayground.model.Article

interface QiitaApi {
  suspend fun getArticles(user: String): List<Article>
}