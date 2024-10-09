package com.example.quizizz_app.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizizz_app.models.AnswerState
import com.example.quizizz_app.models.Question
import com.example.quizizz_app.models.QuizResult
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch

class QuizViewModel : ViewModel() {
    private val _questions = MutableLiveData<List<Question>>(emptyList())
    val question: LiveData<List<Question>> = _questions

    private val _isLoading = MutableLiveData<Boolean>(true)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _currentQuestionIndex = MutableLiveData(0)
    val currentQuestionIndex: LiveData<Int> = _currentQuestionIndex

    private val _selectedChoice = MutableLiveData<String?>()
    val selectedChoice: LiveData<String?> = _selectedChoice

    private val _answerChecked = MutableLiveData(false)
    val answerChecked: LiveData<Boolean> = _answerChecked

    private val _score = MutableLiveData(0)
    val score: LiveData<Int> = _score

    private val _correctStreak = MutableLiveData(0)
    val correctStreak: LiveData<Int> = _correctStreak

    // lưu trữ chuỗi cao nhất mà người chơi đạt đuơc
    private val _highestStreak = MutableLiveData(0)
    val highestStreak: LiveData<Int> = _highestStreak

    // lưu trữ tổng số câu trả lời đúng
    private val _totalCorrectAnswers = MutableLiveData(0)
    val totalCorrectAnswers: LiveData<Int> = _totalCorrectAnswers

    private val database = FirebaseDatabase.getInstance()
    private val questionRef = database.getReference("Questions")
    private val quizResultRef = database.getReference("QuizResults")

    init {
        fetchQuestions()
    }

    private fun fetchQuestions() {
        viewModelScope.launch {
            questionRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val questionList = mutableListOf<Question>()
                    for (questionSnapShot in snapshot.children) {
                        val question = questionSnapShot.getValue(Question::class.java)
                        if (question != null) {
                            question.choices = question.choices.shuffled()
                            questionList.add(question)
                        }
                    }
                    _questions.value = questionList.shuffled()
                    _isLoading.value = false
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("QuizViewModel", "Database error: ${error.message}")
                    _isLoading.value = false
                }
            })
        }
    }

    fun selectChoice(choiceID: String) {
        if (_answerChecked.value == true) return // Không cho phép chọn lại nếu đã kiểm tra

        _selectedChoice.value = choiceID
        _questions.value?.get(_currentQuestionIndex.value ?: 0)?.choices?.forEach { choice ->
            choice.answerState = if (choice.choiceID == choiceID) {
                AnswerState.SELECTED
            } else {
                AnswerState.UNSELECTED
            }
        }
    }

    fun checkAnswer() {
        val currentQuestion = _questions.value?.get(_currentQuestionIndex.value ?: 0)
        val selected = _selectedChoice.value

        if (selected == null || _answerChecked.value == true) return

        _answerChecked.value = true

        currentQuestion?.choices?.forEach { choice ->
            choice.answerState = when {
                choice.choiceID == selected && choice.isCorrect -> {
                    _correctStreak.value = _correctStreak.value?.plus(1) // Tăng chuỗi đúng liên tiếp
                    updateScore(_correctStreak.value ?: 0) // Cập nhật điểm dựa trên chuỗi
                    _totalCorrectAnswers.value = _totalCorrectAnswers.value?.plus(1)
                    // Cập nhật chuỗi cao nhất nếu chuỗi hiện tại lớn hơn
                    if ((_correctStreak.value ?: 0) > (_highestStreak.value ?: 0)) {
                        _highestStreak.value = _correctStreak.value
                    }

                    AnswerState.CORRECT
                }
                choice.choiceID == selected -> {
                    if ((_score.value ?: 0) > 0) {
                        _score.value = (_score.value ?: 0) - 1 // Trừ điểm nếu sai nhưng không âm
                    }
                    _correctStreak.value = 0 // Reset chuỗi nếu sai
                    AnswerState.INCORRECT
                }
                choice.isCorrect -> AnswerState.REVEALED_CORRECT
                else -> AnswerState.REVEALED_UNSELECTED
            }
        }
    }

    fun nextQuestion() {
        if (_currentQuestionIndex.value!! < (_questions.value?.size ?: 0) - 1) {
            _currentQuestionIndex.value = _currentQuestionIndex.value?.plus(1)
            _selectedChoice.value = null
            _answerChecked.value = false
        }
    }

    private fun updateScore(streak: Int) {
        when {
            streak in 1..5 -> _score.value = _score.value?.plus(2) // Từ câu 1-20: 2 điểm/câu
            streak in 6..10 -> _score.value = _score.value?.plus(3) // Từ câu 21-50: 3 điểm/câu
            streak > 10 -> _score.value = _score.value?.plus(4) // Từ câu 51 trở lên: 4 điểm/câu
        }
    }

    // Hàm lưu kết quả quiz
    fun saveQuizResult(quizId: String, userId: String, score: Int, highestStreak: Int, totalCorrectAnswers: Int, imageUrl: String?, userName: String) {
        val quizResultId = quizResultRef.push().key ?: return

        val quizResult = QuizResult(
            quizResultId = quizResultId,
            userId = userId,
            userName = userName,
            quizId = quizId,
            score = score,
            highestStreak = highestStreak,
            totalCorrectAnswers = totalCorrectAnswers,
            dateCompleted = System.currentTimeMillis(),
            imageUrl = imageUrl
        )

        quizResultRef.child(quizResultId).setValue(quizResult)
            .addOnSuccessListener {
                Log.d("ResultStudy", "Quiz result saved successfully")
            }
            .addOnFailureListener { exception ->
                Log.e("ResultStudy", "Error saving quiz result", exception)
            }
    }
}
