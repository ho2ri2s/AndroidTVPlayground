package com.example.androidtvplayground

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.leanback.widget.Presenter
import com.ho2ri2s.androidtvplayground.databinding.LayoutArticleCardBinding

class ArticleCardPresenter : Presenter() {

  override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
    val layoutInflater = LayoutInflater.from(parent.context)
    val binding = LayoutArticleCardBinding.inflate(layoutInflater, parent, false)

    return ViewHolder(binding)
  }

  override fun onBindViewHolder(viewHolder: Presenter.ViewHolder?, item: Any?) {
    val binding = (viewHolder as ViewHolder).binding
    binding.title.text = "タイトルだよ"
  }

  override fun onUnbindViewHolder(viewHolder: Presenter.ViewHolder?) = Unit

  class ViewHolder(
    val binding: LayoutArticleCardBinding,
  ) : Presenter.ViewHolder(binding.root)
}