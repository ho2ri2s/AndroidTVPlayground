package com.ho2ri2s.androidtvplayground.sample

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.leanback.app.DetailsSupportFragment
import androidx.leanback.app.DetailsSupportFragmentBackgroundController
import androidx.leanback.widget.Action
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.ClassPresenterSelector
import androidx.leanback.widget.DetailsOverviewRow
import androidx.leanback.widget.FullWidthDetailsOverviewRowPresenter
import androidx.leanback.widget.FullWidthDetailsOverviewSharedElementHelper
import androidx.leanback.widget.HeaderItem
import androidx.leanback.widget.ImageCardView
import androidx.leanback.widget.ListRow
import androidx.leanback.widget.ListRowPresenter
import androidx.leanback.widget.OnActionClickedListener
import androidx.leanback.widget.OnItemViewClickedListener
import androidx.leanback.widget.Presenter
import androidx.leanback.widget.Row
import androidx.leanback.widget.RowPresenter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.ho2ri2s.androidtvplayground.R
import timber.log.Timber
import java.util.Collections

/**
 * A wrapper fragment for leanback details screens.
 * It shows a detailed view of video and its metadata plus related videos.
 */
class SampleVideoDetailsFragment : DetailsSupportFragment() {

  private var mSelectedMovie: Movie? = null

  private lateinit var mDetailsBackground: DetailsSupportFragmentBackgroundController
  private lateinit var mPresenterSelector: ClassPresenterSelector
  private lateinit var mAdapter: ArrayObjectAdapter

  @RequiresApi(Build.VERSION_CODES.TIRAMISU)
  override fun onCreate(savedInstanceState: Bundle?) {
    Timber.d("onCreate DetailsFragment")
    super.onCreate(savedInstanceState)

    mDetailsBackground = DetailsSupportFragmentBackgroundController(this)

    mSelectedMovie = requireActivity().intent.getSerializableExtra(
      SampleDetailsActivity.MOVIE,
      Movie::class.java
    ) as Movie
    if (mSelectedMovie != null) {
      mPresenterSelector = ClassPresenterSelector()
      mAdapter = ArrayObjectAdapter(mPresenterSelector)
      setupDetailsOverviewRow()
      setupDetailsOverviewRowPresenter()
      setupRelatedMovieListRow()
      adapter = mAdapter
      initializeBackground(mSelectedMovie)
      onItemViewClickedListener = ItemViewClickedListener()
    } else {
      val intent = Intent(requireContext(), SampleActivity::class.java)
      startActivity(intent)
    }
  }

  private fun initializeBackground(movie: Movie?) {
    mDetailsBackground.enableParallax()
    Glide.with(requireContext())
      .asBitmap()
      .centerCrop()
      .error(R.drawable.default_background)
      .load(movie?.backgroundImageUrl)
      .into<CustomTarget<Bitmap>>(object : CustomTarget<Bitmap>() {
        override fun onResourceReady(
          bitmap: Bitmap,
          transition: Transition<in Bitmap>?
        ) {
          mDetailsBackground.coverBitmap = bitmap
          mAdapter.notifyArrayItemRangeChanged(0, mAdapter.size())
        }

        override fun onLoadCleared(placeholder: Drawable?) {
          TODO("Not yet implemented")
        }
      })
  }

  private fun setupDetailsOverviewRow() {
    Timber.d("doInBackground: " + mSelectedMovie?.toString())
    val row = DetailsOverviewRow(mSelectedMovie)
    row.imageDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.default_background)
    val width = convertDpToPixel(requireContext(), DETAIL_THUMB_WIDTH)
    val height = convertDpToPixel(requireContext(), DETAIL_THUMB_HEIGHT)
    Glide.with(requireContext())
      .load(mSelectedMovie?.cardImageUrl)
      .centerCrop()
      .error(R.drawable.default_background)
      .into<CustomTarget<Drawable>>(object : CustomTarget<Drawable>(width, height) {
        override fun onResourceReady(
          drawable: Drawable,
          transition: Transition<in Drawable>?
        ) {
          Timber.d("details overview card image url ready: $drawable")
          row.imageDrawable = drawable
          mAdapter.notifyArrayItemRangeChanged(0, mAdapter.size())
        }

        override fun onLoadCleared(placeholder: Drawable?) {
          TODO("Not yet implemented")
        }
      })

    val actionAdapter = ArrayObjectAdapter()

    actionAdapter.add(
      Action(
        ACTION_WATCH_TRAILER,
        resources.getString(R.string.watch_trailer_1),
        resources.getString(R.string.watch_trailer_2)
      )
    )
    actionAdapter.add(
      Action(
        ACTION_RENT,
        resources.getString(R.string.rent_1),
        resources.getString(R.string.rent_2)
      )
    )
    actionAdapter.add(
      Action(
        ACTION_BUY,
        resources.getString(R.string.buy_1),
        resources.getString(R.string.buy_2)
      )
    )
    row.actionsAdapter = actionAdapter

    mAdapter.add(row)
  }

  private fun setupDetailsOverviewRowPresenter() {
    // Set detail background.
    val detailsPresenter = FullWidthDetailsOverviewRowPresenter(DetailsDescriptionPresenter())
    detailsPresenter.backgroundColor =
      ContextCompat.getColor(requireContext(), R.color.selected_background)

    // Hook up transition element.
    val sharedElementHelper = FullWidthDetailsOverviewSharedElementHelper()
    sharedElementHelper.setSharedElementEnterTransition(
      activity, SampleDetailsActivity.SHARED_ELEMENT_NAME
    )
    detailsPresenter.setListener(sharedElementHelper)
    detailsPresenter.isParticipatingEntranceTransition = true

    detailsPresenter.onActionClickedListener = OnActionClickedListener { action ->
      if (action.id == ACTION_WATCH_TRAILER) {
        val intent = Intent(requireContext(), PlaybackActivity::class.java)
        intent.putExtra(SampleDetailsActivity.MOVIE, mSelectedMovie)
        startActivity(intent)
      } else {
        Toast.makeText(requireContext(), action.toString(), Toast.LENGTH_SHORT).show()
      }
    }
    mPresenterSelector.addClassPresenter(DetailsOverviewRow::class.java, detailsPresenter)
  }

  private fun setupRelatedMovieListRow() {
    val subcategories = arrayOf(getString(R.string.related_movies))
    val list = MovieList.list

    Collections.shuffle(list)
    val listRowAdapter = ArrayObjectAdapter(CardPresenter())
    for (j in 0 until NUM_COLS) {
      listRowAdapter.add(list[j % 5])
    }

    val header = HeaderItem(0, subcategories[0])
    mAdapter.add(ListRow(header, listRowAdapter))
    mPresenterSelector.addClassPresenter(ListRow::class.java, ListRowPresenter())
  }

  private fun convertDpToPixel(context: Context, dp: Int): Int {
    val density = context.applicationContext.resources.displayMetrics.density
    return Math.round(dp.toFloat() * density)
  }

  private inner class ItemViewClickedListener : OnItemViewClickedListener {
    override fun onItemClicked(
      itemViewHolder: Presenter.ViewHolder?,
      item: Any?,
      rowViewHolder: RowPresenter.ViewHolder,
      row: Row
    ) {
      if (item is Movie) {
        Timber.tag(TAG).d("Item: %s", item.toString())
        val intent = Intent(requireContext(), SampleDetailsActivity::class.java)
        intent.putExtra(resources.getString(R.string.movie), mSelectedMovie)

        val bundle =
          ActivityOptionsCompat.makeSceneTransitionAnimation(
            requireActivity(),
            (itemViewHolder?.view as ImageCardView).mainImageView,
            SampleDetailsActivity.SHARED_ELEMENT_NAME
          )
            .toBundle()
        startActivity(intent, bundle)
      }
    }
  }

  companion object {
    private val TAG = "SampleVideoDetailsFragm"

    private val ACTION_WATCH_TRAILER = 1L
    private val ACTION_RENT = 2L
    private val ACTION_BUY = 3L

    private val DETAIL_THUMB_WIDTH = 274
    private val DETAIL_THUMB_HEIGHT = 274

    private val NUM_COLS = 10
  }
}