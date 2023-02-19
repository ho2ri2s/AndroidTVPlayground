package com.ho2ri2s.androidtvplayground

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.leanback.widget.Presenter
import coil.load
import coil.transform.CircleCropTransformation
import com.ho2ri2s.androidtvplayground.databinding.LayoutArticleCardBinding
import com.ho2ri2s.androidtvplayground.model.Article

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
    val article = item as Article
    viewHolder.article = article
    binding.title.text = article.title
    binding.thumbnail
      .load(article.user.profileImageUrl) {
        transformations(CircleCropTransformation())
      }
  }

  override fun onUnbindViewHolder(viewHolder: Presenter.ViewHolder) = Unit

  class ViewHolder(
    val binding: LayoutArticleCardBinding,
  ) : Presenter.ViewHolder(binding.root) {
    var article: Article? = null
  }
}