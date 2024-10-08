package com.example.quizizz_app.activities

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.quizizz_app.R
import com.example.quizizz_app.activities.components.AnimatedCounter
import com.example.quizizz_app.activities.components.AnswerButton
import com.example.quizizz_app.activities.components.DialogConfirmExit
import com.example.quizizz_app.activities.components.InstructUps
import com.example.quizizz_app.activities.components.StepProgressBarItem
import com.example.quizizz_app.activities.components.TopAppBarItem
import com.example.quizizz_app.models.AnswerState
import com.example.quizizz_app.utils.getUserId
import com.example.quizizz_app.viewModels.QuizViewModel
import com.example.quizizz_app.viewModels.ResultStudyViewModel
import java.util.UUID

@Composable
fun QuizScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    quizViewModel: QuizViewModel = viewModel(),
    resultStudyViewModel: ResultStudyViewModel = viewModel()
) {

    val listQuestion by quizViewModel.question.observeAsState(emptyList())
    val isLoading by quizViewModel.isLoading.observeAsState(true)
    val currentQuestionIndex by quizViewModel.currentQuestionIndex.observeAsState(0)
    val selectedChoice by quizViewModel.selectedChoice.observeAsState(null)
    val answerChecked by quizViewModel.answerChecked.observeAsState(false)

    var backPressedOnce by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    val score by quizViewModel.score.observeAsState(0)
    val correctStreak by quizViewModel.correctStreak.observeAsState(0)
    val highestStreak by quizViewModel.highestStreak.observeAsState(0)
    val totalCorrectAnswers by quizViewModel.totalCorrectAnswers.observeAsState(0)

    val context = LocalContext.current
    val userId = getUserId(context)
    if (userId != null) {
        resultStudyViewModel.setUserId(userId)
    } else {
        Log.e("DrawerContent", "User không tồn tại!")
    }
    val imageUrlOld by resultStudyViewModel.imageUrl.observeAsState("")
    val userNameOld by resultStudyViewModel.userName.observeAsState("")
    Log.d("QuizScreen", "QuizScreen: $userNameOld")

    // BackHandler để lắng nghe sự kiện nhấn nút Back
    BackHandler {
        if (backPressedOnce) {
            // Hiển thị Dialog nếu đã nhấn Back lần thứ hai
            showDialog = true
        } else {
            // Đánh dấu là đã nhấn Back lần đầu
            backPressedOnce = true
        }
    }

    // Tắt trạng thái Back lần đầu sau một khoảng thời gian
    LaunchedEffect(backPressedOnce) {
        if (backPressedOnce) {
            kotlinx.coroutines.delay(2000L) // 2 giây để reset trạng thái
            backPressedOnce = false
        }
    }

    val currentQuestion = listQuestion.getOrNull(currentQuestionIndex)
    currentQuestion?.let { question ->
        val correctAnswers = question.choices.filter { it.isCorrect }.joinToString { it.choiceText }
        Log.d("QuizScreen", "TrueQuest: $correctAnswers")
    }

    // Hiển thị dialog xác nhận thoát
    if (showDialog) {
        DialogConfirmExit(
            onDismiss = { showDialog = false },
            onClick = {
                navController.popBackStack() // Quay về màn hình trước
            }
        )
    }

    val stateOfLesson = when {
        answerChecked && listQuestion[currentQuestionIndex].choices.any { it.isCorrect && it.answerState == AnswerState.CORRECT } -> "true"
        answerChecked && listQuestion[currentQuestionIndex].choices.any { it.answerState == AnswerState.INCORRECT } -> "false"
        else -> "default"
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "Đang tải dữ liệu...")
        }
    } else {
        Scaffold(
            containerColor = Color.White,
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBarItem(
                    modifier = modifier.statusBarsPadding(),
                    iconLeft = painterResource(id = R.drawable.button_back),
                    contentLogo = { modifier ->
                        Text(
                            text = "Quizizz",
                            modifier = modifier,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            ),
                            color = Color(0xff111245)
                        )
                    },
                    iconRight = { modifier ->
                        Icon(
                            Icons.Filled.Menu,
                            contentDescription = "",
                            modifier = modifier,
                            tint = Color(0xff353a75),
                        )
                    },
                    backgroundColor = Brush.linearGradient(listOf(Color.White, Color.White)),
                    onClickLeftIcon = {},
                    onClickRightIcon = {},
                    onClick = {}
                )
            },
            bottomBar = {
                val backgroundColor = when {
                    answerChecked && listQuestion[currentQuestionIndex].choices.any { it.isCorrect && it.answerState == AnswerState.CORRECT } -> Color(0xff46D688)
                    answerChecked && listQuestion[currentQuestionIndex].choices.any { it.answerState == AnswerState.INCORRECT } -> Color(0xff9E3332)
                    else -> Color(0xff4453b8)
                }
                Box(
                    modifier = Modifier
                        .navigationBarsPadding()
                        .fillMaxWidth()
                        .background(backgroundColor)
                        .clickable {
                            if (!answerChecked) {
                                quizViewModel.checkAnswer()
                            } else {
                                if (currentQuestionIndex < listQuestion.size - 1) {
                                    quizViewModel.nextQuestion()
                                } else {
                                    val quizId = UUID.randomUUID().toString()
                                    // Nếu đã hoàn thành quiz, điều hướng về màn hình Home
                                    if(userId != null){
                                        quizViewModel.saveQuizResult(quizId, userId, score, highestStreak, totalCorrectAnswers, imageUrlOld, userNameOld)
                                        navController.navigate("ResultScreen/${score}/${highestStreak}/${totalCorrectAnswers}") {
                                            popUpTo("QuizScreen") { inclusive = true }
                                        }
                                    }else{
                                        navController.navigate("ResultScreen/${score}/${highestStreak}/${totalCorrectAnswers}") {
                                            popUpTo("QuizScreen") { inclusive = true }
                                        }
                                    }
                                }
                            }
                        }
                        .padding(17.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = if (!answerChecked) "Kiểm tra ›" else if (currentQuestionIndex < listQuestion.size - 1) "Tiếp theo ›" else "Kết thúc ›",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White
                    )
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                InstructUps(
                    modifier = modifier.padding(top = 10.dp),
                    stateOfLesson = stateOfLesson
                )
                Spacer(modifier = Modifier.padding(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    AnimatedCounter(
                        point = score,
                        imageResId = R.drawable.image14,
                        title = "score"
                    )
                    Spacer(modifier = Modifier.padding(10.dp))
                    AnimatedCounter(
                        point = correctStreak,
                        imageResId = R.drawable.image15,
                        title = "Streak"
                    )
                }
                Spacer(modifier = Modifier.padding(10.dp))
                StepProgressBarItem(
                    currentQuestionIndex = currentQuestionIndex,
                    totalQuestions = listQuestion.size,
                    correctStreak = correctStreak
                )
                Spacer(modifier = Modifier.padding(10.dp))
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp)
                        .height(50.dp), // Cố định chiều cao cho Row
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = listQuestion[currentQuestionIndex].questionText,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontSize = 20.sp
                        ),
                        color = Color(0xff111245),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                }

                Spacer(modifier = Modifier.padding(6.dp))
                Column(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    listQuestion[currentQuestionIndex].choices.forEach { choice ->
                        AnswerButton(
                            answerText = choice.choiceText,
                            onClick = {
                                quizViewModel.selectChoice(choice.choiceID)
                            },
                            isQuizChecked = choice.choiceID == selectedChoice && answerChecked,
                            answerState = choice.answerState
                        )
                    }
                }
            }
        }
    }
}