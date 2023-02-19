package com.ho2ri2s.androidtvplayground.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
  val id: String,
  val name: String,
  @SerialName("profile_image_url") val profileImageUrl: String,
)