package com.example.jatis.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.jatis.databinding.ItemSourceBinding
import com.example.jatis.models.Source

class SourceAdapter(listener: ((source: Source) -> Unit)): RecyclerView.Adapter<SourceAdapter.ViewHolder>(), Filterable {

    private var mList: MutableList<Source> = mutableListOf()
    private var resultList: MutableList<Source> = mutableListOf()
    private var onItemClickListener: ((source: Source) -> Unit)? = null

    init {
        onItemClickListener = listener
    }

    fun performSearch(search: String?) {
        this.filter.filter(search)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemSourceBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(source: List<Source>?) {
        mList.clear()
        resultList.clear()
        source?.let {
            if (it.isNotEmpty()) {
                mList.addAll(it)
                resultList.addAll(it)
            }
        }
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.populateUI(resultList[position])
    }

    override fun getItemCount(): Int {
        return resultList.size
    }

    inner class ViewHolder(private val binding: ItemSourceBinding): RecyclerView.ViewHolder(binding.root) {
        private var mSource: Source? = null

        init {
            binding.root.setOnClickListener {
                mSource?.let { s ->
                    onItemClickListener?.invoke(s)
                }
            }
        }

        fun populateUI(source: Source) {
            mSource = source
            with(binding) {
                tvName.text = source.name
                tvDescription.text = source.description
            }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResult = FilterResults()
                filterResult.values = if (constraint == null) {
                    mList
                } else {
                    mList.filter { it.name?.lowercase()?.contains(constraint.toString().lowercase()) == true }
                }
                return filterResult
            }

            @SuppressLint("NotifyDataSetChanged")
            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                val list = results?.values as List<Source>
                resultList.clear()
                if (list.isNotEmpty()) resultList.addAll(list)
                notifyDataSetChanged()
            }
        }
    }
}