package com.neatroots.suddahutpadahadmin.fragments

import android.app.Activity
import android.app.AlertDialog
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.storage.FirebaseStorage
import com.neatroots.suddahutpadahadmin.R
import com.neatroots.suddahutpadahadmin.databinding.FragmentEditServiceBinding
import com.neatroots.suddahutpadahadmin.factory.MainViewModelFactory
import com.neatroots.suddahutpadahadmin.model.ProductModel
import com.neatroots.suddahutpadahadmin.repository.MainRepository
import com.neatroots.suddahutpadahadmin.utils.Constants
import com.neatroots.suddahutpadahadmin.utils.Utils
import com.neatroots.suddahutpadahadmin.viewmodel.MainViewModel
import java.util.UUID


class EditServiceFragment : Fragment() {
    private val binding by lazy { FragmentEditServiceBinding.inflate(layoutInflater) }
    private var serviceImage: Uri? = null
    private val service: EditServiceFragmentArgs by navArgs()
    private lateinit var progress: AlertDialog
    private lateinit var mainViewModel: MainViewModel
    private lateinit var storage: FirebaseStorage
    private lateinit var categories: ArrayList<Pair<String, String>>
    private var categoryId: String = ""
    private var isChecked = false

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

        progress = Utils.showLoading(requireContext())
        storage = FirebaseStorage.getInstance()
        val repository = MainRepository()
        val factory = MainViewModelFactory(repository)
        mainViewModel = ViewModelProvider(this@EditServiceFragment, factory) [MainViewModel::class.java]
        categories = ArrayList()

        binding.rg.setOnCheckedChangeListener { _, checkedId ->
            isChecked = when (checkedId) {
                R.id.rbAvailable -> true
                R.id.rbNotAvailable -> false
                else -> false
            }
        }

        mainViewModel.fetchCategories()

        mainViewModel.categoryList.observe(viewLifecycleOwner) { list ->
            if(list.isNotEmpty()) {
                categories = ArrayList()
                list.forEach { categories.add(Pair(it.categoryName, it.id)) }
                val categoryNames = categories.map { it.first }
                val adapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, categoryNames)
                binding.etCategory.setAdapter(adapter)

                binding.etCategory.setOnItemClickListener { parent, view, position, id ->
                    categoryId = categories[position].second
                }

            } else {
                Utils.showMessage(requireContext(), "No Category Found")
            }

        }


        mainViewModel.status.observe(viewLifecycleOwner) {
            if(it) {
                progress.dismiss()
                findNavController().navigate(R.id.action_editServiceFragment_to_addServiceFragment)
            } else {
                progress.dismiss()
                Utils.showMessage(requireContext(), "Something went wrong")
            }
        }


        binding.apply {

            ivServiceImage.setOnClickListener {
                ImagePicker.with(this@EditServiceFragment)
                    .crop()
                    .createIntent { intent ->
                        startForImageResult.launch(intent)
                    }
            }

            btUpdateService.setOnClickListener {
                checkServiceInputs()
            }

            setData(service.service)

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
            } else {
                progress.show()
                if(serviceImage != null) {
                    uploadServiceImage(serviceImage)
                } else {
                    updateService(null)
                }
            }

        }
    }

    private fun updateService(serviceImage: String?) {
        val image = serviceImage ?: service.service.image
        categoryId = if(categoryId == "") service.service.categoryId else categoryId

        mainViewModel.updateService(
            service.service.id,
            image,
            binding.etName.text.toString(),
            binding.etServiceDescription.text.toString(),
            binding.etOfferPrice.text.toString(),
            binding.etOriginalPrice.text.toString(),
            binding.etCategory.text.toString(),
            categoryId,
            binding.etRating.text.toString(),
            isChecked
            )
    }

    private fun uploadServiceImage(imageUri: Uri?) {
        val fileName = UUID.randomUUID().toString() + ".jpg"
        if(imageUri != null) {
            storage.getReference(Constants.USER_IMAGES + fileName).putFile(imageUri)
                .addOnSuccessListener {
                    it.storage.downloadUrl.addOnSuccessListener {imgUrl ->
                        updateService(imgUrl.toString())
                    }
                }.addOnFailureListener {
                    progress.dismiss()
                    Utils.showMessage(requireContext(), "Image Upload Failed")
                }
        }
    }



    private fun setData(service: ProductModel) {
        binding.apply {
            ivServiceImage.load(service.image) {
                placeholder(R.drawable.placeholder)
                error(R.drawable.placeholder)
            }

            if(service.available) binding.rbAvailable.isChecked = true else binding.rbNotAvailable.isChecked = true

            etName.setText(service.title)
            etOfferPrice.setText(service.offerPrice)
            etOriginalPrice.setText(service.originalPrice)
            etRating.setText(service.rating)
            etServiceDescription.setText(service.description)
            etCategory.setText(service.category)
            val categoryNames = categories.map { it.first }
            val adapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, categoryNames)
            binding.etCategory.setAdapter(adapter)

        }
    }

}