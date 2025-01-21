package com.neatroots.suddahutpadahadmin.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neatroots.suddahutpadahadmin.model.CartModel
import com.neatroots.suddahutpadahadmin.model.CategoryModel
import com.neatroots.suddahutpadahadmin.model.OrderModel
import com.neatroots.suddahutpadahadmin.model.ProductModel
import com.neatroots.suddahutpadahadmin.model.UserModel
import com.neatroots.suddahutpadahadmin.repository.MainRepository
import kotlinx.coroutines.launch

class MainViewModel(private val mainRepository: MainRepository): ViewModel() {

    val status: LiveData<Boolean> = mainRepository.status
    val servicesList: LiveData<List<ProductModel>> = mainRepository.serviceList
    val orderedUsersList: LiveData<List<UserModel>> = mainRepository.orderedUserList
    val ordersList: LiveData<List<OrderModel>> = mainRepository.ordersList
    val categoryList: LiveData<List<CategoryModel>> = mainRepository.categoryList

    fun createService(image: String, title: String, description: String, offerPrice: String, originalPrice: String, rating: String, category: String, categoryId: String, available: Boolean) {
        viewModelScope.launch {
            try {
                mainRepository.createService(image, title, description, offerPrice, originalPrice, rating, category, categoryId,  available)
            } catch (e: Exception) {
                mainRepository.status.postValue(false)
            }
        }
    }

    fun fetchCategories() {
        viewModelScope.launch {
            try {
                mainRepository.fetchCategories()
            } catch (e: Exception) {
                mainRepository.status.postValue(false)
            }
        }
    }

    fun deleteService(id: String) {
        viewModelScope.launch {
            try {
                mainRepository.deleteService(id)
            }  catch (e: Exception) {
                mainRepository.status.postValue(false)
            }
        }
    }

    fun deleteCategory(id: String) {
        viewModelScope.launch {
            try {
                mainRepository.deleteCategory(id)
            }  catch (e: Exception) {
                mainRepository.status.postValue(false)
            }
        }
    }

    fun updateService(id: String, image: String, title: String, description: String, offerPrice: String, originalPrice: String, category: String, categoryId: String, rating: String, available: Boolean) {
        viewModelScope.launch {
            try {
                mainRepository.updateService(id, image, title, description, offerPrice, originalPrice, category, categoryId, rating, available)
            }  catch (e: Exception) {
                mainRepository.status.postValue(false)
            }
        }
    }

    fun updateCategory(id: String, title: String) {
        viewModelScope.launch {
            try {
                mainRepository.updateCategory(id, title)
            }  catch (e: Exception) {
                mainRepository.status.postValue(false)
            }
        }
    }

    fun updateCategory(id: String, image: String, title: String) {
        viewModelScope.launch {
            try {
                mainRepository.updateCategory(id, image, title)
            }  catch (e: Exception) {
                mainRepository.status.postValue(false)
            }
        }
    }

    fun createCategory(image: String, title: String) {
        viewModelScope.launch {
            try {
                mainRepository.createCategory(image, title)
            }  catch (e: Exception) {
                mainRepository.status.postValue(false)
            }
        }
    }

    fun fetchAllOrderedUsers() {
        viewModelScope.launch {
            try {
                mainRepository.fetchAllOrders()
            }  catch (e: Exception) {
                print(e.message)
            }
        }
    }




    fun updateOrderStatus(order: OrderModel, orderList: MutableList<CartModel>, updatedStatus: String, index: Int) {
        viewModelScope.launch {
            try {
                mainRepository.updateOrderStatus(order, orderList, updatedStatus, index)
            }  catch (e: Exception) {
                mainRepository.status.postValue(false)
            }
        }
    }

    fun  clearAndSaveAsHistory(userId: String, order: OrderModel, orderList: MutableList<CartModel>, status: String, index: Int)  {
        viewModelScope.launch {
            try {
                mainRepository.clearAndSaveAsHistory(userId, order, orderList, status, index)
            }  catch (e: Exception) {
                mainRepository.status.postValue(false)
            }
        }
    }


    fun fetchUserOrders(userId: String) {
        try {
            mainRepository.fetchUserOrders(userId)
        }  catch (e: Exception) {
            mainRepository.status.postValue(false)
        }
    }

}