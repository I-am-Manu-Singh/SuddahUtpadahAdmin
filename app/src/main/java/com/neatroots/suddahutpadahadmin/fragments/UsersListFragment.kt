package com.neatroots.suddahutpadahadmin.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.auth.User
import com.neatroots.suddahutpadahadmin.adapter.UserAdapter
import com.neatroots.suddahutpadahadmin.databinding.FragmentUsersListBinding
import com.neatroots.suddahutpadahadmin.factory.AuthViewModelFactory
import com.neatroots.suddahutpadahadmin.model.UserModel
import com.neatroots.suddahutpadahadmin.repository.AuthRepository
import com.neatroots.suddahutpadahadmin.utils.Utils.gone
import com.neatroots.suddahutpadahadmin.utils.Utils.visible
import com.neatroots.suddahutpadahadmin.viewmodel.AuthViewModel


class UsersListFragment : Fragment() {
    private val binding by  lazy { FragmentUsersListBinding.inflate(layoutInflater) }
    private lateinit var authViewModel: AuthViewModel
    private lateinit var adapter: UserAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repository = AuthRepository(FirebaseAuth.getInstance(), requireContext())
        val factory = AuthViewModelFactory(repository)
        authViewModel = ViewModelProvider(requireActivity(), factory) [AuthViewModel::class.java]
        adapter = UserAdapter()

        binding.apply {

            authViewModel.usersList.observe(viewLifecycleOwner) { list ->
                if (list.isNotEmpty()) {
                    loadingLayout.gone()
                    mainLayout.visible()
                    rv.adapter = adapter
                    adapter.submitList(list)
                    search(list)
                } else {
                    loadingLayout.visible()
                    mainLayout.gone()
                    tvStatus.text = "No Services Found"
                }


            }
        }

    }

    private fun search(list: List<UserModel>) {
        binding.searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if(list.isNotEmpty()) {
                    filteredList(newText, list)
                }
                return true
            }

        })

    }

    private fun filteredList(newText: String?, list: List<UserModel>) {
        val filteredList = ArrayList<UserModel>()
        for (model in list) {
            if (model.name.contains(newText.orEmpty(), ignoreCase = true) ||
                model.city.contains(newText.orEmpty(), ignoreCase = true) ||
                model.pinCode.contains(newText.orEmpty(), ignoreCase = true) ||
                model.phoneNumber.contains(newText.orEmpty(), ignoreCase = true)

            )
                filteredList.add(model)
        }
        adapter.submitList(filteredList)
        binding.rv.adapter = adapter

    }

}