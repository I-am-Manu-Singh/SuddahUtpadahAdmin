package com.neatroots.suddahutpadahadmin.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.neatroots.suddahutpadahadmin.databinding.ItemMainOrderBinding
import com.neatroots.suddahutpadahadmin.fragments.UserOrderListFragmentDirections
import com.neatroots.suddahutpadahadmin.model.OrderModel
import com.neatroots.suddahutpadahadmin.model.UserModel
import com.neatroots.suddahutpadahadmin.utils.Utils


class OrdersListAdapter(val user: UserModel, val context: Context) : ListAdapter<OrderModel, OrdersListAdapter.CategoryVH>(DiffUtils) {
    inner class CategoryVH(val binding: ItemMainOrderBinding) : RecyclerView.ViewHolder(binding.root)

    object DiffUtils : DiffUtil.ItemCallback<OrderModel>() {
        override fun areItemsTheSame(oldItem: OrderModel, newItem: OrderModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: OrderModel, newItem: OrderModel): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryVH {
        val binding = ItemMainOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryVH(binding)
    }

    override fun onBindViewHolder(holder: CategoryVH, position: Int) {
        val item = getItem(position)

        holder.binding.apply {

//            ivImage.load(user.hsImage1) {
//                placeholder(R.drawable.placeholder)
//                error(R.drawable.placeholder)
//            }

            tvAmount.text = "₹ ${item.totalAmount}"
            tvAddress.text = user.address
            tvName.text = user.name
            tvDate.text = item.orderDate
            tvPhoneNumber.text = user.phoneNumber

            btCall.setOnClickListener {
                Utils.openDialer(context, user.phoneNumber)
            }

            holder.itemView.setOnClickListener {
                val action = UserOrderListFragmentDirections.actionUserOrderListFragmentToOrdersFragment(user, item)
                Navigation.findNavController(it).navigate(action)
            }


        }

        }





}







