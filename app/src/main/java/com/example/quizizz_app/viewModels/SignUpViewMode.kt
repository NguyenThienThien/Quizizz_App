package com.example.quizizz_app.viewModels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class SignUpViewModel : ViewModel() {

    private val _signUpSuccess = MutableLiveData<Boolean>()
    val signUpSuccess: LiveData<Boolean> = _signUpSuccess

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _emailError = MutableLiveData<Boolean>()
    val emailError: LiveData<Boolean> = _emailError

    private val _passwordError = MutableLiveData<Boolean>()
    val passwordError: LiveData<Boolean> = _passwordError

    private val _rePasswordError = MutableLiveData<Boolean>()
    val rePasswordError: LiveData<Boolean> = _rePasswordError

    fun signUp(context: Context, email: String, password: String, rePassword: String) {
        var errorMessage: String? = null
        var hasError = false

        // Kiểm tra email
        if (email.isEmpty()) {
            errorMessage = "Email không được để trống"
            _emailError.value = true
            hasError = true
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            errorMessage = "Email không hợp lệ"
            _emailError.value = true
            hasError = true
        } else {
            _emailError.value = false
        }

        // Kiểm tra mật khẩu
        if (password.isEmpty()) {
            if (errorMessage == null) errorMessage = "Mật khẩu không được để trống"
            _passwordError.value = true
            hasError = true
        } else if (password.length < 8) {
            if (errorMessage == null) errorMessage = "Mật khẩu phải có ít nhất 8 ký tự"
            _passwordError.value = true
            hasError = true
        } else {
            _passwordError.value = false
        }

        // Kiểm tra mật khẩu nhập lại
        if (rePassword.isEmpty()) {
            if (errorMessage == null) errorMessage = "Vui lòng nhập lại mật khẩu"
            _rePasswordError.value = true
            hasError = true
        } else if (password != rePassword) {
            if (errorMessage == null) errorMessage = "Mật khẩu nhập lại không trùng khớp"
            _rePasswordError.value = true
            hasError = true
        } else {
            _rePasswordError.value = false
        }

        // Nếu có lỗi, cập nhật thông báo lỗi và dừng quá trình
        if (hasError) {
            _signUpSuccess.value = false
            _errorMessage.value = errorMessage
            return
        }

        // Tiến hành đăng ký nếu không có lỗi
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = FirebaseAuth.getInstance().currentUser
                    user?.sendEmailVerification()?.addOnCompleteListener { verificationTask ->
                        if (verificationTask.isSuccessful) {
                            // Gửi email xác thực thành công
                            _signUpSuccess.value = true
                            _errorMessage.value = "Vui lòng kiểm tra email để xác minh tài khoản"
                        } else {
                            // Gửi email xác thực thất bại
                            _signUpSuccess.value = false
                            _errorMessage.value =
                                "Không thể gửi email xác minh: ${verificationTask.exception?.message}"
                        }
                    }
                } else {
                    // Đăng ký thất bại
                    _signUpSuccess.value = false
                    _errorMessage.value = "Đăng ký thất bại: ${task.exception?.message}"
                }
            }
    }

    // Reset lại thông báo lỗi sau khi hiển thị
    fun resetErrorMessage() {
        _errorMessage.value = null
    }
}
