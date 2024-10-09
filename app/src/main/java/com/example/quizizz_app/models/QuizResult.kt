package com.example.quizizz_app.models

data class QuizResult(
    val quizResultId: String ?= "",
    val userId: String ?= "",
    var userName: String ?= "",
    val quizId: String?= "",
    val score: Int ?= 0,
    val highestStreak: Int ?= 0,
    val totalCorrectAnswers: Int ?= 0,
    val dateCompleted: Long ?= 0,
    var imageUrl: String? = null
)

