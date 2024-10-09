package com.example.quizizz_app.activities

import android.content.Context
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.quizizz_app.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

@Composable
fun LoginOther(viewModel: LoginViewModel = viewModel()) {
    val context = LocalContext.current
    val launcher = rememberGoogleSignInLauncher(context, viewModel::firebaseAuthWithGoogle)

    Column(
        modifier = Modifier.statusBarsPadding()
    ) {
        Button(onClick = {
            launcher.launch(viewModel.getSignInIntent(context))
        }) {
            Text("Login with Google")
        }
    }
}

@Composable
fun rememberGoogleSignInLauncher(
    context: Context,
    onSignInResult: (String) -> Unit
): ActivityResultLauncher<Intent> {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            account?.idToken?.let {
                onSignInResult(it)
            }
        } catch (e: ApiException) {
            e.printStackTrace() // Handle the error
        }
    }
    return launcher
}

// ViewModel logic to handle Google SignIn
class LoginViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()

    fun getSignInIntent(context: Context): Intent {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        val googleSignInClient = GoogleSignIn.getClient(context, gso)
        return googleSignInClient.signInIntent
    }

    fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Successfully signed in
                    val user = auth.currentUser
                } else {
                    // If sign in fails, display a message to the user.
                }
            }
    }
}
