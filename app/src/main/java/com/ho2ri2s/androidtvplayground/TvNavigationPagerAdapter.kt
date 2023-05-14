package com.ho2ri2s.androidtvplayground

import android.content.Context
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import timber.log.Timber
import kotlin.reflect.KClass

class TvNavigationPagerAdapter(
  private val context: Context,
  fragmentManager: FragmentManager,
) : FragmentPagerAdapter(fragmentManager) {

  private val navItems = listOf(
    NavigationItem(R.string.navigation_playground, PlaygroundFragment::class),
    NavigationItem(R.string.navigation_tv_compose, TvComposeFragment::class),
  )

  override fun getCount(): Int = navItems.size

  override fun getItem(position: Int): Fragment {
    Timber.w("mytag position = $position")
    val fragment = navItems[position].createFragment()
    Timber.w("mytag fragment = $fragment")
    return fragment
  }

  override fun getPageTitle(position: Int): CharSequence {
    return navItems[position].getTitle(context)
  }

  private class NavigationItem<T : Fragment>(
    @StringRes val titleResId: Int,
    val fragmentClazz: KClass<T>,
  ) {
    fun createFragment(): T = fragmentClazz.java.newInstance()
    fun getTitle(context: Context): String = context.getString(titleResId)
  }
}