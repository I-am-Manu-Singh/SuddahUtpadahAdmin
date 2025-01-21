package com.neatroots.suddahutpadahadmin.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class OrderModel(
    val id: String = "",
    val userId: String = "",
    val orderDate: String = "",
    val totalAmount: String = "",
    val orderedItems: @RawValue List<CartModel> = emptyList()
): Parcelable