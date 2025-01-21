package com.neatroots.suddahutpadahadmin.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.neatroots.suddahutpadahadmin.model.EmailModel
import com.neatroots.suddahutpadahadmin.model.UserModel
import com.neatroots.suddahutpadahadmin.utils.Constants
import com.neatroots.suddahutpadahadmin.utils.SharedPref
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthRepository(private val auth: FirebaseAuth, val context: Context) {

    private val fireStore = FirebaseFirestore.getInstance()
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    val authStatus: MutableLiveData<Boolean> = MutableLiveData()
    val loginStatus: MutableLiveData<Boolean> = MutableLiveData()
    val forgetPassStatus: MutableLiveData<Boolean> = MutableLiveData()
    val verificationLink: MutableLiveData<Boolean> = MutableLiveData()
    val emailRegistered: MutableLiveData<Boolean> = MutableLiveData()


    private val _usersList: MutableLiveData<List<UserModel>> = MutableLiveData()
    val usersList: LiveData<List<UserModel>> = _usersList

    init {
        coroutineScope.launch {
            fetchAllUsers()
        }
    }


    suspend fun signUpWithEmail(name: String, phoneNumber: String, email: String, password: String, address: String, location: String, image1: String, image2: String, licenceImage: String, city: String, pinCode: String, verified: Boolean, active: Boolean) {
       try {
           val authResult = auth.createUserWithEmailAndPassword(email, password).await()
           if (authResult.user != null) {
               val userId = authResult.user!!.uid
               registerUser(userId, name, phoneNumber, email, address, location, image1, image2, licenceImage, city, pinCode, verified, active)
           } else {
               authStatus.postValue(false)
           }

       } catch (e: Exception) {
           authStatus.postValue(false)
       }
    }


    private suspend fun fetchAllUsers() {
        try {
            val querySnapshot = fireStore.collection(Constants.USER_REF).get().await()
            val users = mutableListOf<UserModel>()

            for (document in querySnapshot.documents) {
                val user = document.toObject(UserModel::class.java)
                user?.let { users.add(it) }
            }
            _usersList.postValue(users)

        } catch (e: Exception) {
            println("Error fetching users: ${e.message}")
        }
    }


    private suspend fun registerUser(userId: String, name: String, phoneNumber: String, email: String, address: String, location: String, image1: String, image2: String, licenceImage: String, city: String, pinCode: String, verified: Boolean, active: Boolean) {
        try {
            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    return@OnCompleteListener
                }

                val token = task.result
                val userModel = UserModel(
                    userId,
                    name,
                    phoneNumber,
                    email,
                    address,
                    location,
                    city,
                    pinCode,
                    token,
                    verified,
                    active
                )
                val userEmail = EmailModel(userId, email)
                fireStore.collection(Constants.EMAIL_REF).document(userId).set(userEmail)
                fireStore.collection(Constants.USER_REF).document(userId).set(userModel)
                SharedPref.saveUserData(context, userModel)
                authStatus.postValue(true)
            }).await()


        } catch (e: Exception) {
            authStatus.postValue(false)
        }

    }

    suspend fun isEmailAvailable(email: String) {
        try {
            val query = fireStore.collection(Constants.EMAIL_REF).whereEqualTo("email", email)
            val result = query.get().await()
            emailRegistered.postValue(!result.isEmpty)
        } catch (e: Exception) {
            emailRegistered.postValue(false)
        }

    }



    private fun fetchUserData(uid: String) {
        fireStore.collection(Constants.USER_REF).document(uid)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        return@OnCompleteListener
                    }
                    if (documentSnapshot.exists()) {
                        val user = documentSnapshot.toObject(UserModel::class.java)
                        user?.let {
                            user.token = task.result.toString()
                            SharedPref.saveUserData(context, user)
                            loginStatus.postValue(true)
                        }
                    } else {
                        loginStatus.postValue(false)
                    }
                })
            }
            .addOnFailureListener {
                loginStatus.postValue(false)
            }
    }



    suspend fun signInWithEmail(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = Firebase.auth.currentUser!!.uid
                    fetchUserData(userId)
                } else {
                    authStatus.postValue(false)
                }
            }.await()

    }


    fun signOut() {
        SharedPref.clearData(context)
        auth.signOut()
    }


    suspend fun sendPasswordResetEmail(email: String) {
        try {
            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        forgetPassStatus.postValue(true)
                    } else {
                        forgetPassStatus.postValue(false)
                    }
                }.await()
        }catch (e: Exception) {
            forgetPassStatus.postValue(false)
        }
    }

    fun sendVerificationEmail() {
        try {
            auth.currentUser?.sendEmailVerification()
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        verificationLink.postValue(true)
                    } else {
                        verificationLink.postValue(false)
                    }
                }
        } catch (e: Exception) {
            verificationLink.postValue(false)
        }
    }

}