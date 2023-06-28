package com.example.jatis.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.jatis.databinding.ItemNewsBinding
import com.example.jatis.models.News

class NewsAdapter(listener: ((news: News) -> Unit)): RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    private var mList: MutableList<News> = mutableListOf()
    private var onItemClickListener: ((news: News) -> Unit)? = null

    init {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemNewsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(source: List<News>?) {
        mList.clear()
        source?.let {
            if (it.isNotEmpty()) mList.addAll(it)
        }
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.populateUI(mList[position])
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    inner class ViewHolder(private val binding: ItemNewsBinding): RecyclerView.ViewHolder(binding.root) {
        private var mNews: News? = null

        init {
            binding.root.setOnClickListener {
                mNews?.let { s ->
                    onItemClickListener?.invoke(s)
                }
            }
        }

        fun populateUI(news: News) {
            mNews = news
            with(binding) {
                Glide.with(binding.root.context)
                    .load(news.urlToImage)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .fitCenter()
                    .into(ivItemFeedContent)
                tvItemFeedTitle.text = news.title
                tvItemFeedContentDescription.text = news.description
                tvItemFeedAuthor.text = news.author
                tvItemFeedContentUrl.text = news.content
            }
        }
    }
}