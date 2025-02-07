package com.example.wavelink.Pages
import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wavelink.IconButtonWL
import com.example.wavelink.ui.theme.DeepBlack
import com.example.wavelink.ui.theme.TechBlue
import com.example.wavelink.ui.theme.WaveLinkTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun InteractivePage(){
    val colors = listOf(
        listOf(Color.White, Color.White, Color.White, Color.Black),
        listOf(Color.White, Color.White, Color.White, Color.Black),
        listOf(Color.White, Color.White, Color.White, Color.Black),
        listOf(Color.White, Color.White, Color.White, Color.Black)
    )
    val verticalScrollState= rememberScrollState()
    WaveLinkTheme {
            Column(modifier = Modifier.fillMaxSize().verticalScroll(state = verticalScrollState)) {
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Spacer(modifier = Modifier.width(10.dp))
                    IconButtonWL(imageVector = Icons.AutoMirrored.Filled.ArrowBack, onClick = {})
                    Spacer(Modifier.width(80.dp))
                    Text("Wave Link", fontSize = 30.sp, color = DeepBlack)
                }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp).border(width = 5.dp, color = TechBlue)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(20.dp),
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {
                        for (row in colors) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                for (color in row) {
                                    Box(
                                        modifier = Modifier
                                            .size(60.dp)
                                            .background(color, shape = RoundedCornerShape(4.dp))
                                            .border(
                                                1.dp,
                                                Color.Black,
                                                shape = RoundedCornerShape(4.dp)
                                            )
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                    }
                }
                Spacer(Modifier.height(60.dp))
                Row(Modifier.padding(start = 16.dp, end = 16.dp)) {
                    Card(modifier = Modifier.fillMaxWidth().border(width = 5.dp, color = TechBlue), shape = RectangleShape) {
                        Spacer(Modifier.height(30.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Absolute.SpaceAround) {
                            Text("Detected Value: ", color = TechBlue, fontSize = 25.sp)
                            Text("V",color = TechBlue, fontSize = 25.sp)
                        }
                        Spacer(Modifier.height(30.dp))
                    }
                }
                Spacer(Modifier.height(60.dp))
                Row(Modifier.padding(start = 16.dp, end = 16.dp)) {
                    Box(modifier = Modifier.fillMaxWidth(0.65f)) {
                        Card(
                            modifier = Modifier.fillMaxWidth()
                                .border(width = 5.dp, color = TechBlue), shape = RectangleShape
                        ) {
                            Spacer(Modifier.height(30.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Absolute.SpaceAround
                            ) {
                                Text("Console: ", color = TechBlue, fontSize = 20.sp)
                                Text("Y V O", color = TechBlue, fontSize = 20.sp)
                            }
                            Spacer(Modifier.height(30.dp))
                        }
                    }
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Card(
                            modifier = Modifier.fillMaxWidth()
                                .border(width = 5.dp, color = TechBlue), shape = RectangleShape
                        ) {
                            Spacer(Modifier.height(30.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Absolute.SpaceAround
                            ) {
                                Text("Map: Y", color = DeepBlack, fontSize = 20.sp)
                               // Text("Y", color = TechBlue, fontSize = 20.sp)
                            }
                            Spacer(Modifier.height(30.dp))
                        }
                    }
                }

            }
        }
}
@Composable
fun Console(){
    Box(modifier = Modifier.padding(16.dp)){
        Text("HI V")
    }
}
@Preview(showBackground = true)
@Composable
fun InteractivePagePr(){
    InteractivePage()
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicTopAppBar(onBackClick: () -> Unit) {

}
