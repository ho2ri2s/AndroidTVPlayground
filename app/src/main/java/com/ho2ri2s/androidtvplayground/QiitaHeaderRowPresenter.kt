package com.ho2ri2s.androidtvplayground

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.leanback.widget.Row
import androidx.leanback.widget.RowPresenter
import com.ho2ri2s.androidtvplayground.databinding.LayoutQiitaHeaderBinding

class QiitaHeaderRowPresenter : RowPresenter() {

  override fun createRowViewHolder(parent: ViewGroup): RowPresenter.ViewHolder {
    val layoutInflater = LayoutInflater.from(parent.context)
    val binding = LayoutQiitaHeaderBinding.inflate(
      layoutInflater,
      parent,
      false,
    )

    return ViewHolder(binding)
  }

  override fun onBindRowViewHolder(viewHolder: RowPresenter.ViewHolder?, item: Any?) {
    viewHolder as ViewHolder
    val row = item as Row
    val userName = row.headerItem?.name
    viewHolder.userName = userName

    viewHolder.binding.userName.text = userName
  }

  class ViewHolder(
    val binding: LayoutQiitaHeaderBinding
  ) : RowPresenter.ViewHolder(binding.root) {
    var userName: String? = null
  }
}
