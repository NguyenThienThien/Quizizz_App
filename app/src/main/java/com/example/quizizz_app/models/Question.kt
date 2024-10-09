package com.example.quizizz_app.models

import com.google.firebase.database.PropertyName

enum class Category {
    TRIVIA, POLITICS, SHOWBIZ, ECONOMY_SOCIETY
}

enum class Difficulty {
    EASY, MEDIUM, HARD
}

data class Question(
    val questionID: String = "",
    val questionText: String = "",
    val category: Category = Category.TRIVIA,
    val difficulty: Difficulty = Difficulty.EASY,
    var choices: List<Choice> = emptyList(),
    val correctAnswer: String = "", //Đáp án đúng
    val imageUrl: String = "",
)

data class Choice(
    val choiceID: String = "",
    val choiceText: String = "", // Nội dung lựa chọn
    @get:PropertyName("isCorrect")
    val isCorrect: Boolean = false, // Đánh dấu đúng/sai
    var answerState: AnswerState = AnswerState.UNSELECTED // Thêm trạng thái của lựa chọn
)

enum class AnswerState {
    UNSELECTED,
    SELECTED,
    CORRECT,
    INCORRECT,
    REVEALED_CORRECT,
    REVEALED_UNSELECTED
}