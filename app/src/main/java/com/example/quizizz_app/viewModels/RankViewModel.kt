package com.example.quizizz_app.viewModels

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.quizizz_app.models.QuizResult
import com.example.quizizz_app.models.UserInfo
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Calendar
import java.util.concurrent.CountDownLatch

class RankViewModel : ViewModel() {
    private val _rankedUsers = MutableLiveData<List<QuizResult>>()
    val rankedUsers: LiveData<List<QuizResult>> = _rankedUsers

    private val database = FirebaseDatabase.getInstance()
    private val quizResultRef = database.getReference("QuizResults")

    val userNameMap = MutableLiveData<Map<String, String>>()

    fun fetchRankedUsers(typeRank: String, chooseRank: String) {
        val currentTime = System.currentTimeMillis()
        val calendar = Calendar.getInstance()
        val timeRange = when (typeRank) {
            "Tổng" -> null
            "Ngày" -> currentTime - 24 * 60 * 60 * 1000
            "Tuần" -> currentTime - 7 * 24 * 60 * 60 * 1000
            "Tháng" -> {
                calendar.set(Calendar.DAY_OF_MONTH, 1) // Về đầu tháng
                calendar.timeInMillis // Lấy thời gian bắt đầu tháng
            }
            "Nửa năm" -> {
                calendar.add(Calendar.MONTH, -6) // Trừ 6 tháng
                calendar.timeInMillis // Lấy thời gian 6 tháng trước
            }
            "1 năm" -> currentTime - 365 * 24 * 60 * 60 * 1000
            else -> null
        }

        val orderByField = when (chooseRank) {
            "Điểm cao nhất" -> "score"
            "Chuỗi trả lời" -> "maxStreak"
            "Tổng câu đúng" -> "totalCorrectAnswers"
            else -> "score"
        }

        val query = quizResultRef.orderByChild(orderByField)

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val bestResultsMap = mutableMapOf<String, QuizResult>()

                for (snapshot in dataSnapshot.children) {
                    val quizResult = snapshot.getValue(QuizResult::class.java)
                    if (quizResult != null) {
                        Log.d("RankViewModel", "Date Completed: ${quizResult.dateCompleted}")
                        // Manually filter users based on the time range
                        if (timeRange == null || quizResult.dateCompleted!! >= timeRange) {
                            val existingResult = bestResultsMap[quizResult.userId]
                            if (existingResult == null || isBetterResult(quizResult, existingResult, chooseRank)) {
                                Log.d("RankViewModel", "Updating user ${quizResult.userId}: Score ${quizResult.score}, Existing Score ${existingResult?.score}")
                                bestResultsMap[quizResult.userId!!] = quizResult
                            } else {
                                Log.d("RankViewModel", "User ${quizResult.userId} not updated: Score ${quizResult.score}, Existing Score ${existingResult.score}")
                            }
                        }
                    }
                }

                // Convert the map values to a list
                val bestResultsList = bestResultsMap.values.toList()

                val userIds = bestResultsList.map { it.userId!! }
                fetchUserNames(userIds) { userMap ->
                    // Cập nhật userNameMap sau khi đã lấy tên và ảnh người dùng
                    userNameMap.postValue(userMap.mapValues { it.value.userName ?: "Unknown User" })

                    // Cập nhật rankedUsers sau khi đã có tên và ảnh người dùng
                    val updatedResults = bestResultsList.map { result ->
                        result.apply {
                            userName = userMap[result.userId]?.userName ?: "Unknown User"
                            imageUrl = userMap[result.userId]?.photoUrl // Assigning photo URL
                        }
                    }.sortedByDescending { user ->
                        when (chooseRank) {
                            "Điểm cao nhất" -> user.score ?: 0
                            "Chuỗi trả lời" -> user.highestStreak ?: 0
                            "Tổng câu đúng" -> user.totalCorrectAnswers ?: 0
                            else -> user.score ?: 0
                        }
                    }

                    _rankedUsers.postValue(updatedResults)
                }


                Log.d("RankViewModel", "Type Rank: $typeRank, Choose Rank: $chooseRank")
                Log.d("RankViewModel", "Fetched ${bestResultsList.size} results after filtering.")
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("RankViewModel", "Failed to load ranked users: ${databaseError.message}")
            }
        })
    }

    // Helper function to compare quiz results
    private fun isBetterResult(newResult: QuizResult, existingResult: QuizResult, chooseRank: String): Boolean {
        return when (chooseRank) {
            "Điểm cao nhất" -> (newResult.score ?: 0) > (existingResult.score ?: 0)
            "Chuỗi trả lời" -> (newResult.highestStreak ?: 0) > (existingResult.highestStreak ?: 0)
            "Tổng câu đúng" -> (newResult.totalCorrectAnswers ?: 0) > (existingResult.totalCorrectAnswers ?: 0)
            else -> (newResult.score ?: 0) > (existingResult.score ?: 0)
        }
    }

    fun fetchUserNames(userIds: List<String>, onComplete: (Map<String, UserInfo>) -> Unit) {
        val userMap = mutableMapOf<String, UserInfo>()
        val userRef = database.getReference("Users")

        val countDownLatch = CountDownLatch(userIds.size)

        for (userId in userIds) {
            userRef.child(userId).get().addOnSuccessListener { snapshot ->
                val userName = snapshot.child("userName").getValue(String::class.java)
                val photoUrl = snapshot.child("imgUrl").getValue(String::class.java)
                if (userName != null) {
                    userMap[userId] = UserInfo(userName, photoUrl)
                }
                countDownLatch.countDown()
            }.addOnFailureListener {
                countDownLatch.countDown()
            }
        }

        // Wait until all user data is fetched
        Thread {
            countDownLatch.await()
            // Update LiveData on the main thread
            Handler(Looper.getMainLooper()).post {
                onComplete(userMap)
            }
        }.start()
    }
}