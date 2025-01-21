package com.neatroots.suddahutpadahadmin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.neatroots.suddahutpadahadmin.R
import com.neatroots.suddahutpadahadmin.databinding.ItemCategoryBinding
import com.neatroots.suddahutpadahadmin.model.CategoryModel


class CategoryAdapter(private val onClick: OnItemClickListener): ListAdapter<CategoryModel, CategoryAdapter.CategoryVH>(DiffUtils) {
    inner class CategoryVH(val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root)

    object DiffUtils : DiffUtil.ItemCallback<CategoryModel>() {
        override fun areItemsTheSame(oldItem: CategoryModel, newItem: CategoryModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CategoryModel, newItem: CategoryModel): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryVH {
        val binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryVH(binding)
    }

    override fun onBindViewHolder(holder: CategoryVH, position: Int) {
        val item = getItem(position)

        holder.binding.apply {

            ivCategory.load(item.image) {
                placeholder(R.drawable.placeholder)
                error(R.drawable.placeholder)
            }

            tvCategory.text = item.categoryName

            btDelete.setOnClickListener {
                onClick.onDelete(item)
            }

            holder.itemView.setOnClickListener {
                onClick.onEdit(item)
            }


        }


    }

    interface OnItemClickListener {
        fun onDelete(category: CategoryModel)
        fun onEdit(category: CategoryModel)
    }


}







