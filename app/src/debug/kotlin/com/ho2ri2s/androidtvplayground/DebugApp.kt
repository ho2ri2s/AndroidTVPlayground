package com.ho2ri2s.androidtvplayground

import timber.log.Timber

class DebugApp : PlaygroundApp() {

  override fun onCreate() {
    super.onCreate()
    if (BuildConfig.DEBUG) {
      Timber.plant(Timber.DebugTree())
    }
  }
}