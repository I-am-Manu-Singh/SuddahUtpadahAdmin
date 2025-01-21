package com.neatroots.suddahutpadahadmin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.neatroots.suddahutpadahadmin.databinding.ItemUserBinding
import com.neatroots.suddahutpadahadmin.fragments.OrdersListFragmentDirections
import com.neatroots.suddahutpadahadmin.model.UserModel


class OrderedUserAdapter : ListAdapter<UserModel, OrderedUserAdapter.CategoryVH>(DiffUtils) {
    inner class CategoryVH(val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root)

    object DiffUtils : DiffUtil.ItemCallback<UserModel>() {
        override fun areItemsTheSame(oldItem: UserModel, newItem: UserModel): Boolean {
            return oldItem.userId == newItem.userId
        }

        override fun areContentsTheSame(oldItem: UserModel, newItem: UserModel): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryVH {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryVH(binding)
    }

    override fun onBindViewHolder(holder: CategoryVH, position: Int) {
        val item = getItem(position)

        holder.binding.apply {

//            ivUserImage.load(item.hsImage1) {
//                placeholder(R.drawable.placeholder)
//                error(R.drawable.placeholder)
//            }

            tvName.text = item.name
            tvPhoneNumber.text = item.phoneNumber
            tvCity.text = "${item.city} - ${item.pinCode}"

            holder.itemView.setOnClickListener {
                val action = OrdersListFragmentDirections.actionOrdersListFragmentToUserOrderListFragment(item)
                Navigation.findNavController(it).navigate(action)
            }


        }

        }





}







