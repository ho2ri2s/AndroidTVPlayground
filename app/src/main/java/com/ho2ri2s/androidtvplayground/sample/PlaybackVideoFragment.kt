package com.ho2ri2s.androidtvplayground.sample

import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.leanback.app.VideoSupportFragment
import androidx.leanback.app.VideoSupportFragmentGlueHost
import androidx.leanback.media.MediaPlayerAdapter
import androidx.leanback.media.PlaybackTransportControlGlue
import androidx.leanback.widget.PlaybackControlsRow

/** Handles video playback with media controls. */
class PlaybackVideoFragment : VideoSupportFragment() {

  private lateinit var mTransportControlGlue: PlaybackTransportControlGlue<MediaPlayerAdapter>

  @RequiresApi(Build.VERSION_CODES.TIRAMISU)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val (_, title, description, _, _, videoUrl) =
      activity?.intent?.getSerializableExtra(SampleDetailsActivity.MOVIE, Movie::class.java) as Movie

    val glueHost = VideoSupportFragmentGlueHost(this@PlaybackVideoFragment)
    val playerAdapter = MediaPlayerAdapter(context)
    playerAdapter.setRepeatAction(PlaybackControlsRow.RepeatAction.INDEX_NONE)

    mTransportControlGlue = PlaybackTransportControlGlue(activity, playerAdapter)
    mTransportControlGlue.host = glueHost
    mTransportControlGlue.title = title
    mTransportControlGlue.subtitle = description
    mTransportControlGlue.playWhenPrepared()

    playerAdapter.setDataSource(Uri.parse(videoUrl))
  }

  override fun onPause() {
    super.onPause()
    mTransportControlGlue.pause()
  }
}