package com.example.quizizz_app.utils

import android.content.Context
import android.content.SharedPreferences

fun saveLoginState(context: Context, isLoggedIn: Boolean, userId: String?) {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
    val editor: SharedPreferences.Editor = sharedPreferences.edit()

    // Lưu trạng thái đăng nhập
    editor.putBoolean("isLoggedIn", isLoggedIn)

    // Nếu đăng nhập thành công, lưu userId
    if (isLoggedIn && userId != null) {
        editor.putString("userId", userId)
    } else {
        editor.remove("userId") // Xóa userId khi đăng xuất
    }

    editor.apply()
}

fun isLoggedIn(context: Context): Boolean {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
    return sharedPreferences.getBoolean("isLoggedIn", false)
}

fun saveTrialStartTime(context: Context) {
    val sharedPreferences = context.getSharedPreferences("TrialPrefs", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()

    // Lưu thời gian hiện tại (thời điểm bắt đầu dùng thử)
    val currentTime = System.currentTimeMillis()
    editor.putLong("trialStartTime", currentTime)

    // Khởi tạo trạng thái là chưa hết hạn (false)
    editor.putBoolean("isTrialExpired", false)
    editor.apply()
}

fun checkAndSaveTrialExpired(context: Context): Boolean {
    val sharedPreferences = context.getSharedPreferences("TrialPrefs", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()

    // Kiểm tra nếu trạng thái hết hạn đã lưu là true
    val isTrialExpired = sharedPreferences.getBoolean("isTrialExpired", false)

    // Nếu đã hết hạn, trả về true và không cần kiểm tra thời gian nữa
    if (isTrialExpired) {
        return true
    }

    // Lấy thời gian bắt đầu dùng thử từ SharedPreferences
    val trialStartTime = sharedPreferences.getLong("trialStartTime", 0L)

    // Nếu chưa lưu thời gian bắt đầu, cho phép dùng thử và lưu lại thời gian hiện tại
    if (trialStartTime == 0L) {
        saveTrialStartTime(context) // Lưu thời gian bắt đầu
        return false
    }

    // Kiểm tra nếu đã quá 1 phút kể từ khi bắt đầu dùng thử
    val currentTime = System.currentTimeMillis()
    val timeElapsed = currentTime - trialStartTime
    val oneMinuteInMillis = 60 * 1000

    val isExpired = timeElapsed > oneMinuteInMillis

    // Nếu đã hết hạn, lưu lại trạng thái là true
    if (isExpired) {
        editor.putBoolean("isTrialExpired", true)
        editor.apply()
    }

    return isExpired
}

fun getUserId(context: Context): String? {
    val sharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
    return sharedPreferences.getString("userId", null) // Trả về userId hoặc null nếu không tồn tại
}




