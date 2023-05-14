package com.ho2ri2s.androidtvplayground

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.DrawerValue
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Icon
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.NavigationDrawer
import androidx.tv.material3.Text
import androidx.tv.material3.rememberDrawerState

@OptIn(ExperimentalTvMaterial3Api::class)
class TvComposeActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      MaterialTheme {
        TvComposeDrawerNavigation()
      }
    }
  }

  @Composable
  fun TvComposeDrawerNavigation(modifier: Modifier = Modifier) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    NavigationDrawer(
      modifier = modifier,
      drawerState = drawerState,
      drawerContent = { value ->
        DrawerNavigationItems(drawerValue = value)
      }
    ) {
      Screen1()
    }
  }

  @Composable
  fun Screen1() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
      Text("Screen1", fontSize = 30.sp, color = Color.White)
    }
  }

  @Composable
  fun Screen2() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
      Text("Screen2")
    }
  }

  @Composable
  fun DrawerNavigationItems(drawerValue: DrawerValue) {
    Column(
      modifier = Modifier
        .fillMaxHeight()
        .background(Color.Black),
      verticalArrangement = Arrangement.Center,
    ) {
      NavigationItems().items.forEach { item ->
        Row(
          modifier = Modifier
            .focusable()
            .padding(vertical = 16.dp, horizontal = 32.dp),
          horizontalArrangement = Arrangement.Center,
        ) {
          Icon(
            modifier = Modifier.size(24.dp),
            painter = painterResource(id = item.iconResId),
            contentDescription = null,
            tint = Color.White,
          )
          Spacer(modifier = Modifier.size(8.dp))
          AnimatedVisibility(visible = drawerValue == DrawerValue.Open) {
            Text(
              text = stringResource(id = item.titleResId),
              textAlign = TextAlign.Center,
              color = Color.White,
            )
          }
        }
      }
    }
  }

  data class NavigationItem(@StringRes val titleResId: Int, @DrawableRes val iconResId: Int)
  class NavigationItems {
    val items = listOf(
      NavigationItem(R.string.navigation_compose_home, R.drawable.ic_home_white),
      NavigationItem(R.string.navigation_compose_tv, R.drawable.ic_tv_white),
    )
  }

  companion object {
    fun createIntent(context: Context): Intent {
      return Intent(context, TvComposeActivity::class.java)
    }
  }
}