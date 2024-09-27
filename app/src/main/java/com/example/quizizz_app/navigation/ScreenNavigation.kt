package com.example.quizizz_app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.quizizz_app.activities.HomeScreen
import com.example.quizizz_app.activities.LoginScreen
import com.example.quizizz_app.activities.QuizScreen
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
    }
}