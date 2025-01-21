package com.neatroots.suddahutpadahadmin.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.neatroots.suddahutpadahadmin.adapter.OrderedUserAdapter
import com.neatroots.suddahutpadahadmin.databinding.FragmentOrdersListBinding
import com.neatroots.suddahutpadahadmin.factory.MainViewModelFactory
import com.neatroots.suddahutpadahadmin.repository.MainRepository
import com.neatroots.suddahutpadahadmin.utils.Utils.gone
import com.neatroots.suddahutpadahadmin.utils.Utils.visible
import com.neatroots.suddahutpadahadmin.viewmodel.MainViewModel


class OrdersListFragment : Fragment() {
    private val binding by lazy { FragmentOrdersListBinding.inflate(layoutInflater) }
    private lateinit var mainViewModel: MainViewModel
    private lateinit var adapter: OrderedUserAdapter

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
        adapter = OrderedUserAdapter()

        binding.apply {

            mainViewModel.fetchAllOrderedUsers()

            mainViewModel.orderedUsersList.observe(viewLifecycleOwner) { list ->
                if (list.isNotEmpty()) {
                    loadingLayout.gone()
                    mainLayout.visible()
                    rv.adapter = adapter
                    adapter.submitList(list)
                } else {
                    loadingLayout.visible()
                    mainLayout.gone()
                    tvStatus.text = "No Orders Found"
                }

            }
        }


    }

}