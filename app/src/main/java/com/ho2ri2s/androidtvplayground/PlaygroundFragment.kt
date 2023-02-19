package com.ho2ri2s.androidtvplayground

import android.os.Bundle
import android.view.View
import androidx.leanback.app.RowsSupportFragment
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.DiffCallback
import androidx.leanback.widget.FocusHighlight
import androidx.leanback.widget.ListRow
import androidx.leanback.widget.ListRowPresenter
import androidx.lifecycle.lifecycleScope
import com.ho2ri2s.androidtvplayground.data.QiitaApi
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PlaygroundFragment : RowsSupportFragment() {

  @Inject lateinit var api: QiitaApi

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val rowsAdapter = ArrayObjectAdapter(ListRowPresenter(FocusHighlight.ZOOM_FACTOR_MEDIUM, true))
    adapter = rowsAdapter

    val articleCardAdapter = ArrayObjectAdapter(ArticleCardPresenter())
    val listRow = ListRow(articleCardAdapter)
    rowsAdapter.add(listRow)

    lifecycleScope.launch {
      val articles = api.getArticles(user = "miriwo")
      articleCardAdapter.setItems(articles, ArticleDiffCallback())
    }
  }

  private class ArticleDiffCallback : DiffCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
      return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
      return oldItem == newItem
    }
  }
}