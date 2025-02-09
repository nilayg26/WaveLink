package com.example.wavelink.Pages
import android.content.SharedPreferences
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.wavelink.ForgotPage
import com.example.wavelink.HomePage
import com.example.wavelink.LoadingScreenLL
import com.example.wavelink.LogInPage
import com.example.wavelink.R
import com.example.wavelink.SignInPage
import com.example.wavelink.TextFieldWL
import com.example.wavelink.ViewModels.Authenticated
import com.example.wavelink.ViewModels.FirebaseViewModel
import com.example.wavelink.ViewModels.LoadingFirebase
import com.example.wavelink.check
import com.example.wavelink.ui.theme.MutedGrayText
import com.example.wavelink.ui.theme.SoftPurple
import com.example.wavelink.ui.theme.WaveLinkTheme
import kotlinx.coroutines.launch

@Composable
fun LogInPage(
    firebaseViewModel: FirebaseViewModel,
    sharedPreferences: SharedPreferences,
    navController: NavHostController
) {
    val verticalScroll= rememberScrollState()
    val context= LocalContext.current
    val coroutineScope= rememberCoroutineScope()
    val info = remember {
        mutableStateListOf("","")
    }
    var isLoading by remember {
        mutableStateOf(false)
    }
    val authState=firebaseViewModel.authState.observeAsState()
    LaunchedEffect(authState.value) {
        when (authState.value){
            LoadingFirebase ->{isLoading=true}
            Authenticated->{navController.navigate(HomePage.route){
                popUpTo(LogInPage.route){
                    inclusive=true
                }
            }}
            else->{isLoading=false}
        }
    }
    WaveLinkTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .paint(
                        painter = painterResource(R.drawable.app_logo),
                        contentScale = ContentScale.Crop
                    ),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    "Welcome to ",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(5.dp)
                )
            }

            Text(
                text = "Wave Link",
                color = MutedGrayText,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 5.dp)
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(verticalScroll),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TextFieldWL(text = info[0], password = false, label = "Email") { info[0] = it; info[0] }
                    TextFieldWL(text = info[1], password = true, label = "Password") { info[1] = it; info[1] }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        "Forgot Password?",
                        fontSize = 14.sp,
                        color = SoftPurple,
                        modifier = Modifier.clickable { navController.navigate(ForgotPage.route) }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        modifier = Modifier.animateContentSize(),
                        onClick = {
                            if (!isLoading && check(info, context)) {
                                firebaseViewModel.logIn(context = context, sharedPreferences = sharedPreferences, info)
                            }
                        }
                    ) {
                        if (!isLoading) {
                            Text(text = "Go !", fontSize = 18.sp)
                        } else {
                            LoadingScreenLL()
                        }
                    }
                    Spacer(modifier = Modifier.height(15.dp))
                    Text("Or")
                    Spacer(modifier = Modifier.height(15.dp))
                    Button(
                        onClick = {
                            if (!isLoading) {
                                coroutineScope.launch {
                                    firebaseViewModel.googleLogin(context = context, sharedPreferences = sharedPreferences)
                                }
                            }
                        },
                        modifier = Modifier.animateContentSize()
                    ) {
                        if (!isLoading) {
                            Text(text = "Continue with ", fontSize = 18.sp)
                            Image(
                                painter = painterResource(id = R.drawable.google_logo),
                                contentDescription = "google_logo",
                                modifier = Modifier
                                    .size(20.dp)
                                    .clip(RoundedCornerShape(10.dp))
                            )
                        } else {
                            LoadingScreenLL()
                        }
                    }
                    Spacer(Modifier.height(30.dp))
                    Text(
                        "Newbie? Sign In",
                        fontSize = 14.sp,
                        color = SoftPurple,
                        modifier = Modifier.clickable {
                            navController.navigate(SignInPage.route)
                        }
                    )
                    Spacer(Modifier.height(100.dp))
                }
            }
        }
    }
}
//@Preview(showBackground = true)
//@Composable
//fun LogInPagePr(){
//    LogInPage()
//}