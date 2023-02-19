package com.ho2ri2s.androidtvplayground.model

data class PagedArticles(
  val articles: List<Article>,
  val nextPage: Int?,
)