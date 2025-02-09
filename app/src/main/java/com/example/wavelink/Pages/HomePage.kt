package com.example.wavelink.Pages

import android.annotation.SuppressLint
import android.content.SharedPreferences
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.wavelink.AccountPage
import com.example.wavelink.HomePage
import com.example.wavelink.InteractivePage
import com.example.wavelink.InteractivePage2
import com.example.wavelink.R
import com.example.wavelink.ViewModels.BluetoothViewModel
import com.example.wavelink.ui.theme.Background
import com.example.wavelink.ui.theme.MutedGrayText
import com.example.wavelink.ui.theme.WaveLinkTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomePage(
    bluetoothViewModel: BluetoothViewModel,
    sharedPreferences: SharedPreferences,
    navController: NavHostController,
    page: String,
    function: () -> Unit
) {
    val vertical= rememberScrollState()
    WaveLinkTheme {
        Scaffold(bottomBar = {BottomMenu(navController,page,function)}) {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier.fillMaxWidth().paint(
                        painter = painterResource(R.drawable.app_logo),
                        contentScale = ContentScale.Crop
                    ),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        "Home Page ",
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
                        modifier = Modifier.fillMaxWidth().verticalScroll(state = vertical),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(30.dp))
                        Button(
                            onClick = {navController.navigate(InteractivePage.route){
                            } },
                            modifier = Modifier.animateContentSize().width(200.dp).height(50.dp).clip(
                                RoundedCornerShape(20.dp)
                            ),
                            shape = RectangleShape

                        ) {
                            Text(text = "Static Mode ", fontSize = 18.sp)
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        Button(
                            onClick = {navController.navigate(InteractivePage2.route){
                            } },
                            modifier = Modifier.animateContentSize().width(200.dp).height(50.dp).clip(
                                RoundedCornerShape(20.dp)
                            ),
                            shape = RectangleShape

                        ) {
                            Text(text = "Dynamic Mode ", fontSize = 18.sp)
                        }
                        Spacer(Modifier.height(50.dp))
                    }
                }
            }
        }
    }
}
//@Preview(showBackground = true)
//@Composable
//fun HomePagePr(){
//    HomePage(bluetoothViewModel, sharedPreferences, navController)
//}
@Composable
fun BottomMenu(navController: NavHostController, page: String, function: () -> Unit) {
    function()
    BottomAppBar() {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            IconButton(onClick = {navController.navigate(HomePage.route){;
                popUpTo(AccountPage.route){
                    inclusive=true
                }
            } }) {
                Icon(Icons.Default.Home, contentDescription = "Home", modifier = Modifier.size(60.dp),
                    tint = if (page == HomePage.route) Background else Color.Black)
            }
            IconButton(onClick = { navController.navigate(AccountPage.route){
                popUpTo(HomePage.route){
                    inclusive=true
                }
            } }) {
                Icon(Icons.Default.AccountCircle, contentDescription = "Account", modifier = Modifier.size(60.dp),
                    tint = if (page == AccountPage.route) Background else Color.Black)
            }
        }
    }
}