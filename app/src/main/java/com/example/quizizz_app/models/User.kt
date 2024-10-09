package com.example.quizizz_app.models

data class User(
    var userId: String = "",
    val userName: String = "",
    var userEmail: String = "",
    var imgUrl: String = "",
    var maxStreak: Int = 0,
    var score: Int = 0,
    var totalCorrectAnswers: Int = 0
)

data class UserInfo(
    val userName: String?,
    val photoUrl: String?
)
