package com.example.jatis.ui.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.jatis.databinding.ItemCategoryBinding

class CategoryAdapter(listener: ((category: String) -> Unit)): RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    private var mList = listOf("Business", "Entertainment", "General", "Health", "Science", "Sports", "Technology")
    private var onItemClickListener: ((category: String) -> Unit)? = null
    private var mSelectedPosition: Int = -1

    init {
        onItemClickListener = listener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun selectItem(position: Int, callback: ((category: String) -> Unit)) {
        mSelectedPosition = position
        notifyDataSetChanged()
        callback.invoke(mList[position].lowercase())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemCategoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.populateUI(mList[position], position)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    inner class ViewHolder(private val binding: ItemCategoryBinding): RecyclerView.ViewHolder(binding.root) {

        private var mCategory: String? = null

        init {
            binding.tvCategory.setOnClickListener {
                mCategory?.let { s ->
                    onItemClickListener?.invoke(s.lowercase())
                    mSelectedPosition = bindingAdapterPosition
                    notifyDataSetChanged()
                }
            }
        }

        fun populateUI(category: String, position: Int) {
            mCategory = category
            with(binding) {
                tvCategory.text = category
                tvCategory.setBackgroundColor(
                    if (mSelectedPosition == position) Color.YELLOW else Color.WHITE
                )
            }
        }
    }
}