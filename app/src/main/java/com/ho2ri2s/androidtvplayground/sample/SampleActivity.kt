package com.ho2ri2s.androidtvplayground.sample

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.ho2ri2s.androidtvplayground.R

/**
 * Loads [SampleFragment].
 */
class SampleActivity : FragmentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_sample)
    if (savedInstanceState == null) {
      supportFragmentManager.beginTransaction()
        .replace(R.id.main_browse_fragment, SampleFragment())
        .commitNow()
    }
  }
}