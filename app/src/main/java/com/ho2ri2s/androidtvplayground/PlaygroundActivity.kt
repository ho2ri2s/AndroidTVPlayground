package com.ho2ri2s.androidtvplayground

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ho2ri2s.androidtvplayground.databinding.ActivityPlaygroundBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlaygroundActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val binding = ActivityPlaygroundBinding.inflate(layoutInflater)
    setContentView(binding.root)
    val adapter = TvNavigationPagerAdapter(this, supportFragmentManager)
    binding.viewPager.adapter = adapter
    binding.tabLayout.setupWithViewPager(binding.viewPager)

    binding.tabLayout.getTabAt(0)?.view?.requestFocus()
  }
}