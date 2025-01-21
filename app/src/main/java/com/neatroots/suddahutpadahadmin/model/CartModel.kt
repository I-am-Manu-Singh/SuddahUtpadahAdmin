package com.neatroots.suddahutpadahadmin.model

data class CartModel(
    val id: String = "",
    val productId: String = "",
    val productImage: String = "",
    val title: String = "",
    var qty: String = "",
    var price: String = "",
    var originalPrice: String = "",
    var status: String = "",
    var orderDate: String = "",
    var deliveryDate: String = ""
)
