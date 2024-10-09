package com.example.quizizz_app.viewModels

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChangeInformationViewModel : ViewModel() {

    private val _updateSuccess = MutableLiveData<Boolean>()
    val updateSuccess: LiveData<Boolean> = _updateSuccess

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val userId = FirebaseAuth.getInstance().currentUser?.uid
    private val database = FirebaseDatabase.getInstance().getReference("Users")
    private val storage = FirebaseStorage.getInstance().getReference("Profile_pictures")

    fun updateUserInformation(name: String, imageUri: Uri?, isImageUpdated: Boolean) {
        if (name.isEmpty()) {
            _errorMessage.value = "Tên không được để trống"
            return
        }

        if (isImageUpdated && imageUri != null) {
            val currentDate = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val imageRef = storage.child("${userId}_${currentDate}.jpg")
            imageRef.putFile(imageUri)
                .addOnSuccessListener {
                    imageRef.downloadUrl.addOnSuccessListener { uri ->
                        val imageUrl = uri.toString()
                        saveUserInfo(name, imageUrl)
                    }
                }
                .addOnFailureListener {
                    _errorMessage.value = "Tải ảnh lên thất bại"
                }
        } else {
            saveUserInfo(name, null)
        }
    }

    private fun saveUserInfo(name: String, imageUrl: String?) {
        val updates = mutableMapOf<String, Any?>()
        updates["userName"] = name
        if (imageUrl != null) {
            updates["imgUrl"] = imageUrl
        }
        userId?.let {
            database.child(it).updateChildren(updates)
                .addOnSuccessListener {
                    _updateSuccess.value = true
                }
                .addOnFailureListener {
                    _errorMessage.value = "Cập nhật thất bại"
                }
        }
    }

    fun resetErrorMessage() {
        _errorMessage.value = null
    }
}