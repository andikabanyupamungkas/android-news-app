package com.example.jatis.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jatis.databinding.FragmentNewsBinding
import com.example.jatis.ui.adapter.NewsAdapter
import com.example.jatis.utils.Const
import com.example.jatis.utils.EndlessRecyclerViewScroll
import com.example.jatis.utils.Resource
import com.example.jatis.utils.hide
import com.example.jatis.utils.show
import com.example.jatis.viewmodel.AppViewModel
import kotlinx.coroutines.launch
import kotlin.math.ceil

class NewsFragment : BaseFragment() {

    private lateinit var binding: FragmentNewsBinding
    private val appViewModel: AppViewModel by activityViewModels()

    private lateinit var mAdapter: NewsAdapter
    private var mSearchText: String? = null
    private var mPage = 1
    private var mHasNextPage = false
    private lateinit var mScrollListener: EndlessRecyclerViewScroll

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setMainEvent(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAdapter = NewsAdapter {
            println("my-app $it")
            appViewModel.setNews(it)
            mainEvent?.openFragment(NewsDetailFragment())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        populateUI()
    }

    private fun populateUI() = with(binding) {
        setupSearch()
        setupRecyclerView()

        subscribeToHeadLines()
        loadNewsList(true, null)
    }

    private fun setupRecyclerView() = with(binding) {
        val lm = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        mScrollListener = EndlessScroll(lm)
        rvNews.apply {
            layoutManager = lm
            setHasFixedSize(true)
            itemAnimator = DefaultItemAnimator()
            adapter = mAdapter
            addOnScrollListener(mScrollListener)
        }
    }

    private fun setupSearch() = with(binding) {
        svNews.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                mSearchText = query
                loadNewsList(false, mSearchText)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                mSearchText = newText
                val txt = mSearchText ?: ""
                return if (txt.isEmpty() || txt.length > 2) {
                    loadNewsList(false, mSearchText)
                    true
                } else false
            }
        })
    }

    private fun loadNewsList(initLoad: Boolean, search: String?) = lifecycleScope.launch {
        appViewModel.articleSource.collect { source ->
            source?.let {
                if (initLoad) {
                    mPage = 1
                    mHasNextPage = false
                    mScrollListener.resetState()
                    mAdapter.submitList(null)
                }
                appViewModel.getTopHeadLines(it, mPage, search)
            }
        }
    }

    private fun subscribeToHeadLines() = lifecycleScope.launch {
        appViewModel.topHeadLines.collect { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.rvNews.hide()
                    binding.pbNews.show()
                }
                is Resource.Empty,
                is Resource.Error -> {
                    binding.pbNews.hide()
                    binding.rvNews.hide()
                    resource.error?.let { error ->
                        Toast.makeText(
                            requireActivity(),
                            error.asString(requireActivity()),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                is Resource.Success -> {
                    binding.pbNews.hide()
                    binding.rvNews.show()
                    resource.data?.let {
                        val total = it.totalResults ?: 0
                        val totalPages = ceil(total / Const.PAGE_SIZE.toDouble()).toInt()
                        mHasNextPage = mPage < totalPages

                        val list = it.articles ?: emptyList()
                        println("my-app $list")
                        mAdapter.submitList(list)
                    }
                }

                else -> Unit
            }
        }
    }

    private inner class EndlessScroll(lm: RecyclerView.LayoutManager) :
        EndlessRecyclerViewScroll(lm as LinearLayoutManager) {
        override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
            if (mHasNextPage) {
                mPage++
                loadNewsList(false, mSearchText)
            }
        }
    }
}