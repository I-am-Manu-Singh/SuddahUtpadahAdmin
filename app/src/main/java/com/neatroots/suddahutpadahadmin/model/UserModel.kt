package com.neatroots.suddahutpadahadmin.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel(
    val userId: String = "",
    val name: String = "",
    val phoneNumber: String = "",
    val email: String = "",
    val address: String = "",
    val location: String = "",
    val city: String = "",
    val pinCode: String = "",
    var token: String = "",
    val verified: Boolean = false,
    val active: Boolean = false
): Parcelable