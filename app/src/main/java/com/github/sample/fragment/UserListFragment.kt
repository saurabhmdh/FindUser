package com.github.sample.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.github.sample.R
import com.github.sample.layout.SearchSuggestionScreen
import com.github.sample.viewmodel.SearchSuggestionViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserListFragment : Fragment() {
    private val viewModel: SearchSuggestionViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ) = ComposeView(requireContext()).apply {
        setContent {
            SearchSuggestionScreen(viewModel = viewModel, onItemClick = { userName ->
                findNavController().navigate(R.id.userDetailFragment, bundleOf("userName" to userName))
            },
            onNavigateBack = {
                handleBackKey()
            })
        }
    }

    private fun handleBackKey() {
        activity?.onBackPressed()
    }
}