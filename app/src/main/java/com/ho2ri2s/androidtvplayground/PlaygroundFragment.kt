package com.ho2ri2s.androidtvplayground

import android.os.Bundle
import android.view.View
import androidx.leanback.app.RowsSupportFragment
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.DiffCallback
import androidx.leanback.widget.FocusHighlight
import androidx.leanback.widget.HeaderItem
import androidx.leanback.widget.ListRow
import androidx.leanback.widget.ListRowPresenter

class PlaygroundFragment : RowsSupportFragment() {

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val rowsAdapter = ArrayObjectAdapter(ListRowPresenter(FocusHighlight.ZOOM_FACTOR_MEDIUM, true))
    adapter = rowsAdapter
    val list = listOf(
      "hogehoge",
      "hogge",
      "hohhoohge",
      "hohhoohgee",
      "hogehogeeeee",
      "hoggegg",
      "hohhoohgeggga",
      "hohhoohgaega",
    )
    val listRows = listOf(
      ListRow(
        HeaderItem("ヘッダー"),
        ArrayObjectAdapter(ArticleCardPresenter()).apply { setItems(list, null) }),
      ListRow(
        HeaderItem("ヘッダー"),
        ArrayObjectAdapter(ArticleCardPresenter()).apply { setItems(list, null) })
    )

    rowsAdapter.setItems(listRows, ArticleDiffCallback())
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