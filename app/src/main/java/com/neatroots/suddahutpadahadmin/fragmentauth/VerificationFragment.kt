package com.neatroots.suddahutpadahadmin.fragmentauth

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.FirebaseAuth
import com.neatroots.suddahutpadahadmin.R
import com.neatroots.suddahutpadahadmin.databinding.FragmentVerificationBinding
import com.neatroots.suddahutpadahadmin.factory.AuthViewModelFactory
import com.neatroots.suddahutpadahadmin.repository.AuthRepository
import com.neatroots.suddahutpadahadmin.utils.Utils
import com.neatroots.suddahutpadahadmin.viewmodel.AuthViewModel
import suddahutpadahadmin.fragmentauth.VerificationFragmentArgs


class VerificationFragment : Fragment() {
    private val binding by lazy { FragmentVerificationBinding.inflate(layoutInflater) }
    private lateinit var authViewModel: AuthViewModel
    private lateinit var progress: AlertDialog
    private val emailId : VerificationFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progress = Utils.showLoading(requireContext())

        val authRepository = AuthRepository(FirebaseAuth.getInstance(), requireContext())
        val factory = AuthViewModelFactory(authRepository)
        authViewModel = ViewModelProvider(requireActivity(), factory)[AuthViewModel::class.java]

        authViewModel.verificationLink.observe(viewLifecycleOwner) { success ->
            if(success) {
                progress.dismiss()
                binding.btLogin.text = "Login Account"
                Utils.showMessage(requireContext(), "Verification Link Sent")

                binding.btLogin.setOnClickListener {
                    if(success) {
                        findNavController().navigate(R.id.action_verificationFragment_to_loginFragment)
                    } else {

                        Utils.showMessage(requireContext(), "Please Wait")
                    }
                }

            } else {
                progress.dismiss()
                Utils.showMessage(requireContext(), "Link Send Failed")
            }

        }

        binding.apply {
            tvEmailId.text = emailId.emailId

            btLogin.setOnClickListener {
                progress.show()
                authViewModel.sendVerificationLink()
            }
        }
    }


}