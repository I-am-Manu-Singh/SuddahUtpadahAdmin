package com.neatroots.suddahutpadahadmin.fragmentauth

import android.app.Activity
import android.app.AlertDialog
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.neatroots.suddahutpadahadmin.R
import com.neatroots.suddahutpadahadmin.databinding.FragmentRegisterBinding
import com.neatroots.suddahutpadahadmin.factory.AuthViewModelFactory
import com.neatroots.suddahutpadahadmin.repository.AuthRepository
import com.neatroots.suddahutpadahadmin.utils.Constants
import com.neatroots.suddahutpadahadmin.utils.Utils
import com.neatroots.suddahutpadahadmin.viewmodel.AuthViewModel
import suddahutpadahadmin.fragmentauth.RegisterFragmentDirections
import java.util.UUID


class RegisterFragment : Fragment() {
    private val binding by lazy { FragmentRegisterBinding.inflate(layoutInflater) }
    private lateinit var storage: FirebaseStorage
    private lateinit var authViewModel: AuthViewModel
    private lateinit var progress: AlertDialog
    private var image1: Uri? = null
    private var image2: Uri? = null
    private var licence: Uri? = null
    private var image1Url: String = ""
    private var image2Url: String = ""
    private var licenceUrl: String = ""
    private var address = ""

    private val startForImageResult2 =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val resultCode = result.resultCode
            val data = result.data

            when (resultCode) {
                Activity.RESULT_OK -> {
                    image2 = data?.data!!
                    binding.ivHomestayImage2.setImageURI(image2)
                }

                ImagePicker.RESULT_ERROR -> {
                    Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT)
                        .show()
                }

                else -> {
                    Toast.makeText(requireContext(), "Task Cancelled", Toast.LENGTH_SHORT).show()
                }
            }
        }

    private val startForImageResult1 =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val resultCode = result.resultCode
            val data = result.data

            when (resultCode) {
                Activity.RESULT_OK -> {
                    image1 = data?.data!!
                    binding.ivHomestayImage1.setImageURI(image1)
                }

                ImagePicker.RESULT_ERROR -> {
                    Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT)
                        .show()
                }

                else -> {
                    Toast.makeText(requireContext(), "Task Cancelled", Toast.LENGTH_SHORT).show()
                }
            }
        }


    private val startForImageResultLicence =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val resultCode = result.resultCode
            val data = result.data

            when (resultCode) {
                Activity.RESULT_OK -> {
                    licence = data?.data!!
                    binding.ivLicence.setImageURI(licence)
                }

                ImagePicker.RESULT_ERROR -> {
                    Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT)
                        .show()
                }

                else -> {
                    Toast.makeText(requireContext(), "Task Cancelled", Toast.LENGTH_SHORT).show()
                }
            }
        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        storage = FirebaseStorage.getInstance()
        progress = Utils.showLoading(requireContext())


        val authRepository = AuthRepository(FirebaseAuth.getInstance(), requireContext())
        val factory = AuthViewModelFactory(authRepository)
        authViewModel = ViewModelProvider(requireActivity(), factory)[AuthViewModel::class.java]


        authViewModel.authStatus.observe(viewLifecycleOwner) { success ->
            if(success) {
                val action = RegisterFragmentDirections.actionRegisterFragmentToVerificationFragment(binding.etEmailId.text.toString())
                findNavController().navigate(action)
                progress.dismiss()
            } else {
                progress.dismiss()
                Utils.showMessage(requireContext(), "Registration Failed")
            }

        }


        binding.apply {

            ivHomestayImage1.setOnClickListener {
                ImagePicker.with(this@RegisterFragment)
                    .crop()
                    .createIntent { intent ->
                        startForImageResult1.launch(intent)
                    }
            }

            ivHomestayImage2.setOnClickListener {
                ImagePicker.with(this@RegisterFragment)
                    .crop()
                    .createIntent { intent ->
                        startForImageResult2.launch(intent)
                    }
            }

            ivLicence.setOnClickListener {
                ImagePicker.with(this@RegisterFragment)
                    .crop()
                    .createIntent { intent ->
                        startForImageResultLicence.launch(intent)
                    }
            }

            tvLogin.setOnClickListener {
                Utils.navigate(it, R.id.action_registerFragment_to_loginFragment)
            }

            tvLogin1.setOnClickListener {
                Utils.navigate(it, R.id.action_registerFragment_to_loginFragment)
            }

            btRegister.setOnClickListener {
                validateUserInput()
            }
        }

    }

    private fun validateUserInput() {
        binding.apply {
            val message = "Empty"
            if (etHomeStayName.text.toString().isEmpty()) {
                etHomeStayName.requestFocus()
                etHomeStayName.error = message
            } else if (etaddress1.text.toString().isEmpty()) {
                etaddress1.requestFocus()
                etaddress1.error = message
            } else if (etAddress2.text.toString().isEmpty()) {
                etAddress2.requestFocus()
                etAddress2.error = message
            } else if (etMobileNumber.text.toString().isEmpty()) {
                etMobileNumber.requestFocus()
                etMobileNumber.error = message
            } else if (etCityName.text.toString().isEmpty()) {
                etCityName.requestFocus()
                etCityName.error = message
            } else if (etPincode.text.toString().isEmpty()) {
                etPincode.requestFocus()
                etPincode.error = message
            } else if (etEmailId.text.toString().isEmpty()) {
                etEmailId.requestFocus()
                etEmailId.error = message
            } else if (etCreatePassword.text.toString().isEmpty()) {
                etCreatePassword.requestFocus()
                etCreatePassword.error = message
            } else if (etConfirmPassword.text.toString().isEmpty()) {
                etConfirmPassword.requestFocus()
                etConfirmPassword.error = message
            }  else if (etConfirmPassword.text.toString() != etCreatePassword.text.toString()) {
                etConfirmPassword.error = "Error"
                etCreatePassword.error = "Error"
            } else if (image1 == null) {
               Utils.showMessage(requireContext(), "Please Select Image 1")
            } else if (image2 == null) {
                Utils.showMessage(requireContext(), "Please Select Image 2")
            } else {
                progress.show()
                address = "${etaddress1.text.toString()} ${etAddress2.text.toString()} ${etCityName.text.toString()}, ${etPincode.text.toString()}"
                uploadImage1(image1)
            }
        }
    }

    private fun uploadImage1(image1Uri: Uri?) {
        val fileName = UUID.randomUUID().toString() + ".jpg"
        storage.getReference(Constants.USER_IMAGES + fileName).putFile(image1Uri!!)
            .addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener {imgUrl ->
                    image1Url = imgUrl.toString()
                    uploadImage2(image2)
                }
            }.addOnFailureListener {
                progress.dismiss()
                Utils.showMessage(requireContext(), "Image Upload Failed")
            }
    }


    private fun uploadImage2(image2Uri: Uri?) {
        val fileName = UUID.randomUUID().toString() + ".jpg"
        storage.getReference(Constants.USER_IMAGES + fileName).putFile(image2Uri!!)
            .addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener {imgUrl ->
                    image2Url = imgUrl.toString()
                    if(licence != null) {
                        uploadLicence(licence)
                    } else {
                        progress.dismiss()
                        createRegister()
                    }
                }
            }.addOnFailureListener {
                progress.dismiss()
                Utils.showMessage(requireContext(), "Image Upload Failed")
            }
    }


    private fun uploadLicence(licence: Uri?) {
        val fileName = UUID.randomUUID().toString() + ".jpg"
        storage.getReference(Constants.USER_IMAGES + fileName).putFile(licence!!)
            .addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener {imgUrl ->
                    licenceUrl = imgUrl.toString()
                    createRegister()
                }
            }.addOnFailureListener {
                progress.dismiss()
                Utils.showMessage(requireContext(), "Image Upload Failed")
            }
    }

    private fun createRegister() {
        binding.apply {
            val licenceImage = if(licence != null) licenceUrl else ""
            authViewModel.registerUser(
                etHomeStayName.text.toString(),
                etMobileNumber.text.toString(),
                etEmailId.text.toString(),
                etConfirmPassword.text.toString(),
                address,
                "",
                image1Url,
                image2Url,
                licenceImage,
                etCityName.text.toString(),
                etPincode.text.toString(),
                verified = false,
                active = false
            )
        }
    }


}