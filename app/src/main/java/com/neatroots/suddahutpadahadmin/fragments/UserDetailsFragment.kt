package com.neatroots.suddahutpadahadmin.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.neatroots.suddahutpadahadmin.databinding.FragmentUserDetailsBinding
import com.neatroots.suddahutpadahadmin.model.UserModel
import com.neatroots.suddahutpadahadmin.utils.Utils

class UserDetailsFragment : Fragment() {
    private val binding by lazy { FragmentUserDetailsBinding.inflate(layoutInflater) }
    private val user: UserDetailsFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setData(user.user)

    }

    private fun setData(user: UserModel) {
        binding.apply {
            etHomeStayName.setText(user.name)
            etaddress1.setText(user.address)
            etCityName.setText(user.city)
            etEmailId.setText(user.email)
            etMobileNumber.setText(user.phoneNumber)
            etPincode.setText(user.pinCode)

            etMobileNumber.setOnClickListener {
                Utils.openDialer(requireContext(), user.phoneNumber)
            }



        }
    }

}