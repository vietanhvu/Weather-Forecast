package com.example.nabtest.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.sleeptracker.ForecastsAdapter
import com.example.nabtest.LoadingState
import com.example.nabtest.databinding.MainFragmentBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val mainViewModel: MainViewModel by viewModels()

    private lateinit var binding: MainFragmentBinding

    private lateinit var forecastsAdapter: ForecastsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MainFragmentBinding.inflate(LayoutInflater.from(context), container, false)

        forecastsAdapter = ForecastsAdapter()

        mainViewModel.apply {
            listForecast.observe(viewLifecycleOwner) {
                forecastsAdapter.submitList(it)
            }
            loadingState.observe(viewLifecycleOwner) {
                binding.swipeRefresh.isRefreshing = it == LoadingState.REFRESHING
            }
            errorText.observe(viewLifecycleOwner) {
                if (!it.isNullOrEmpty()) {
                    Snackbar.make(
                        binding.root,
                        it,
                        Snackbar.LENGTH_SHORT // How long to display the message.
                    ).show()
                    mainViewModel.errorText.value = null
                }
            }
        }

        binding.apply {
            viewModel = mainViewModel
            recyclerView.apply {
                layoutManager = LinearLayoutManager(context)
                addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
                adapter = forecastsAdapter
            }
            inputText.setOnEditorActionListener { _, _, event ->
                // If triggered by an enter key, this is the event; otherwise, this is null.
                if (event != null) {
                    // if shift key is down, then we want to insert the '\n' char in the TextView;
                    // otherwise, the default action is to send the message.
                    if (!event.isShiftPressed) {
                        mainViewModel.search()
                        return@setOnEditorActionListener true
                    }
                    return@setOnEditorActionListener false
                }

                mainViewModel.search()
                return@setOnEditorActionListener true
            }
            swipeRefresh.setOnRefreshListener {
                mainViewModel.search(true)
            }
            lifecycleOwner = this@MainFragment
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel.search()
    }

    override fun onDestroy() {
        mainViewModel.onCleared()
        super.onDestroy()
    }
}