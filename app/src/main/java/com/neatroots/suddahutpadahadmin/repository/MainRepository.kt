package com.neatroots.suddahutpadahadmin.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.neatroots.suddahutpadahadmin.model.CartModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.neatroots.suddahutpadahadmin.model.CategoryModel
import com.neatroots.suddahutpadahadmin.model.OrderModel
import com.neatroots.suddahutpadahadmin.model.ProductModel
import com.neatroots.suddahutpadahadmin.model.UserModel
import com.neatroots.suddahutpadahadmin.utils.Constants
import com.neatroots.suddahutpadahadmin.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class MainRepository {
    private val database = FirebaseDatabase.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val fireStore = FirebaseFirestore.getInstance()

    private val orderRef = database.getReference(Constants.ORDERS_REF)
    private val serviceRef = database.getReference(Constants.SERVICE_REF)
    private val categoryRef = database.getReference(Constants.CATEGORY_REF)
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    val status: MutableLiveData<Boolean> = MutableLiveData()
    val orderStatus: MutableLiveData<Boolean> = MutableLiveData()

    private val _ordersList: MutableLiveData<List<OrderModel>> = MutableLiveData()
    val ordersList: LiveData<List<OrderModel>> = _ordersList

    private val _serviceList = MutableLiveData<List<ProductModel>>()
    val serviceList: LiveData<List<ProductModel>> = _serviceList

    private val _categoryList = MutableLiveData<List<CategoryModel>>()
    val categoryList: LiveData<List<CategoryModel>> = _categoryList

    private val _orderedUsersList: MutableLiveData<List<UserModel>> = MutableLiveData()
    val orderedUserList: LiveData<List<UserModel>> = _orderedUsersList


    init {
        coroutineScope.launch {
            fetchServices()
        }
    }

    init {
        coroutineScope.launch {
            fetchAllOrders()
        }

    }

    init {
        coroutineScope.launch {

        }

    }

    suspend fun createService(image: String, title: String, description: String, offerPrice: String, originalPrice: String, rating: String, category: String, categoryId: String, available: Boolean) {
        val serviceId = serviceRef.push().key ?: return

        val serviceModel = ProductModel(serviceId, image, title, description, offerPrice, originalPrice, category, categoryId, rating, available)

        try {
            serviceRef.child(serviceId).setValue(serviceModel).addOnCompleteListener {task ->
                if (task.isSuccessful) {
                    status.postValue(true)
                } else {
                    status.postValue(false)
                }
            }.await()
        } catch (e: Exception) {
            status.postValue(false)
            println("Error creating service: ${e.message}")
        }

    }


    suspend fun createCategory(image: String, title: String) {
        val categoryId = categoryRef.push().key ?: return

        val categoryModel = CategoryModel(categoryId, image, title)

        try {
            categoryRef.child(categoryId).setValue(categoryModel).addOnCompleteListener {task ->
                if (task.isSuccessful) {
                    status.postValue(true)
                } else {
                    status.postValue(false)
                }
            }.await()
        } catch (e: Exception) {
            status.postValue(false)
            println("Error creating service: ${e.message}")
        }

    }


    suspend fun updateCategory(id: String, title: String) {
        val map = mapOf<String, Any>(
            "categoryName" to title)

        try {
            categoryRef.child(id).updateChildren(map).addOnCompleteListener {task ->
                if (task.isSuccessful) {
                    status.postValue(true)
                } else {
                    status.postValue(false)
                }
            }.await()
        } catch (e: Exception) {
            status.postValue(false)
            println("Error creating service: ${e.message}")
        }

    }

    suspend fun updateCategory(id: String, image: String, title: String) {
        val map = mapOf<String, Any>(
            "image" to image,
            "categoryName" to title)

        try {
            categoryRef.child(id).updateChildren(map).addOnCompleteListener {task ->
                if (task.isSuccessful) {
                    status.postValue(true)
                } else {
                    status.postValue(false)
                }
            }.await()
        } catch (e: Exception) {
            status.postValue(false)
            println("Error creating service: ${e.message}")
        }

    }


    suspend fun deleteCategory(id: String) {
        try {
            categoryRef.child(id).removeValue().addOnCompleteListener {
                if(it.isSuccessful) {
                    status.postValue(true)
                } else {
                    status.postValue(false)
                }
            }.await()

        } catch (e: Exception) {
            status.postValue(false)
            println("Error fetching services: ${e.message}")
        }
    }

    fun fetchCategories() {
        try {
            val categoryList = mutableListOf<CategoryModel>()
            categoryRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()) {
                        categoryList.clear()
                        for (childSnapshot in snapshot.children) {
                            val category = childSnapshot.getValue(CategoryModel::class.java)
                            category?.let { categoryList.add(it) }
                        }
                        _categoryList.postValue(categoryList)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    status.postValue(false)
                }

            })


        } catch (e: Exception) {
            status.postValue(false)
            println("Error fetching services: ${e.message}")
        }
    }

    fun fetchUserOrders(userId: String) {
        try {
            val orderList = mutableListOf<OrderModel>()
            orderRef.child(userId).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()) {
                        orderList.clear()
                        for (childSnapshot in snapshot.children) {
                            val order = childSnapshot.getValue(OrderModel::class.java)
                            order?.let { orderList.add(it) }
                        }
                        _ordersList.postValue(orderList)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    _ordersList.postValue(emptyList())
                }

            })
        } catch (e: Exception) {
            _ordersList.postValue(emptyList())
        }

    }


    suspend fun fetchAllOrders() {
        try {
            val orderedUsers = mutableListOf<UserModel>()
            val orderSnapshot = orderRef.get().await()

            if (orderSnapshot.exists()) {
                val userIds = orderSnapshot.children.map { it.key.toString() }
                for (userId in userIds) {
                    val user = getOrderedUserDetails(userId)
                    orderedUsers.add(user)
                }
                _orderedUsersList.postValue(orderedUsers)
            }
        } catch (e: Exception) {
            _orderedUsersList.postValue(emptyList())

        }

    }

    private suspend fun getOrderedUserDetails(userId: String): UserModel {
        return try {
            val documentSnapshot = fireStore.collection(Constants.USER_REF).document(userId).get().await()
            documentSnapshot.toObject(UserModel::class.java) ?: UserModel()
        } catch (e: Exception) {
            println("Error fetching user details: ${e.message}")
            UserModel()
        }
    }


    private fun fetchServices() {
        try {
            val services = mutableListOf<ProductModel>()
            serviceRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()) {
                        services.clear()
                        for (childSnapshot in snapshot.children) {
                            val service = childSnapshot.getValue(ProductModel::class.java)
                            service?.let { services.add(it) }
                        }
                        _serviceList.postValue(services)
                    } else {
                        _serviceList.postValue(emptyList())
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    status.postValue(false)
                }

            })


        } catch (e: Exception) {
            status.postValue(false)
            println("Error fetching services: ${e.message}")
        }
    }




    suspend fun updateService(id: String, image: String, title: String, description: String, offerPrice: String, originalPrice: String, category: String, categoryId: String, rating: String, available: Boolean) {

        val map = mapOf<String, Any>(
            "image" to image,
            "title" to title,
            "description" to description,
            "offerPrice" to offerPrice,
            "originalPrice" to originalPrice,
            "rating" to rating,
            "category" to category,
            "categoryId" to categoryId,
            "available" to available
        )

        try {
            serviceRef.child(id).updateChildren(map).addOnCompleteListener {task ->
                if (task.isSuccessful) {
                    status.postValue(true)
                } else {
                    status.postValue(false)
                }
            }.await()
        } catch (e: Exception) {
            status.postValue(false)
            println("Error creating service: ${e.message}")
        }

    }


    suspend fun updateOrderStatus(order: OrderModel, orderList: MutableList<CartModel>, updatedStatus: String, index: Int) {
        try {

            val orderRef = orderRef.child(order.userId).child(order.id)
            if (index in orderList.indices) {
                val updatedOrderItems = orderList.toMutableList()
                val updatedItem = updatedOrderItems[index].copy(status = updatedStatus)
                updatedOrderItems[index] = updatedItem

                val updatedOrder = order.copy(orderedItems = updatedOrderItems)
                orderRef.setValue(updatedOrder).await()

                println("Order item updated successfully")
            } else {
                println("Invalid index: $index or order not found")
            }
        } catch (e: Exception) {
            println("Error updating order item: ${e.message}")
        }
    }


    suspend fun clearAndSaveAsHistory(userId: String, order: OrderModel, orderList: MutableList<CartModel>, status: String, index: Int) {
        try {
            val orderRef = orderRef.child(order.userId).child(order.id)
            if (index in orderList.indices) {
                val updatedOrderItems = orderList.toMutableList()
                updatedOrderItems.removeAt(index)

                val updatedOrder = order.copy(orderedItems = updatedOrderItems)
                saveAsHistory(order.orderedItems[index], order, userId, status)
                if(orderList.size <= 1) {
                    orderRef.removeValue().await()
                } else {
                    orderRef.setValue(updatedOrder).await()
                }

            } else {
                println("Invalid index: $index or order not found")
            }
        } catch (e: Exception) {
            println("Error updating order item: ${e.message}")
        }
    }

    private suspend fun saveAsHistory(cartModel: CartModel, order: OrderModel, userId: String, status: String) {
        cartModel.orderDate = order.orderDate
        cartModel.status = status
        cartModel.deliveryDate = Utils.getCurrentDateTime()
        fireStore.collection(Constants.ORDERS_HISTORY_REF).document(userId).collection(Constants.ORDERS_HISTORY_REF).add(cartModel).await()
    }


    suspend fun deleteService(id: String) {
        try {
            serviceRef.child(id).removeValue().addOnCompleteListener {
                if(it.isSuccessful) {
                    status.postValue(true)
                } else {
                    status.postValue(false)
                }
            }.await()

        } catch (e: Exception) {
            status.postValue(false)
            println("Error fetching services: ${e.message}")
        }
    }



}