package com.neatroots.suddahutpadahadmin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.neatroots.suddahutpadahadmin.R
import com.neatroots.suddahutpadahadmin.databinding.ItemOrdersBinding
import com.neatroots.suddahutpadahadmin.model.CartModel
import com.neatroots.suddahutpadahadmin.model.OrderModel


class OrdersAdapter(private val onClick: OnItemClickListener, private val orderModel: OrderModel) : ListAdapter<CartModel, OrdersAdapter.CategoryVH>(DiffUtils) {
    inner class CategoryVH(val binding: ItemOrdersBinding) : RecyclerView.ViewHolder(binding.root)

    object DiffUtils : DiffUtil.ItemCallback<CartModel>() {
        override fun areItemsTheSame(oldItem: CartModel, newItem: CartModel): Boolean {
            return oldItem.productId == newItem.productId
        }

        override fun areContentsTheSame(oldItem: CartModel, newItem: CartModel): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryVH {
        val binding = ItemOrdersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryVH(binding)
    }

    override fun onBindViewHolder(holder: CategoryVH, position: Int) {
        val item = getItem(position)

        holder.binding.apply {

            ivServiceImage.load(item.productImage) {
                placeholder(R.drawable.placeholder)
                error(R.drawable.placeholder)
            }

            val price = item.price.toInt() * item.qty.toInt()
            tvPrice.text = "₹ $price"
            tvQty.text = "${item.qty} Qty"
            tvStatus.text = item.status
            tvTitle.text = item.title
            tvDate.text = item.orderDate

            btUpdateStatus.setOnClickListener {
                onClick.onItemClick(item, position, orderModel)
            }



        }


    }


    interface OnItemClickListener {
        fun onItemClick(cart: CartModel, position: Int, order: OrderModel)
    }


}







