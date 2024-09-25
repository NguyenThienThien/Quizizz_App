package com.example.quizizz_app.viewModels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.quizizz_app.utils.saveLoginState
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel : ViewModel() {

    private val _loginSuccess = MutableLiveData<Boolean>()
    val loginSuccess: LiveData<Boolean> = _loginSuccess

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _emailError = MutableLiveData<Boolean>()
    val emailError: LiveData<Boolean> = _emailError

    private val _passwordError = MutableLiveData<Boolean>()
    val passwordError: LiveData<Boolean> = _passwordError

    fun login(context: Context, email: String, password: String) {
        var hasError = false

        // Kiểm tra email trước
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

        // Nếu đã có lỗi về email, return luôn và không kiểm tra tiếp mật khẩu
        if (hasError) {
            _loginSuccess.value = false
            return
        }

        // Nếu email không có lỗi, tiếp tục kiểm tra mật khẩu
        if (password.isEmpty()) {
            _errorMessage.value = "Mật khẩu không được để trống"
            _passwordError.value = true
            hasError = true
        } else {
            _passwordError.value = false
        }

        // Nếu mật khẩu có lỗi, return
        if (hasError) {
            _loginSuccess.value = false
            return
        }

        // Tiến hành đăng nhập nếu không có lỗi
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    saveLoginState(context, true)
                    _loginSuccess.value = true
                    _errorMessage.value = "Đăng nhập thành công"
                } else {
                    _loginSuccess.value = false
                    _errorMessage.value = "Đăng nhập thất bại sai tài khoản hoặc mật khẩu"
                }
            }
    }

    // Reset lại thông báo lỗi sau khi hiển thị
    fun resetErrorMessage() {
        _errorMessage.value = null
    }

}