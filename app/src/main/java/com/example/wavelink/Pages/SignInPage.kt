package com.example.wavelink.Pages
import android.content.SharedPreferences
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.wavelink.LoadingScreenLL
import com.example.wavelink.LogInPage
import com.example.wavelink.R
import com.example.wavelink.SignInPage
import com.example.wavelink.TextFieldWL
import com.example.wavelink.ViewModels.EmailNotVerified
import com.example.wavelink.ViewModels.FirebaseViewModel
import com.example.wavelink.ViewModels.LoadingFirebase
import com.example.wavelink.ui.theme.MutedGrayText
import com.example.wavelink.ui.theme.WaveLinkTheme
import com.example.wavelink.check
@Composable
fun SignInPage(
    firebaseViewModel: FirebaseViewModel,
    sharedPreferences: SharedPreferences,
    navController: NavHostController
) {
    val verticalScroll= rememberScrollState()
    val info = remember {
        mutableStateListOf("","","","")
    }
    var isLoading by remember {
       mutableStateOf(false)
    }
    val context= LocalContext.current
    val authState=firebaseViewModel.authState.observeAsState()
    LaunchedEffect(authState.value) {
        when (authState.value){
            EmailNotVerified -> {navController.navigate(LogInPage.route){
                popUpTo(SignInPage.route){
                    inclusive=true
                }
            }}
            LoadingFirebase->{isLoading=true}
            else->{isLoading=false}
        }
    }
    WaveLinkTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier.fillMaxWidth().paint(
                    painter = painterResource(R.drawable.app_logo),
                    contentScale = ContentScale.Crop
                ), horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    "Welcome to ",
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
                    modifier = Modifier.fillMaxWidth().verticalScroll(state = verticalScroll),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TextFieldWL(text = info[0], password = false, label = "Name"){info[0]=it; info[0]}
                    TextFieldWL(text = info[1], password = false, label = "Email") {info[1]=it; info[1] }
                    TextFieldWL(text = info[2], password = true, label = "Password") { info[2]=it; info[2] }
                    TextFieldWL(text = info[3], password = true, label = "Confirm Password") { info[3]=it;info[3]}
                    Spacer(modifier = Modifier.height(30.dp))
                    Button(onClick = {
                        if (check(info,context)){
                            firebaseViewModel.signUp(context=context,sharedPreferences=sharedPreferences,info)
                        }
                    }) {
                        if (!isLoading) {
                            Text(text = "Go !", fontSize = 18.sp)
                        }
                        else{
                            LoadingScreenLL()
                        }
                    }
                    Spacer(Modifier.height(100.dp))
                }
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun SignInPagePr(){
//    SignInPage()
//}