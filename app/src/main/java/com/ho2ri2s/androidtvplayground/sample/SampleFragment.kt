package com.ho2ri2s.androidtvplayground.sample

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.leanback.app.BackgroundManager
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.HeaderItem
import androidx.leanback.widget.ImageCardView
import androidx.leanback.widget.ListRow
import androidx.leanback.widget.ListRowPresenter
import androidx.leanback.widget.OnItemViewClickedListener
import androidx.leanback.widget.OnItemViewSelectedListener
import androidx.leanback.widget.Presenter
import androidx.leanback.widget.Row
import androidx.leanback.widget.RowPresenter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.ho2ri2s.androidtvplayground.R
import timber.log.Timber
import java.util.Collections
import java.util.Timer
import java.util.TimerTask

/**
 * Loads a grid of cards with movies to browse.
 */
class SampleFragment : BrowseSupportFragment() {

  private val mHandler = Handler(Looper.myLooper()!!)
  private lateinit var mBackgroundManager: BackgroundManager
  private var mDefaultBackground: Drawable? = null
  private lateinit var mMetrics: DisplayMetrics
  private var mBackgroundTimer: Timer? = null
  private var mBackgroundUri: String? = null

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    Timber.i("onCreate")

    prepareBackgroundManager()

    setupUIElements()

    loadRows()

    setupEventListeners()
  }

  override fun onDestroy() {
    super.onDestroy()
    Timber.d("onDestroy: " + mBackgroundTimer?.toString())
    mBackgroundTimer?.cancel()
  }

  @Suppress("DEPRECATION")
  private fun prepareBackgroundManager() {

    mBackgroundManager = BackgroundManager.getInstance(activity)
    mBackgroundManager.attach(requireActivity().window)
    mDefaultBackground = ContextCompat.getDrawable(requireContext(), R.drawable.default_background)
    mMetrics = DisplayMetrics()
    requireActivity().windowManager.defaultDisplay.getMetrics(mMetrics)
  }

  private fun setupUIElements() {
    title = getString(R.string.browse_title)
    // over title
    headersState = BrowseSupportFragment.HEADERS_ENABLED
    isHeadersTransitionOnBackEnabled = true

    // set fastLane (or headers) background color
    brandColor = ContextCompat.getColor(requireContext(), R.color.fastlane_background)
    // set search icon color
    searchAffordanceColor = ContextCompat.getColor(requireContext(), R.color.search_opaque)
  }

  private fun loadRows() {
    val list = MovieList.list

    val rowsAdapter = ArrayObjectAdapter(ListRowPresenter())
    val cardPresenter = CardPresenter()

    for (i in 0 until NUM_ROWS) {
      if (i != 0) {
        Collections.shuffle(list)
      }
      val listRowAdapter = ArrayObjectAdapter(cardPresenter)
      for (j in 0 until NUM_COLS) {
        listRowAdapter.add(list[j % 5])
      }
      val header = HeaderItem(i.toLong(), MovieList.MOVIE_CATEGORY[i])
      rowsAdapter.add(ListRow(header, listRowAdapter))
    }

    val gridHeader = HeaderItem(NUM_ROWS.toLong(), "PREFERENCES")

    val mGridPresenter = GridItemPresenter()
    val gridRowAdapter = ArrayObjectAdapter(mGridPresenter)
    gridRowAdapter.add(resources.getString(R.string.grid_view))
    gridRowAdapter.add(getString(R.string.error_fragment))
    gridRowAdapter.add(resources.getString(R.string.personal_settings))
    rowsAdapter.add(ListRow(gridHeader, gridRowAdapter))

    adapter = rowsAdapter
  }

  private fun setupEventListeners() {
    setOnSearchClickedListener {
      Toast.makeText(requireContext(), "Implement your own in-app search", Toast.LENGTH_LONG)
        .show()
    }

    onItemViewClickedListener = ItemViewClickedListener()
    onItemViewSelectedListener = ItemViewSelectedListener()
  }

  private inner class ItemViewClickedListener : OnItemViewClickedListener {
    override fun onItemClicked(
      itemViewHolder: Presenter.ViewHolder,
      item: Any,
      rowViewHolder: RowPresenter.ViewHolder,
      row: Row
    ) {

      if (item is Movie) {
        Timber.d("Item: $item")
        val intent = Intent(context!!, SampleDetailsActivity::class.java)
        intent.putExtra(SampleDetailsActivity.MOVIE, item)

        val bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(
          activity!!,
          (itemViewHolder.view as ImageCardView).mainImageView,
          SampleDetailsActivity.SHARED_ELEMENT_NAME
        )
          .toBundle()
        startActivity(intent, bundle)
      } else if (item is String) {
        if (item.contains(getString(R.string.error_fragment))) {
          val intent = Intent(context!!, BrowseErrorActivity::class.java)
          startActivity(intent)
        } else {
          Toast.makeText(context!!, item, Toast.LENGTH_SHORT).show()
        }
      }
    }
  }

  private inner class ItemViewSelectedListener : OnItemViewSelectedListener {
    override fun onItemSelected(
      itemViewHolder: Presenter.ViewHolder?, item: Any?,
      rowViewHolder: RowPresenter.ViewHolder, row: Row
    ) {
      if (item is Movie) {
        mBackgroundUri = item.backgroundImageUrl
        startBackgroundTimer()
      }
    }
  }

  private fun updateBackground(uri: String?) {
    val width = mMetrics.widthPixels
    val height = mMetrics.heightPixels
    Glide.with(requireContext())
      .load(uri)
      .centerCrop()
      .error(mDefaultBackground)
      .into<CustomTarget<Drawable>>(
        object : CustomTarget<Drawable>(width, height) {
          override fun onResourceReady(
            drawable: Drawable,
            transition: Transition<in Drawable>?
          ) {
            mBackgroundManager.drawable = drawable
          }

          override fun onLoadCleared(placeholder: Drawable?) = Unit
        })
    mBackgroundTimer?.cancel()
  }

  private fun startBackgroundTimer() {
    mBackgroundTimer?.cancel()
    mBackgroundTimer = Timer()
    mBackgroundTimer?.schedule(UpdateBackgroundTask(), BACKGROUND_UPDATE_DELAY.toLong())
  }

  private inner class UpdateBackgroundTask : TimerTask() {

    override fun run() {
      mHandler.post { updateBackground(mBackgroundUri) }
    }
  }

  private inner class GridItemPresenter : Presenter() {
    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
      val view = TextView(parent.context)
      view.layoutParams = ViewGroup.LayoutParams(GRID_ITEM_WIDTH, GRID_ITEM_HEIGHT)
      view.isFocusable = true
      view.isFocusableInTouchMode = true
      view.setBackgroundColor(ContextCompat.getColor(context!!, R.color.default_background))
      view.setTextColor(Color.WHITE)
      view.gravity = Gravity.CENTER
      return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, item: Any) {
      (viewHolder.view as TextView).text = item as String
    }

    override fun onUnbindViewHolder(viewHolder: ViewHolder) {}
  }

  companion object {
    private val TAG = "SampleFragment"

    private val BACKGROUND_UPDATE_DELAY = 300
    private val GRID_ITEM_WIDTH = 200
    private val GRID_ITEM_HEIGHT = 200
    private val NUM_ROWS = 6
    private val NUM_COLS = 15
  }
}