package com.example.jatis.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.jatis.databinding.FragmentNewsDetailBinding
import com.example.jatis.utils.MyWebClient
import com.example.jatis.viewmodel.AppViewModel
import kotlinx.coroutines.launch

class NewsDetailFragment: BaseFragment() {

    private lateinit var binding: FragmentNewsDetailBinding
    private val appViewModel: AppViewModel by activityViewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setMainEvent(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewsDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        populateUI()
    }

    private fun populateUI() = with(binding) {
        setupWebView()
        loadNews()
    }

    private fun loadNews() = lifecycleScope.launch {
        appViewModel.news.collect { news ->
            news?.let {
                it.url?.let { url ->
                    binding.vwNewsDetail.loadUrl(url)
                }
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() = with(binding) {
        val mWebSettings: WebSettings = vwNewsDetail.settings
        mWebSettings.javaScriptEnabled = true
        mWebSettings.cacheMode = WebSettings.LOAD_DEFAULT
        mWebSettings.loadWithOverviewMode = true
        mWebSettings.useWideViewPort = true
        mWebSettings.builtInZoomControls = true
        mWebSettings.displayZoomControls = false
        mWebSettings.databaseEnabled = true
        mWebSettings.domStorageEnabled = true
        vwNewsDetail.webViewClient = MyWebClient()
        vwNewsDetail.webChromeClient = mChromeClient
    }

    private var mChromeClient: WebChromeClient = object : WebChromeClient() {}
}
