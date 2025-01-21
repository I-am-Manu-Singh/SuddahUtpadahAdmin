package com.neatroots.suddahutpadahadmin.fragments

import android.app.AlertDialog
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.google.firebase.storage.FirebaseStorage
import com.neatroots.suddahutpadahadmin.R
import com.neatroots.suddahutpadahadmin.adapter.CategoryAdapter
import com.neatroots.suddahutpadahadmin.databinding.DialogDeleteBinding
import com.neatroots.suddahutpadahadmin.databinding.DialogEditCategoryBinding
import com.neatroots.suddahutpadahadmin.databinding.FragmentCategoryBinding
import com.neatroots.suddahutpadahadmin.factory.MainViewModelFactory
import com.neatroots.suddahutpadahadmin.model.CategoryModel
import com.neatroots.suddahutpadahadmin.repository.MainRepository
import com.neatroots.suddahutpadahadmin.utils.Constants
import com.neatroots.suddahutpadahadmin.utils.Utils
import com.neatroots.suddahutpadahadmin.viewmodel.MainViewModel
import java.util.UUID


class CategoryFragment : Fragment(), CategoryAdapter.OnItemClickListener {
    private val binding by lazy { FragmentCategoryBinding.inflate(layoutInflater) }
    private lateinit var progress: AlertDialog
    private lateinit var editDialog: AlertDialog
    private lateinit var deleteDialog: AlertDialog
    private lateinit var mainViewModel: MainViewModel
    private lateinit var layoutEditCategory: DialogEditCategoryBinding
    private lateinit var adapter: CategoryAdapter
    private lateinit var storage: FirebaseStorage
    private  var categoryImageUri: Uri? = null


    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            categoryImageUri = it
            binding.ivCategoryImage.setImageURI(categoryImageUri)
            layoutEditCategory.ivCurrentCategory.setImageURI(categoryImageUri)
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
        editDialog = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog).create()
        deleteDialog = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog).create()
        layoutEditCategory = DialogEditCategoryBinding.inflate(layoutInflater)
        val repository = MainRepository()
        val factory = MainViewModelFactory(repository)
        mainViewModel = ViewModelProvider(this@CategoryFragment, factory) [MainViewModel::class.java]
        adapter = CategoryAdapter(this@CategoryFragment)

        mainViewModel.fetchCategories()

        binding.apply {

            mainViewModel.status.observe(viewLifecycleOwner) {
                if(it) {
                    binding.etName.text = null
                    categoryImageUri = null
                    binding.ivCategoryImage.setImageResource(R.drawable.placeholder)
                    progress.dismiss()
                    editDialog.dismiss()
                    deleteDialog.dismiss()
                } else {
                    deleteDialog.dismiss()
                    progress.dismiss()
                    editDialog.dismiss()
                    Utils.showMessage(requireContext(), "Something went wrong")
                }
            }


            mainViewModel.categoryList.observe(viewLifecycleOwner) { list ->
                if(list.isNotEmpty()) {
                    rv.adapter = adapter
                    adapter.submitList(list)
                } else {
                    Utils.showMessage(requireContext(), "No Categories Found")
                }

            }


            ivCategoryImage.setOnClickListener {
                pickImageLauncher.launch("image/*")
            }

            btAddCategory.setOnClickListener {
                checkServiceInputs()
            }


        }


    }

    private fun checkServiceInputs() {
        binding.apply {
            val msg = "Empty"
            if(etName.text.toString().isEmpty()) {
                etName.requestFocus()
                etName.error = msg
            } else if(categoryImageUri == null) {
                Utils.showMessage(requireContext(), "Add Category Image")
            }  else {
                progress.show()
                uploadImage(categoryImageUri)
            }

        }
    }

    private fun uploadImage(imageUri: Uri?) {
        val fileName = UUID.randomUUID().toString() + ".jpg"
        if(imageUri != null) {
            storage.getReference(Constants.USER_IMAGES + fileName).putFile(imageUri)
                .addOnSuccessListener {
                    it.storage.downloadUrl.addOnSuccessListener {imgUrl ->
                        createCategory(imgUrl.toString())
                    }
                }.addOnFailureListener {
                    progress.dismiss()
                    Utils.showMessage(requireContext(), "Image Upload Failed")
                }
        }
    }

    private fun uploadUpdatedImage(imageUri: Uri?, category: CategoryModel, title: String) {
        val fileName = UUID.randomUUID().toString() + ".jpg"
        if(imageUri != null) {
            storage.getReference(Constants.USER_IMAGES + fileName).putFile(imageUri)
                .addOnSuccessListener {
                    it.storage.downloadUrl.addOnSuccessListener {imgUrl ->
                        mainViewModel.updateCategory(category.id, imgUrl.toString(), title)
                    }
                }.addOnFailureListener {
                    progress.dismiss()
                    Utils.showMessage(requireContext(), "Image Upload Failed")
                }
        }
    }

    private fun createCategory(image: String) {
        progress.show()
        mainViewModel.createCategory(image, binding.etName.text.toString())
    }

    override fun onDelete(category: CategoryModel) {
        editDialog.dismiss()
        showDeleteDialog(category)
    }

    override fun onEdit(category: CategoryModel) {
        editDialog = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog).create()
        layoutEditCategory = DialogEditCategoryBinding.inflate(layoutInflater)
        editDialog.setView(layoutEditCategory.root)
        editDialog.setCanceledOnTouchOutside(true)

        layoutEditCategory.apply {

            ivCurrentCategory.load(category.image) {
                placeholder(R.drawable.placeholder)
                error(R.drawable.placeholder)
            }

            ivCurrentCategory.setOnClickListener {
                pickImageLauncher.launch("image/*")
            }

            etCategoryNamez.setText(category.categoryName)

            btCancel.setOnClickListener {
                editDialog.dismiss()
            }

            btCategoryUpdate.setOnClickListener {
                if(categoryImageUri != null) {
                    uploadUpdatedImage(categoryImageUri, category, etCategoryNamez.text.toString())
                } else {
                    mainViewModel.updateCategory(category.id, etCategoryNamez.text.toString())
                }
            }


        }

        editDialog.show()
    }

    private fun showDeleteDialog(category: CategoryModel) {
        deleteDialog = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog).create()
        val deleteLayout = DialogDeleteBinding.inflate(layoutInflater)
        deleteDialog.setView(deleteLayout.root)
        deleteDialog.setCanceledOnTouchOutside(true)

        deleteLayout.btCancel.setOnClickListener {
            deleteDialog.dismiss()
        }

        deleteLayout.btDelete.setOnClickListener {
            progress.show()
            mainViewModel.deleteCategory(category.id)
        }

        deleteDialog.show()

    }

}