package com.example.wavelink.Pages

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wavelink.LogInPage
import com.example.wavelink.R
import com.example.wavelink.TextFieldWL
import com.example.wavelink.ui.theme.MutedGrayText
import com.example.wavelink.ui.theme.SoftBlueText
import com.example.wavelink.ui.theme.SoftPurple
import com.example.wavelink.ui.theme.WaveLinkTheme

@Composable
fun LogInPage(){
    val verticalScroll= rememberScrollState()
    WaveLinkTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier.fillMaxWidth().paint(
                    painter = painterResource(R.drawable.app_logo),
                    contentScale = ContentScale.FillBounds
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
                    TextFieldWL(text = "abc@gmail.com", password = false, label = "Email") { it }
                    TextFieldWL(text = "1234", password = true, label = "Password") { it }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text("Forgot Password?", fontSize = 14.sp, color =  SoftPurple, modifier = Modifier.clickable {  })
                    Spacer(modifier = Modifier.height(30.dp))
                    Button(onClick = {/*google log in*/}, modifier = Modifier.animateContentSize()) {
                        Text(text = "Continue with ", fontSize = 18.sp)
                            Image(
                                painter = painterResource(id = R.drawable.google_logo),
                                contentDescription = "google_logo",
                                modifier = Modifier.size(
                                    20.dp
                                ).clip(RoundedCornerShape(10.dp))
                            )
                    }
                    Spacer(Modifier.height(50.dp))
                    Text("Newbie? SigIn", fontSize = 14.sp, color =  SoftPurple,modifier = Modifier.clickable {  })
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