package com.ho2ri2s.androidtvplayground

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.leanback.app.RowsSupportFragment
import androidx.leanback.paging.PagingDataAdapter
import androidx.leanback.widget.ClassPresenterSelector
import androidx.leanback.widget.FocusHighlight
import androidx.leanback.widget.HeaderItem
import androidx.leanback.widget.ListRow
import androidx.leanback.widget.ListRowPresenter
import androidx.leanback.widget.Row
import androidx.leanback.widget.SparseArrayObjectAdapter
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.recyclerview.widget.DiffUtil
import com.ho2ri2s.androidtvplayground.data.QiitaApi
import com.ho2ri2s.androidtvplayground.model.Article
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PlaygroundFragment : RowsSupportFragment() {

  @Inject lateinit var api: QiitaApi
  private val viewModel: PlaygroundViewModel by viewModels()
  private lateinit var rowsAdapter: SparseArrayObjectAdapter
  private lateinit var articleListRowPresenter: ListRowPresenter
  private lateinit var qiitaHeaderPresenter: QiitaHeaderRowPresenter

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    articleListRowPresenter = ListRowPresenter(FocusHighlight.ZOOM_FACTOR_MEDIUM, true)
    qiitaHeaderPresenter = QiitaHeaderRowPresenter()
    val classPresenterSelector = ClassPresenterSelector().apply {
      addClassPresenter(Row::class.java, qiitaHeaderPresenter)
      addClassPresenter(ListRow::class.java, articleListRowPresenter)
    }
    rowsAdapter = SparseArrayObjectAdapter(classPresenterSelector)
    adapter = rowsAdapter

    setHeader("takahirom")

    lifecycleScope.launch {
      viewModel.articlesPageDataStream.collectLatest { data ->
        setArticles(data)
      }
    }
  }

  private fun setHeader(userName: String) {
    val row = Row(HeaderItem(userName))
    rowsAdapter.set(ROW_TYPE_HEADER, row)
  }

  private suspend fun setArticles(articles: PagingData<Article>) {
    val articleCardAdapter = PagingDataAdapter(
      presenter = ArticleCardPresenter(),
      diffCallback = ArticleDiffCallback()
    )
    val listRow = ListRow(articleCardAdapter)
    rowsAdapter.set(ROW_TYPE_ARTICLE_LIST, listRow)

    articleCardAdapter.submitData(articles)
  }

  private class ArticleDiffCallback : DiffUtil.ItemCallback<Article>() {
    override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
      return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
      return oldItem == newItem
    }
  }

  companion object {
    private const val ROW_TYPE_HEADER = 0
    private const val ROW_TYPE_ARTICLE_LIST = 1
  }
}