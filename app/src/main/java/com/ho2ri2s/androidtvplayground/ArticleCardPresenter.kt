package com.ho2ri2s.androidtvplayground

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.leanback.widget.RowPresenter
import com.ho2ri2s.androidtvplayground.databinding.LayoutArticleCardBinding

class ArticleCardPresenter : RowPresenter() {

  override fun createRowViewHolder(parent: ViewGroup): RowPresenter.ViewHolder {
    val layoutInflater = LayoutInflater.from(parent.context)
    val binding = LayoutArticleCardBinding.bind(
      layoutInflater.inflate(
        R.layout.layout_article_card,
        parent,
        false
      )
    )

    return ViewHolder(binding)
  }

  override fun onBindRowViewHolder(viewHolder: RowPresenter.ViewHolder, item: Any?) {
    val binding = (viewHolder as ViewHolder).binding
    val title = item as String
    viewHolder.title = title
    binding.title.text = title
  }

  override fun onUnbindRowViewHolder(viewHolder: RowPresenter.ViewHolder) = Unit

  class ViewHolder(
    val binding: LayoutArticleCardBinding,
  ) : RowPresenter.ViewHolder(binding.root) {
    var title: String? = null
  }
}