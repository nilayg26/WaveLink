package com.example.wavelink.Pages
import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.example.wavelink.IconButtonWL
import com.example.wavelink.ViewModels.BluetoothConnected
import com.example.wavelink.ViewModels.BluetoothDataReceived
import com.example.wavelink.ViewModels.BluetoothViewModel
import com.example.wavelink.ViewModels.FirebaseViewModel
import com.example.wavelink.ViewModels.GeminiViewModel
import com.example.wavelink.ViewModels.LoadingGemini
import com.example.wavelink.ViewModels.OutputGenerated
import com.example.wavelink.ui.theme.DeepBlack
import com.example.wavelink.ui.theme.TechBlue
import com.example.wavelink.ui.theme.WaveLinkTheme
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.S)
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun InteractivePage(
    bluetoothViewModel: BluetoothViewModel,
    geminiViewModel: GeminiViewModel,
    firebaseViewModel: FirebaseViewModel,
    navController: NavHostController
) {
    var hasBluetoothPermission by remember {
        mutableStateOf(false)
    }
    val geminiOutput=geminiViewModel.output
    var colorMatrix= remember {
        mutableStateListOf( listOf(Color.White, Color.White, Color.White, Color.White),
            listOf(Color.White, Color.White, Color.White, Color.White),
            listOf(Color.White, Color.White, Color.White, Color.White),
            listOf(Color.White, Color.White, Color.White, Color.White))
    }
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        hasBluetoothPermission = permissions.values.all { it }
    }
    val result=geminiViewModel.result.observeAsState()
    LaunchedEffect(result.value) {
        if (result.value!=null&& result.value?.isNotEmpty() == true){
            println(result.value.toString())
            updateColorMatrix(colorMatrix,result.value.toString())
        }
    }
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        val permissionsToRequest = listOf(
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        val permissionsNotGranted = permissionsToRequest.filter {
            ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED
        }
        if (permissionsNotGranted.isNotEmpty()) {
            launcher.launch(permissionsNotGranted.toTypedArray())
        } else {
            hasBluetoothPermission = true
        }
    }
    val geminiState=geminiViewModel.geminiState.observeAsState("")
    val bluetoothState=bluetoothViewModel.bluetoothState.collectAsState()
    var isloading by remember {
        mutableStateOf(false)
    }
    val coroutineScope= rememberCoroutineScope()
    LaunchedEffect(bluetoothState.value) {
        when(bluetoothState.value){
            is BluetoothDataReceived-> {
                val data =bluetoothViewModel.receivedData
                geminiViewModel.getGeminiOutput(context,data)
            }
        }
    }
    DisposableEffect(Unit) {
        onDispose {
            bluetoothViewModel.disconnect()

        }
    }
    LaunchedEffect(geminiState.value) {
        when(geminiState.value){
            LoadingGemini->{isloading=true}
            OutputGenerated->{
                println("AI response: "+geminiViewModel.output.value.toString())
            }
            else ->{isloading=false}
        }
    }
    val verticalScrollState= rememberScrollState()
    WaveLinkTheme {
        Scaffold(topBar = {
            TopAppBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding(),
                title = {
                    Text(
                        text = "Wave Link",
                        fontSize = 30.sp,
                        color = DeepBlack,
                        modifier = Modifier.fillMaxWidth(),
                    )
                },
                navigationIcon = {
                    IconButtonWL(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        onClick = { navController.popBackStack() }
                    )
                }
            )
        }){ paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(verticalScrollState)
        ) {
                Spacer(modifier = Modifier.height(10.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .border(width = 5.dp, color = TechBlue)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp).animateContentSize(),
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {
                        for (row in colorMatrix) {
                            Row(
                                modifier = Modifier.fillMaxWidth().animateContentSize(),
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
                                            ).animateContentSize()
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                    }
                }
                Spacer(Modifier.height(60.dp))
                Row(Modifier.padding(start = 16.dp, end = 16.dp)) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(width = 5.dp, color = TechBlue).animateContentSize(),
                        shape = RectangleShape
                    ) {
                        Spacer(Modifier.height(30.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Absolute.SpaceAround
                        ) {
                            Text("AI Response: ", color = TechBlue, fontSize = 25.sp)
                            geminiOutput.value.toString()
                            Text(if(geminiOutput.value.toString()=="null"){"idle"}else{geminiOutput.value.toString()}, color = TechBlue, fontSize = 25.sp)
                        }
                        Spacer(Modifier.height(30.dp))
                    }
                }
                Row(Modifier.padding(start = 16.dp, end = 16.dp)) {
                    Box(modifier = Modifier.fillMaxWidth(0.65f)) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(width = 5.dp, color = TechBlue), shape = RectangleShape
                        ) {
                            Spacer(Modifier.height(30.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        coroutineScope.launch {
                                            if (bluetoothState.value !is BluetoothConnected||bluetoothState.value !is BluetoothDataReceived) {
                                                bluetoothViewModel.checkExistingConnection(context,geminiViewModel)
                                            }
                                        }
                                    },
                                horizontalArrangement = Arrangement.Absolute.SpaceAround
                            ) {
                                Text("Connect ", color = DeepBlack, fontSize = 20.sp)
                            }
                            Spacer(Modifier.height(30.dp))
                        }
                    }
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(width = 5.dp, color = TechBlue), shape = RectangleShape
                        ) {
                            Spacer(Modifier.height(30.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Absolute.SpaceAround
                            ) {
                                Text("Map: Y", color = TechBlue, fontSize = 20.sp)
                            }
                            Spacer(Modifier.height(30.dp))
                        }
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
fun updateColorMatrix(colorMatrix: SnapshotStateList<List<Color>>, inputString: String) {
    colorMatrix.clear()
    val map = mapOf(0 to Color.Black, 1 to Color.White)

    val rows = inputString.split("\n")
    for (rowStr in rows) {
        val colorsInRow = rowStr.split(",").mapNotNull { elementStr ->
            elementStr.toIntOrNull()?.let { num -> map[num] ?: Color.Gray }
        }
        colorMatrix.add(colorsInRow)
    }
}
