package com.neatroots.suddahutpadahadmin.fragments

import android.app.Activity
import android.app.AlertDialog
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.storage.FirebaseStorage
import com.neatroots.suddahutpadahadmin.R
import com.neatroots.suddahutpadahadmin.adapter.ServiceAdapter
import com.neatroots.suddahutpadahadmin.databinding.FragmentAddServiceBinding
import com.neatroots.suddahutpadahadmin.factory.MainViewModelFactory
import com.neatroots.suddahutpadahadmin.model.ProductModel
import com.neatroots.suddahutpadahadmin.repository.MainRepository
import com.neatroots.suddahutpadahadmin.utils.Constants
import com.neatroots.suddahutpadahadmin.utils.Utils
import com.neatroots.suddahutpadahadmin.viewmodel.MainViewModel
import java.util.UUID

class AddServiceFragment : Fragment(), ServiceAdapter.OnItemClickListener {
    private val binding by lazy { FragmentAddServiceBinding.inflate(layoutInflater) }
    private lateinit var progress: AlertDialog
    private lateinit var mainViewModel: MainViewModel
    private lateinit var adapter: ServiceAdapter
    private lateinit var storage: FirebaseStorage
    private lateinit var categories: ArrayList<Pair<String, String>>
    private var categoryId: String = ""
    private  var serviceImage: Uri? = null

    private val startForImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val resultCode = result.resultCode
            val data = result.data

            when (resultCode) {
                Activity.RESULT_OK -> {
                    serviceImage = data?.data!!
                    binding.ivServiceImage.setImageURI(serviceImage)
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
        val repository = MainRepository()
        val factory = MainViewModelFactory(repository)
        mainViewModel = ViewModelProvider(this@AddServiceFragment, factory) [MainViewModel::class.java]
        adapter = ServiceAdapter(this@AddServiceFragment)

        mainViewModel.fetchCategories()

        binding.apply {

            mainViewModel.status.observe(viewLifecycleOwner) {
                if(it) {
                    clearData()
                    progress.dismiss()
                } else {
                    progress.dismiss()
                }
            }


            mainViewModel.servicesList.observe(viewLifecycleOwner) { list ->
                if(list.isNotEmpty()) {
                    rv.adapter = adapter
                    adapter.submitList(list)
                } else {
                    rv.adapter = adapter
                    adapter.submitList(emptyList())
                    Utils.showMessage(requireContext(), "No Service Found")
                }

            }

            mainViewModel.categoryList.observe(viewLifecycleOwner) { list ->
                if(list.isNotEmpty()) {
                    categories = ArrayList()
                    list.forEach { categories.add(Pair(it.categoryName, it.id)) }
                    val categoryNames = categories.map { it.first }
                    val adapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, categoryNames)
                    etCategory.setAdapter(adapter)

                    etCategory.setOnItemClickListener { parent, view, position, id ->
                        categoryId = categories[position].second
                    }

                } else {
                    Utils.showMessage(requireContext(), "No Category Found")
                }

            }


            ivServiceImage.setOnClickListener {
                ImagePicker.with(this@AddServiceFragment)
                    .crop()
                    .createIntent { intent ->
                        startForImageResult.launch(intent)
                    }
            }

            btAddService.setOnClickListener {
                checkServiceInputs()
            }


        }


    }

    private fun clearData() {
        binding.apply {
            ivServiceImage.setImageResource(R.drawable.placeholder)
            serviceImage = null
            etName.text = null
            etServiceDescription.text = null
            etOfferPrice.text = null
            etOriginalPrice.text = null
            etRating.text = null
        }
    }

    private fun checkServiceInputs() {
        binding.apply {
            val msg = "Empty"
            if(etName.text.toString().isEmpty()) {
                etName.requestFocus()
                etName.error = msg
            } else if(etServiceDescription.text.toString().isEmpty()) {
                etServiceDescription.requestFocus()
                etServiceDescription.error = msg
            } else if(etOfferPrice.text.toString().isEmpty()) {
                etOfferPrice.requestFocus()
                etOfferPrice.error = msg
            } else if(etOriginalPrice.text.toString().isEmpty()) {
                etOriginalPrice.requestFocus()
                etOriginalPrice.error = msg
            } else if(etRating.text.toString().isEmpty()) {
                etRating.requestFocus()
                etRating.error = msg
            } else if(serviceImage == null) {
                Utils.showMessage(requireContext(), "Add Service Image")
            }  else {
                progress.show()
                uploadServiceImage(serviceImage)
            }

        }
    }

    private fun uploadServiceImage(imageUri: Uri?) {
        val fileName = UUID.randomUUID().toString() + ".jpg"
        if(imageUri != null) {
            storage.getReference(Constants.USER_IMAGES + fileName).putFile(imageUri)
                .addOnSuccessListener {
                    it.storage.downloadUrl.addOnSuccessListener {imgUrl ->
                        createService(imgUrl.toString())
                    }
                }.addOnFailureListener {
                    progress.dismiss()
                    Utils.showMessage(requireContext(), "Image Upload Failed")
                }
        }
    }

    private fun createService(serviceImage: String) {

        mainViewModel.createService(
            serviceImage,
            binding.etName.text.toString(),
            binding.etServiceDescription.text.toString(),
            binding.etOfferPrice.text.toString(),
            binding.etOriginalPrice.text.toString(),
            binding.etRating.text.toString(),
            binding.etCategory.text.toString(),
            categoryId,
            true
        )
    }

    override fun onItemDelete(service: ProductModel) {
        progress.show()
        mainViewModel.deleteService(service.id)
    }

}