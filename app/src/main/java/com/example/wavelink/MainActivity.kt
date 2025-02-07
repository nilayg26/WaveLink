package com.example.wavelink

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.wavelink.ui.theme.WaveLinkTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WaveLinkTheme {
                Navigation()
            }
        }
    }
}
@Composable
fun Navigation(){
    val navController= rememberNavController()
    NavHost(navController=navController, startDestination = LogInPage.route){
        composable(SignInPage.route) {

        }
        composable(HomePage.route) {

        }
        composable(AccountPage.route) {

        }
        composable(LogInPage.route) {

        }
        composable(InteractivePage.route) {

        }
    }
}
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WaveLinkTheme {
        Greeting("Android")
    }
}