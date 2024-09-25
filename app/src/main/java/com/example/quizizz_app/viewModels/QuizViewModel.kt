package com.example.quizizz_app.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizizz_app.models.AnswerState
import com.example.quizizz_app.models.Question
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

    private val database = FirebaseDatabase.getInstance()
    private val questionRef = database.getReference("Questions")

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
                choice.choiceID == selected && choice.isCorrect -> AnswerState.CORRECT
                choice.choiceID == selected -> AnswerState.INCORRECT
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
}
