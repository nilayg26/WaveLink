package com.example.wavelink.Pages

import android.annotation.SuppressLint
import android.content.SharedPreferences
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.wavelink.LogInPage
import com.example.wavelink.R
import com.example.wavelink.TextFieldWL
import com.example.wavelink.ViewModels.FirebaseViewModel
import com.example.wavelink.ViewModels.Unauthenticated
import com.example.wavelink.ui.theme.MutedGrayText
import com.example.wavelink.ui.theme.SoftPurple
import com.example.wavelink.ui.theme.WaveLinkTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AccountPage(
    firebaseViewModel: FirebaseViewModel,
    sharedPreferences: SharedPreferences,
    navController: NavHostController,
    page: String,
    function: () -> Unit
) {
    val context= LocalContext.current
    val verticalScroll= rememberScrollState()
    val name by remember {
        mutableStateOf(sharedPreferences.getString("name",""))
    }
    val email by remember {
        mutableStateOf(sharedPreferences.getString("email",""))
    }
    val authState=firebaseViewModel.authState.observeAsState()
    LaunchedEffect(authState.value) {
        when(authState.value){
        Unauthenticated->{navController.navigate(LogInPage.route){
            popUpTo(0){
                inclusive=true
            }
            launchSingleTop=true
        }}
        }
    }
    WaveLinkTheme {
        Scaffold(bottomBar = {BottomMenu(navController, page, function) }) {
            Column(modifier = Modifier.fillMaxSize().verticalScroll(state = verticalScroll).animateContentSize()) {
                Row(
                    modifier = Modifier.fillMaxWidth().paint(
                        painter = painterResource(R.drawable.app_logo),
                        contentScale = ContentScale.Crop
                    ),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        "Account Page ",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(5.dp)
                    )
                }
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Wave Link",
                        color = MutedGrayText,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 5.dp)
                    )
                    Spacer(Modifier.height(10.dp))
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Spacer(modifier = Modifier.height(10.dp))
                        TextFieldWL(text = name.toString(), password = false, label = "Name") {
                            it
                        }
                        TextFieldWL(text = email.toString(), password = false, label = "Email") {
                            it
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Button(onClick = {
                            firebaseViewModel.logOut(context = context,sharedPreferences=sharedPreferences)
                        }) {
                            Text(text = "Log Out", fontSize = 18.sp)
                        }
                    }
                }
                Spacer(Modifier.height(20.dp))
                Row() {
                    Box(modifier = Modifier.fillMaxWidth().padding(start = 10.dp).animateContentSize(),){
                        Text("View History...", color = SoftPurple, modifier = Modifier.clickable {  })

                    }
                }
            }
        }
    }
}
//@Preview(showBackground = true)
//@Composable
//fun AccountPagePr(){
//    AccountPage(firebaseViewModel, sharedPreferences, navController)
//}