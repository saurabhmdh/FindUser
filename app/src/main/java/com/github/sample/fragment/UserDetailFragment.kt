package com.github.sample.fragment

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.github.sample.layout.UserInfoScreen
import com.github.sample.viewmodel.SearchSuggestionViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserDetailFragment : Fragment() {
    private val viewModel: SearchSuggestionViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ) = ComposeView(requireContext()).apply {
        setContent {
            val userName = arguments?.getString("userName") ?: "saurabhmdh"
            UserInfoScreen(
                userName = userName,
                modifier = Modifier, viewModel = viewModel,
                onBackClick = { findNavController().popBackStack() },
                onItemClick = { url -> openUrl(url) }
            )
        }
    }


    private fun openUrl(url: String) {
        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(requireContext(), Uri.parse(url))
    }
}
