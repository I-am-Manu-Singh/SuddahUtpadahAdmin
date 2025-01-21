package com.neatroots.suddahutpadahadmin.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.neatroots.suddahutpadahadmin.adapter.OrdersListAdapter
import com.neatroots.suddahutpadahadmin.databinding.FragmentUserOrderListBinding
import com.neatroots.suddahutpadahadmin.factory.MainViewModelFactory
import com.neatroots.suddahutpadahadmin.repository.MainRepository
import com.neatroots.suddahutpadahadmin.utils.Utils.gone
import com.neatroots.suddahutpadahadmin.utils.Utils.visible
import com.neatroots.suddahutpadahadmin.viewmodel.MainViewModel


class UserOrderListFragment : Fragment() {
    private val binding by lazy { FragmentUserOrderListBinding.inflate(layoutInflater) }
    private lateinit var mainViewModel: MainViewModel
    private lateinit var adapter: OrdersListAdapter
    private val user: UserOrderListFragmentArgs by navArgs()


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
        mainViewModel = ViewModelProvider(requireActivity(), factory) [MainViewModel::class.java]
        adapter = OrdersListAdapter(user.user, requireContext())


        binding.apply {

            mainViewModel.fetchUserOrders(user.user.userId)

            mainViewModel.ordersList.observe(viewLifecycleOwner) { list ->
                if (list.isNotEmpty()) {
                    loadingLayout.gone()
                    mainLayout.visible()
                    rv.adapter = adapter
                    adapter.submitList(list)

                    //search(list)
                } else {
                    loadingLayout.visible()
                    mainLayout.gone()
                    tvStatus.text = "No Orders Found"
                }


            }
        }



    }
}