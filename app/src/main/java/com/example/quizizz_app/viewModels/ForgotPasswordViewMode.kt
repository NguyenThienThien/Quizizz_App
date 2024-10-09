package com.example.quizizz_app.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordViewModel : ViewModel() {

    private val _resetSuccess = MutableLiveData<Boolean>()
    val resetSuccess: LiveData<Boolean> = _resetSuccess

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _emailError = MutableLiveData<Boolean>()
    val emailError: LiveData<Boolean> = _emailError

    fun resetPassword(email: String) {
        var hasError = false

        // Kiểm tra email
        if (email.isEmpty()) {
            _errorMessage.value = "Email không được để trống"
            _emailError.value = true
            hasError = true
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _errorMessage.value = "Email không hợp lệ"
            _emailError.value = true
            hasError = true
        } else {
            _emailError.value = false
        }

        // Nếu có lỗi email, return
        if (hasError) {
            _resetSuccess.value = false
            return
        }

        // Gửi email reset mật khẩu qua Firebase
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _resetSuccess.value = true
                    _errorMessage.value = "Email khôi phục mật khẩu đã được gửi"
                } else {
                    _resetSuccess.value = false
                    _errorMessage.value = "Không thể gửi email khôi phục mật khẩu. Vui lòng thử lại."
                }
            }
    }

    // Reset lại thông báo lỗi sau khi hiển thị
    fun resetErrorMessage() {
        _errorMessage.value = null
    }
}
