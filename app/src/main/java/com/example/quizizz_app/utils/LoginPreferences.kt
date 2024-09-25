package com.example.quizizz_app.utils

import android.content.Context
import android.content.SharedPreferences

fun saveLoginState(context: Context, isLoggedIn: Boolean) {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
    val editor: SharedPreferences.Editor = sharedPreferences.edit()
    editor.putBoolean("isLoggedIn", isLoggedIn)
    editor.apply()
}


fun isLoggedIn(context: Context): Boolean {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
    return sharedPreferences.getBoolean("isLoggedIn", false)
}
