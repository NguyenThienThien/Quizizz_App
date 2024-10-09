package com.example.quizizz_app.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.quizizz_app.models.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ResultStudyViewModel : ViewModel() {
    private val _score = MutableLiveData("")
    val score: LiveData<String> = _score

    private val _scoreStreak = MutableLiveData("")
    val scoreStreak: LiveData<String> = _scoreStreak

    private val _imageUrl = MutableLiveData("")
    val imageUrl: LiveData<String> = _imageUrl

    private val _userName = MutableLiveData("")
    val userName: LiveData<String> = _userName

    private val _email = MutableLiveData("")
    val email: LiveData<String> = _email

    private var userId: String? = null

    private val database = FirebaseDatabase.getInstance()
    private val userRef = database.getReference("Users")

    fun setUserId(id: String) {
        userId = id
        fetchUsers() // Gọi hàm fetchUsers khi có userId
    }

    private fun fetchUsers() {
        userId?.let {
            userRef.child(it).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        // Lấy thông tin người dùng từ snapshot
                        val scoreValue = snapshot.child("score").getValue(Int::class.java) ?: ""
                        val streakValue = snapshot.child("maxStreak").getValue(Int::class.java) ?: ""
                        val imageValue = snapshot.child("imgUrl").getValue(String::class.java) ?: ""
                        val userNameValue = snapshot.child("userName").getValue(String::class.java) ?: ""
                        val emailValue = snapshot.child("userEmail").getValue(String::class.java) ?: ""

                        // Cập nhật LiveData
                        _score.value = scoreValue.toString()
                        _scoreStreak.value = streakValue.toString()
                        _imageUrl.value = imageValue
                        _userName.value = userNameValue
                        _email.value = emailValue

                        Log.d("QuizScreens", "onDataChange: $userName")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("ResultStudy", "Database error: ${error.message}")
                }
            })
        }
    }

    fun updateScoreAndStreak(newScore: Int, newStreak: Int, newTotalCorrectAnswers: Int,  userId: String) {
        val userUpdates = mutableMapOf<String, Any?>()

        // Chỉ cập nhật giá trị score nếu score mới lớn hơn score cũ
        userRef.child(userId).get().addOnSuccessListener { dataSnapshot ->
            val user = dataSnapshot.getValue(User::class.java)
            if (user != null) {
                if (newScore > user.score) {
                    userUpdates["score"] = newScore
                }
                if (newStreak > user.maxStreak) {
                    userUpdates["maxStreak"] = newStreak
                }
                if(newTotalCorrectAnswers > user.totalCorrectAnswers){
                    userUpdates["totalCorrectAnswers"] = newTotalCorrectAnswers
                }

                if (userUpdates.isNotEmpty()) {
                    userRef.child(userId).updateChildren(userUpdates)
                        .addOnSuccessListener {
                            Log.d("QuizViewModel", "User data updated successfully")
                        }
                        .addOnFailureListener { exception ->
                            Log.e("QuizViewModel", "Error updating user data", exception)
                        }
                } else {
                    Log.d("QuizViewModel", "No changes to update")
                }
            }
        }.addOnFailureListener { exception ->
            Log.e("QuizViewModel", "Error getting user data", exception)
        }
    }

}