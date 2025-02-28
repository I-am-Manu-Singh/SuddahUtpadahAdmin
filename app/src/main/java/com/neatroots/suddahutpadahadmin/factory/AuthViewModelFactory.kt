package com.neatroots.suddahutpadahadmin.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.neatroots.suddahutpadahadmin.repository.AuthRepository
import com.neatroots.suddahutpadahadmin.viewmodel.AuthViewModel


class  AuthViewModelFactory(private val userRepository: AuthRepository):ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}