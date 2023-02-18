package com.ho2ri2s.androidtvplayground

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ho2ri2s.androidtvplayground.databinding.FragmentPlaygroundBinding

class PlaygroundFragment : Fragment() {

  private var binding: FragmentPlaygroundBinding? = null

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = FragmentPlaygroundBinding.inflate(inflater, null, false)
    return binding!!.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
  }

  override fun onDestroyView() {
    super.onDestroyView()
    binding = null
  }
}