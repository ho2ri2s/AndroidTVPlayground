package com.ho2ri2s.androidtvplayground.data

import com.ho2ri2s.androidtvplayground.model.Article
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Inject

class QiitaApiClient constructor(
  private val service: Service,
) : QiitaApi {

  @Inject constructor(retrofit: Retrofit) : this(retrofit.create(Service::class.java))

  interface Service {
    @GET("items")
    suspend fun getUserArticles(
      @Query("query") query: String,
      @Query("page") page: Int = 1,
      @Query("per_page") perPage: Int = 10,
    ): List<Article>
  }

  override suspend fun getArticles(user: String): List<Article> {
    val query = "qiita user:$user"
    return service.getUserArticles(query)
  }
}