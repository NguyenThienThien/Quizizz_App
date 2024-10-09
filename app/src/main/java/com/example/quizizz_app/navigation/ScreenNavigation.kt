package com.example.quizizz_app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.quizizz_app.activities.ChangeInformation
import com.example.quizizz_app.activities.HomeScreen
import com.example.quizizz_app.activities.LoginScreen
import com.example.quizizz_app.activities.QuizScreen
import com.example.quizizz_app.activities.RankScreen
import com.example.quizizz_app.activities.ResultStudyScreen
import com.example.quizizz_app.activities.SignUpScreen
import com.example.quizizz_app.activities.WelcomeScreen

@Composable
fun ScreenNavigation(){
    val navController = rememberNavController()
    val context = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = "WelcomeScreen"
    ){
        composable(ScreenName.WelcomeScreen.route){ WelcomeScreen(navController) }
        composable(ScreenName.LoginScreen.route){ LoginScreen(navController) }
        composable(ScreenName.SignUpScreen.route){ SignUpScreen(navController) }
        composable(ScreenName.HomeScreen.route){ HomeScreen(navController = navController) }
        composable(ScreenName.QuizScreen.route){ QuizScreen(navController = navController) }

        composable(
            route = "ResultScreen/{score}/{highestStreak}/{totalCorrectAnswers}",
            arguments = listOf(
                navArgument("score") { type = NavType.IntType },
                navArgument("highestStreak") { type = NavType.IntType },
                navArgument("totalCorrectAnswers") { type = NavType.IntType }
            )
        ){ backStackEntry ->
            val score = backStackEntry.arguments?.getInt("score") ?: 0
            val highestStreak = backStackEntry.arguments?.getInt("highestStreak") ?: 0
            val totalCorrectAnswers = backStackEntry.arguments?.getInt("totalCorrectAnswers")?: 0

            ResultStudyScreen(navController = navController, score = score, highestStreak = highestStreak, totalCorrectAnswers = totalCorrectAnswers)
        }

        composable(ScreenName.RankScreen.route){ RankScreen(navController = navController) }
        composable(ScreenName.ChangeInformationScreen.route){ ChangeInformation(navController = navController) }
    }
}