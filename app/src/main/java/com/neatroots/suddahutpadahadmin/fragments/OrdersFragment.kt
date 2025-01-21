package com.neatroots.suddahutpadahadmin.fragments

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.neatroots.suddahutpadahadmin.R
import com.neatroots.suddahutpadahadmin.adapter.OrdersAdapter
import com.neatroots.suddahutpadahadmin.databinding.DialogOrderStatusBinding
import com.neatroots.suddahutpadahadmin.databinding.FragmentOrdersBinding
import com.neatroots.suddahutpadahadmin.factory.MainViewModelFactory
import com.neatroots.suddahutpadahadmin.model.CartModel
import com.neatroots.suddahutpadahadmin.model.OrderModel
import com.neatroots.suddahutpadahadmin.model.UserModel
import com.neatroots.suddahutpadahadmin.repository.MainRepository
import com.neatroots.suddahutpadahadmin.utils.Utils
import com.neatroots.suddahutpadahadmin.utils.Utils.gone
import com.neatroots.suddahutpadahadmin.utils.Utils.visible
import com.neatroots.suddahutpadahadmin.viewmodel.MainViewModel


class OrdersFragment : Fragment(), OrdersAdapter.OnItemClickListener {
    private val binding by lazy { FragmentOrdersBinding.inflate(layoutInflater) }
    private val user: OrdersFragmentArgs by navArgs()
    private lateinit var mainViewModel: MainViewModel
    private lateinit var adapter: OrdersAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repository = MainRepository()
        val factory = MainViewModelFactory(repository)
        mainViewModel = ViewModelProvider(this@OrdersFragment, factory) [MainViewModel::class.java]
        adapter = OrdersAdapter(this@OrdersFragment, user.order)

        binding.apply {

            setUserData(user.user, user.order)
            mainViewModel.fetchUserOrders(user.user.userId)

            mainViewModel.ordersList.observe(viewLifecycleOwner) { list ->
                if (list.isNotEmpty()) {

                    val matchingOrder = list.find { order -> order.id == user.order.id }
                    matchingOrder?.let {
                        rv.adapter = adapter
                        adapter.submitList(it.orderedItems)
                    }
                    loadingLayout.gone()
                    mainLayout.visible()


                }
            }


        }

    }

    private fun setUserData(user: UserModel, order: OrderModel) {
        binding.apply {
            tvAddress.text = user.address
            tvName.text = user.name
            tvPhoneNumber.text = user.phoneNumber
            tvAmount.text = "₹ ${order.totalAmount}"
            tvDate.text = "₹ ${order.orderDate}"
        }
    }

    override fun onItemClick(cart: CartModel, position: Int, order: OrderModel) {
        showUpdateDialog(cart, position, order)
    }

    private fun showUpdateDialog(cart: CartModel, position: Int, order: OrderModel) {
        val dialog = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog).create()
        val layout = DialogOrderStatusBinding.inflate(layoutInflater)
        dialog.setView(layout.root)

        layout.apply {

            btClose.setOnClickListener {
                dialog.dismiss()
            }

            rGroup.setOnCheckedChangeListener { _, checkedId ->
                val selectedRadioButton: RadioButton = root.findViewById(checkedId)
                val selectedText = selectedRadioButton.text.toString()

                btUpdateStatus.setOnClickListener {
                    when(selectedText) {
                        "Ordered" -> {
                            mainViewModel.updateOrderStatus(order, adapter.currentList, selectedText, position)
                        }

                        "Accepted" -> {
                            mainViewModel.updateOrderStatus(order, adapter.currentList, selectedText, position)
                        }

                        "On the Way" -> {
                            mainViewModel.updateOrderStatus(order, adapter.currentList, selectedText, position)
                        }

                        "Delivered" -> {
                            mainViewModel.clearAndSaveAsHistory(user.user.userId, order, adapter.currentList, selectedText, position)
                        }

                        "Completed" -> {
                            mainViewModel.clearAndSaveAsHistory(user.user.userId, order, adapter.currentList, selectedText, position)
                        }

                        "Canceled" -> {
                            mainViewModel.clearAndSaveAsHistory(user.user.userId, order, adapter.currentList, selectedText, position)
                        }

                        "Refunded" -> {
                            mainViewModel.clearAndSaveAsHistory(user.user.userId, order, adapter.currentList, selectedText, position)
                        }

                    }
                    Utils.showMessage(requireContext(), "Order Status Updated")
                    dialog.dismiss()
                }

            }

        }

        dialog.show()
    }


}