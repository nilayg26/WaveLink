package com.example.wavelink

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.wavelink.Pages.AccountPage
import com.example.wavelink.Pages.ForgotPage
import com.example.wavelink.Pages.HomePage
import com.example.wavelink.Pages.InteractivePage
import com.example.wavelink.Pages.InteractivePage2
import com.example.wavelink.Pages.LogInPage
import com.example.wavelink.Pages.SignInPage
import com.example.wavelink.ViewModels.BluetoothViewModel
import com.example.wavelink.ViewModels.FirebaseViewModel
import com.example.wavelink.ViewModels.GeminiViewModel
import com.example.wavelink.ui.theme.WaveLinkTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val firebaseViewModel:FirebaseViewModel by viewModels()
        val geminiViewModel:GeminiViewModel by viewModels()
        val bluetoothViewModel:BluetoothViewModel by viewModels()
        val sharedPreferences:SharedPreferences=this.getSharedPreferences("WaveLink", Context.MODE_PRIVATE)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WaveLinkTheme {
                Navigation(firebaseViewModel,geminiViewModel,bluetoothViewModel,sharedPreferences)
            }
        }
    }
}
@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun Navigation(
    firebaseViewModel: FirebaseViewModel,
    geminiViewModel: GeminiViewModel,
    bluetoothViewModel: BluetoothViewModel,
    sharedPreferences: SharedPreferences
) {
    var page by remember {
        mutableStateOf(HomePage.route)
    }
    val navController= rememberNavController()
    val loginStatus= sharedPreferences.getBoolean("loginStatus",false)
    NavHost(navController=navController, startDestination = if(loginStatus){HomePage.route} else{LogInPage.route}){
        composable(SignInPage.route) {
            SignInPage(firebaseViewModel,sharedPreferences,navController)
        }
        composable(HomePage.route) {
            HomePage(bluetoothViewModel,sharedPreferences,navController,page){
                page=HomePage.route
            }
        }
        composable(AccountPage.route) {
            AccountPage(firebaseViewModel,sharedPreferences,navController,page){
                page=AccountPage.route
            }
        }
        composable(LogInPage.route) {
            LogInPage(firebaseViewModel,sharedPreferences,navController)
        }
        composable(InteractivePage.route, exitTransition = { slideOutHorizontally() }) {
            InteractivePage(bluetoothViewModel,geminiViewModel,firebaseViewModel,navController)
        }
        composable(InteractivePage2.route, exitTransition = { slideOutHorizontally() }) {
            InteractivePage2(bluetoothViewModel,geminiViewModel,firebaseViewModel,navController)
        }
        composable(ForgotPage.route) {
            ForgotPage(firebaseViewModel,sharedPreferences,navController)
        }
    }
}
