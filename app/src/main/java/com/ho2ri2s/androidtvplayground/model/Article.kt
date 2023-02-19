package com.ho2ri2s.androidtvplayground.model

import kotlinx.serialization.Serializable

@Serializable
data class Article(
  val title: String,
  val user: User,
)