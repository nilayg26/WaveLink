package com.example.wavelink.Pages

import android.annotation.SuppressLint
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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wavelink.R
import com.example.wavelink.ui.theme.Background
import com.example.wavelink.ui.theme.MutedGrayText
import com.example.wavelink.ui.theme.WaveLinkTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomePage(){
    WaveLinkTheme {
        Scaffold(bottomBar = {BottomMenu()}) {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier.fillMaxWidth().paint(
                        painter = painterResource(R.drawable.app_logo),
                        contentScale = ContentScale.FillBounds
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
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Spacer(modifier = Modifier.height(10.dp))
                        Spacer(modifier = Modifier.height(30.dp))
                        Button(
                            onClick = {/*google log in*/ },
                            modifier = Modifier.animateContentSize().width(300.dp).height(100.dp).clip(
                                RoundedCornerShape(20.dp)
                            ),
                            shape = RectangleShape

                        ) {
                            Text(text = "Interactive Mode ", fontSize = 18.sp)
                        }
                        Spacer(Modifier.height(50.dp))
                    }
                }
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun HomePagePr(){
    HomePage()
}
@Composable
fun BottomMenu(){
    BottomAppBar() {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            IconButton(onClick = { /* Handle home click */ }) {
                Icon(Icons.Default.Home, contentDescription = "Home", modifier = Modifier.size(60.dp),
                    tint = if (true) Color.Black else Background)
            }
            IconButton(onClick = { /* Handle search click */ }) {
                Icon(Icons.Default.AccountCircle, contentDescription = "Search", modifier = Modifier.size(60.dp),
                    tint = if (true) Color.Black else Background)
            }
        }
    }
}