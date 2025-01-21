package com.neatroots.suddahutpadahadmin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.neatroots.suddahutpadahadmin.R
import com.neatroots.suddahutpadahadmin.databinding.ItemServiceBinding
import com.neatroots.suddahutpadahadmin.fragments.AddServiceFragmentDirections
import com.neatroots.suddahutpadahadmin.model.ProductModel



class ServiceAdapter(private val onClick: OnItemClickListener) : ListAdapter<ProductModel, ServiceAdapter.CategoryVH>(DiffUtils) {
    inner class CategoryVH(val binding: ItemServiceBinding) : RecyclerView.ViewHolder(binding.root)

    object DiffUtils : DiffUtil.ItemCallback<ProductModel>() {
        override fun areItemsTheSame(oldItem: ProductModel, newItem: ProductModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProductModel, newItem: ProductModel): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryVH {
        val binding = ItemServiceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryVH(binding)
    }

    override fun onBindViewHolder(holder: CategoryVH, position: Int) {
        val item = getItem(position)

        holder.binding.apply {

            ivServiceImage.load(item.image) {
                placeholder(R.drawable.placeholder)
                error(R.drawable.placeholder)
            }

            tvTitle.text = item.title
            tvDescription.text = item.description
            tvPrice.text = "₹${item.offerPrice}"

            btDelete.setOnClickListener {
                onClick.onItemDelete(item)
            }

            holder.itemView.setOnClickListener {
                val action = AddServiceFragmentDirections.actionAddServiceFragmentToEditServiceFragment(item)
                Navigation.findNavController(it).navigate(action)
            }

        }

    }


    interface OnItemClickListener {
        fun onItemDelete(service: ProductModel)


    }

}







