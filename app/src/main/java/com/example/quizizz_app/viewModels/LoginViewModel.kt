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
            _loginSuccess.value = false
            return
        }

        // Kiểm tra mật khẩu
        if (password.isEmpty()) {
            _errorMessage.value = "Mật khẩu không được để trống"
            _passwordError.value = true
            hasError = true
        } else {
            _passwordError.value = false
        }

        // Nếu có lỗi mật khẩu, return
        if (hasError) {
            _loginSuccess.value = false
            return
        }

        // Tiến hành đăng nhập nếu không có lỗi
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = FirebaseAuth.getInstance().currentUser
                    if (user != null) {
                        // Kiểm tra xem email đã được xác minh hay chưa
                        if (user.isEmailVerified) {
                            val userId = user.uid // Lấy userId từ Firebase
                            saveLoginState(context, true, userId) // Lưu userId vào SharedPreferences
                            _loginSuccess.value = true
                            _errorMessage.value = "Đăng nhập thành công"
                        } else {
                            // Email chưa được xác minh
                            _loginSuccess.value = false
                            _errorMessage.value = "Email chưa được xác minh. Vui lòng kiểm tra hộp thư của bạn."
                            FirebaseAuth.getInstance().signOut() // Đăng xuất người dùng để họ không truy cập được
                        }
                    } else {
                        // Trường hợp người dùng là null (đăng nhập không thành công)
                        _loginSuccess.value = false
                        _errorMessage.value = "Đăng nhập thất bại, vui lòng thử lại"
                    }
                } else {
                    // Đăng nhập thất bại do sai email hoặc mật khẩu
                    _loginSuccess.value = false
                    _errorMessage.value = "Đăng nhập thất bại: sai tài khoản hoặc mật khẩu"
                }
            }
    }


    // Reset lại thông báo lỗi sau khi hiển thị
    fun resetErrorMessage() {
        _errorMessage.value = null
    }

}