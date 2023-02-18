package com.ho2ri2s.androidtvplayground

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.leanback.widget.Presenter
import com.ho2ri2s.androidtvplayground.databinding.LayoutArticleCardBinding

class ArticleCardPresenter : Presenter() {

  override fun onCreateViewHolder(parent: ViewGroup): Presenter.ViewHolder {
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

  override fun onBindViewHolder(viewHolder: Presenter.ViewHolder, item: Any?) {
    val binding = (viewHolder as ViewHolder).binding
    val title = item as String
    viewHolder.title = title
    binding.title.text = title
  }

  override fun onUnbindViewHolder(viewHolder: Presenter.ViewHolder) = Unit

  class ViewHolder(
    val binding: LayoutArticleCardBinding,
  ) : Presenter.ViewHolder(binding.root) {
    var title: String? = null
  }
}