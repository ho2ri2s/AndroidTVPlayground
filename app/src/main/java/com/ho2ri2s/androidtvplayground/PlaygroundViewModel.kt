package com.ho2ri2s.androidtvplayground

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.ho2ri2s.androidtvplayground.data.QiitaApi
import com.ho2ri2s.androidtvplayground.model.UserId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PlaygroundViewModel @Inject constructor(
  private val qiitaApi: QiitaApi,
) : ViewModel() {
  val articlesPageDataStream = Pager(PagingConfig(pageSize = 10)) {
    QiitaPagingSource(qiitaApi = qiitaApi, userId = UserId("miriwo"))
  }.flow.catch {
    Timber.d("catch = $this")
  }.cachedIn(viewModelScope)
}
