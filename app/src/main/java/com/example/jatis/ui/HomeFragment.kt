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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jatis.databinding.FragmentHomeBinding
import com.example.jatis.ui.adapter.CategoryAdapter
import com.example.jatis.ui.adapter.SourceAdapter
import com.example.jatis.utils.Resource
import com.example.jatis.viewmodel.AppViewModel
import kotlinx.coroutines.launch

class HomeFragment: BaseFragment() {

    private lateinit var binding: FragmentHomeBinding
    private val appViewModel: AppViewModel by activityViewModels()

    private lateinit var mSourceAdapter: SourceAdapter
    private lateinit var mCategoryAdapter: CategoryAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setMainEvent(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        populateUI()
    }

    private fun populateUI() = with(binding) {
        mSourceAdapter = SourceAdapter { source ->
            source.id?.let {
                appViewModel.setArticleSource(it) //save ke viewmodel dulu
                mainEvent?.openFragment(NewsFragment()) //open news fragment (list)
            }
        }
        mCategoryAdapter = CategoryAdapter {
            appViewModel.getSources(it)
        }

        subscribeToSources()
        setupSearch()
        setUpRecyclerview()
    }

    private fun setUpRecyclerview() = with(binding) {
        val lmCategory = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        rvCategory.apply {
            layoutManager = lmCategory
            setHasFixedSize(true)
            itemAnimator = DefaultItemAnimator()
            adapter = mCategoryAdapter
        }

        val lmSource = GridLayoutManager(requireActivity(), 2, GridLayoutManager.VERTICAL, false)
        rvSource.apply {
            layoutManager = lmSource
            setHasFixedSize(true)
            itemAnimator = DefaultItemAnimator()
            adapter = mSourceAdapter
        }

        mCategoryAdapter.selectItem(0) {
            appViewModel.getSources(it)
        }
    }

    private fun setupSearch() = with(binding) {
        svSource.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                mSourceAdapter.performSearch(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val searchText = newText ?: ""
                mSourceAdapter.performSearch(searchText)
                return true
            }
        })
    }

    private fun subscribeToSources() {
        lifecycleScope.launch {
            appViewModel.sources.collect { resource ->
                resource?.let {
                    when (it) {
                        is Resource.Empty, is Resource.Error -> {
                            binding.paginationProgressBar.visibility = View.GONE
                            binding.rvSource.visibility = View.GONE
                            mSourceAdapter.submitList(null)
                            it.error?.let { error ->
                                Toast.makeText(
                                    requireActivity(),
                                    error.asString(requireActivity()),
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                        is Resource.Loading -> {
                            binding.rvSource.visibility = View.GONE
                            binding.paginationProgressBar.visibility = View.VISIBLE
                        }
                        is Resource.Success -> {
                            binding.paginationProgressBar.visibility = View.GONE
                            binding.rvSource.visibility = View.VISIBLE

                            val data = it.data ?: emptyList()
                            mSourceAdapter.submitList(data)
                        }
                    }
                }
            }
        }
    }
}
