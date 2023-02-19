package com.ho2ri2s.androidtvplayground

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.commit
import com.ho2ri2s.androidtvplayground.databinding.ActivityPlaygroundBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlaygroundActivity : FragmentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_playground)
    val binding = ActivityPlaygroundBinding.inflate(layoutInflater)
    if (savedInstanceState == null) {
      supportFragmentManager.commit {
        setReorderingAllowed(true)
        add(binding.fragmentContainer.id, PlaygroundFragment::class.java, null)
      }
    }
  }
}