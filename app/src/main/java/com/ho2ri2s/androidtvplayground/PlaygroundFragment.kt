package com.ho2ri2s.androidtvplayground

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.leanback.app.RowsSupportFragment
import androidx.leanback.paging.PagingDataAdapter
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.FocusHighlight
import androidx.leanback.widget.ListRow
import androidx.leanback.widget.ListRowPresenter
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import com.ho2ri2s.androidtvplayground.data.QiitaApi
import com.ho2ri2s.androidtvplayground.model.Article
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class PlaygroundFragment : RowsSupportFragment() {

  @Inject lateinit var api: QiitaApi
  private val viewModel: PlaygroundViewModel by viewModels()

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val rowsAdapter = ArrayObjectAdapter(ListRowPresenter(FocusHighlight.ZOOM_FACTOR_MEDIUM, true))
    adapter = rowsAdapter

    val articleCardAdapter = PagingDataAdapter(
      presenter = ArticleCardPresenter(),
      diffCallback = ArticleDiffCallback()
    )
    val listRow = ListRow(articleCardAdapter)
    rowsAdapter.add(listRow)

    lifecycleScope.launch {
      viewModel.articlesPageDataStream.collectLatest { data ->
        articleCardAdapter.submitData(data)
      }
    }
  }

  private class ArticleDiffCallback : DiffUtil.ItemCallback<Article>() {
    override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
      return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
      return oldItem == newItem
    }
  }
}