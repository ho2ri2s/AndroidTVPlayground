package com.ho2ri2s.androidtvplayground.data

import android.accounts.NetworkErrorException
import com.ho2ri2s.androidtvplayground.model.Article
import com.ho2ri2s.androidtvplayground.model.PagedArticles
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query
import timber.log.Timber
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
    ): Response<List<Article>>
  }

  override suspend fun getArticles(userId: String, page: Int, limit: Int): PagedArticles {
    val query = "qiita user:$userId"
    val response = service.getUserArticles(
      query = query,
      page = page,
      perPage = limit,
    )
    if (!response.isSuccessful) throw HttpException(response)
    val nextPageLinks = response.headers()[RESPONSE_HEADER_LINK]
    val splitLinks = nextPageLinks?.split(",")
    val pageMap = splitLinks?.mapNotNull { link ->
      val matchResult = """page=([0-9]+).*rel="([a-z]+)"""".toRegex().find(link)
        ?: return@mapNotNull null

      val pageValue = matchResult.groupValues[1].toInt()
      val relValue = matchResult.groupValues[2]
      relValue to pageValue
    }?.toMap()

    val articles = response.body() ?: throw NetworkErrorException("Response body is null.")

    return PagedArticles(
      articles = articles,
      nextPage = pageMap?.get(RESPONSE_REL_NEXT),
    )
  }

  companion object {
    private const val RESPONSE_HEADER_LINK = "Link"
    private const val RESPONSE_REL_NEXT = "next"
  }
}