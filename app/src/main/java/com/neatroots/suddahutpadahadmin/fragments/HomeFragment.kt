package com.neatroots.suddahutpadahadmin.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.neatroots.suddahutpadahadmin.R
import com.neatroots.suddahutpadahadmin.databinding.DialogExitBinding
import com.neatroots.suddahutpadahadmin.databinding.FragmentHomeBinding
import com.neatroots.suddahutpadahadmin.databinding.NavigationLayoutBinding
import com.neatroots.suddahutpadahadmin.model.UserModel
import com.neatroots.suddahutpadahadmin.utils.SharedPref
import com.neatroots.suddahutpadahadmin.utils.Utils


class HomeFragment : Fragment() {
    private val binding by lazy { FragmentHomeBinding.inflate(layoutInflater) }
    private lateinit var user: UserModel
    private lateinit var bottomSheet: BottomSheetDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        user = SharedPref.getUserData(requireContext()) ?: UserModel()
        bottomSheet = BottomSheetDialog(requireContext())

        binding.apply {

            btMenu.setOnClickListener {
                showExitDialog()
            }

            btAddService.setOnClickListener {
                Utils.navigate(it, R.id.action_homeFragment_to_addServiceFragment)
            }

            btUsers.setOnClickListener {
                Utils.navigate(it, R.id.action_homeFragment_to_usersListFragment)
            }

            btViewOrders.setOnClickListener {
                Utils.navigate(it, R.id.action_homeFragment_to_ordersListFragment)
            }

            btCategory.setOnClickListener {
                Utils.navigate(it, R.id.action_homeFragment_to_categoryFragment)
            }




        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                if(bottomSheet.isShowing) {
                    bottomSheet.dismiss()
                } else {
                    showExitDialog()
                }
            }

        })

    }

    private fun showMoreOptions() {
        bottomSheet = BottomSheetDialog(requireContext())
        val layout = NavigationLayoutBinding.inflate(layoutInflater)
        bottomSheet.setContentView(layout.root)
        bottomSheet.setCanceledOnTouchOutside(true)


        bottomSheet.show()

    }


    private fun showExitDialog() {
        val bottomSheet = BottomSheetDialog(requireContext())
        val layout = DialogExitBinding.inflate(layoutInflater)
        bottomSheet.setContentView(layout.root)
        bottomSheet.setCanceledOnTouchOutside(true)
        layout.btExit.setOnClickListener {
            bottomSheet.dismiss()
            requireActivity().finish()
        }
        layout.btCancel.setOnClickListener {
            bottomSheet.dismiss()
        }
        bottomSheet.show()
    }

}