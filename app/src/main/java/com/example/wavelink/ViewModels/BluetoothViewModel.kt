package com.example.wavelink.ViewModels
import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.example.wavelink.createToastMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.UUID

class BluetoothViewModel : ViewModel() {
    private val _bluetoothState = MutableStateFlow<BluetoothState>(BluetoothDisconnected())
    val bluetoothState: StateFlow<BluetoothState> = _bluetoothState
    val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    private var bluetoothSocket: BluetoothSocket? = null
    private var _statusOfDirection=MutableStateFlow("")
    var statusOfDirection:StateFlow<String> = _statusOfDirection
    private var inputStream: InputStream? = null
    private var outputStream: OutputStream? = null
    private val ioScope = CoroutineScope(Dispatchers.IO)
    private var connectedDevice: BluetoothDevice? = null
    var isConnected by mutableStateOf(false)
        private set
    var receivedData by mutableStateOf("")
        private set
    @RequiresApi(Build.VERSION_CODES.S)
    suspend fun checkExistingConnection(context: Context, geminiViewModel: GeminiViewModel,page2:Boolean=false) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            println("Bluetooth permission not granted")
            return
        }
        _bluetoothState.value = BluetoothLoading()

        if (bluetoothAdapter == null || !hasBluetoothConnectPermission(context)) {
            return
        }
        bluetoothAdapter.bondedDevices.forEach { device ->
            try {
                connectToDevice(device, context,geminiViewModel,page2)
                return
            } catch (e: IOException) {
                handleConnectionError(e, context)
            }
        }
    }

    private suspend fun connectToDevice(
        device: BluetoothDevice,
        context: Context,
        geminiViewModel: GeminiViewModel,
        page2: Boolean
    ) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            println("Bluetooth permission not granted")
            return
        }
        val socket = device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"))
        bluetoothSocket = socket
        connectedDevice = device
        socket.connect()

        inputStream = socket.inputStream
        outputStream = socket.outputStream
        isConnected = true

        context.createToastMessage("Connected to ${device.name}")
        if (!page2) {
            startDataReceiving(geminiViewModel)
        }
        else{
            startDataReceiving2(geminiViewModel)
        }
    }

    private fun handleConnectionError(e: IOException, context: Context) {
        println("Connection failed: ${e.message}")
        context.createToastMessage("Connection failed: ${e.message}")
        _bluetoothState.value = BluetoothError(e.message.toString())
    }

    private suspend fun startDataReceiving(geminiViewModel: GeminiViewModel) {
        ioScope.launch {
            try {
                val buffer = ByteArray(1024)

                while (isConnected) {
                    _bluetoothState.value = BluetoothConnected() // Immediate update
                    try {
                        println("State Value: ${_bluetoothState.value}")
                        val bytesRead = inputStream?.read(buffer) ?: throw IOException("Input stream is null")
                        if (bytesRead > 0) {
                            if (geminiViewModel.geminiState.value != LoadingGemini) {
                                println("not loading")
                                receivedData = String(buffer, 0, bytesRead)
                                println("Bluetooth Data: $receivedData")

                                _bluetoothState.value = BluetoothDataReceived() // Immediate update
                                println("State Value: ${_bluetoothState.value}")
                            }
                        } else if (bytesRead == -1) {
                            throw IOException("Socket closed or disconnected")
                        }
                    } catch (e: IOException) {
                        handleReadError(e)
                        break
                    }
                    delay(2000) // Delay between reads
                }
            } finally {
                cleanupResources()
            }
        }
    }
    private suspend fun startDataReceiving2(geminiViewModel: GeminiViewModel) {
        ioScope.launch {
            try {
                var matrix2= mutableListOf<String>()
                val buffer = ByteArray(1024)
                var count =0;
                while (isConnected) {
                    _bluetoothState.value = BluetoothConnected() // Immediate update
                    try {
                        println("State Value: ${_bluetoothState.value}")
                        val bytesRead = inputStream?.read(buffer) ?: throw IOException("Input stream is null")
                        if (bytesRead > 0) {
                            if (geminiViewModel.geminiState.value != LoadingGemini) {
                                if(count%2==0){
                                    matrix2= mutableListOf()
                                    matrix2.add(receivedData)
                               // println("not loading")
                                receivedData = String(buffer, 0, bytesRead)
                               // println("Bluetooth Data: $receivedData")
                                _bluetoothState.value = BluetoothDataReceived() // Immediate update
                               // println("State Value: ${_bluetoothState.value}")
                                 count++
                                }
                                else{
                                    matrix2.add(receivedData)
                                    println("The matrix are: $matrix2")
                                    process(matrix2)
                                    count++
                                }
                            }
                        } else if (bytesRead == -1) {
                            throw IOException("Socket closed or disconnected")
                        }
                    } catch (e: IOException) {
                        handleReadError(e)
                        break
                    }
                    if (count%2==1){
                        delay(1000)
                    }
                    if (count%2==0){
                        delay(600)
                    }
                }

            } finally {
                cleanupResources()
            }
        }
    }

    private suspend fun handleReadError(e: IOException) {
            println("Read error: ${e.message}")
            isConnected = false
            _bluetoothState.value= BluetoothError(e.message.toString())
    }

    private fun cleanupResources() {
        try {
            inputStream?.close()
            bluetoothSocket?.close()
            println("Resources Closed")
        } catch (e: IOException) {
            e.printStackTrace()
        }
        isConnected = false
        _bluetoothState.value=BluetoothDisconnected()
        println("Finally block")
    }
    private fun process(matrix2:MutableList<String>){
        val first=parseMatrix(matrix2[0])
        val second=parseMatrix(matrix2[1])
        val one=calculateCentroid(first)
        val two=calculateCentroid(second)
        val x1=one[0]
        val y1=one[1]
        val x2=two[0]
        val y2=two[1]
        if(x1<x2 && y1>y2){
            println("Nilayyyy Moved Top Right")
            _statusOfDirection.value="Moved Top Right"
        }
        else if(x2<x1 && y1>y2){
            println("Nilayyyy Moved Top Left")
            _statusOfDirection.value="Moved Top Left"
        }
        else if(x2<x1 && y2>y1){
            println("Nilayyyy Moved Down Left")
            _statusOfDirection.value="Moved Down Left"
        }
        else if(x2>x1 && y2>y1){
            println(" Nilayyyy Moved Down Right")
            _statusOfDirection.value="Moved Down Right"
        }
        else if(y2<y1){
            println(" Nilayyyy Moved Up")
            _statusOfDirection.value="Moved Up"
        }
        else if(y1<y2){
            println(" Nilayyyy Moved Down")
            _statusOfDirection.value="Moved Down"
        }
        else if(x2>x1){
            println(" Nilayyyy Moved Right")
            _statusOfDirection.value="Moved Right"
        }
        else if (x1>x2){
            println(" Nilayyyy Moved Left")
            _statusOfDirection.value="Moved Left"
        }
        else{
            _statusOfDirection.value="Idle"
        }
    }
    private fun calculateCentroid(matrix: List<List<Int>>): List<Double> {
        var count = 0
        var iSum = 0
        var jSum = 0
        for (i in matrix.indices) {
            for (j in matrix[i].indices) {
                if (matrix[i][j] == 1) {
                    count++
                    iSum += i
                    jSum += j
                }
            }
        }
        if (count==0){
            return listOf(0.0,0.0)
        }
        val x = iSum.toDouble() / count
        println("X is:"+x)
        val y = jSum.toDouble() / count
        println("y is:"+y)
        return listOf(x,y)
    }
    private fun parseMatrix(inputString: String): List<List<Int>> {
        return inputString.split("\n").map { row ->
            row.split(",").mapNotNull { it.toIntOrNull() }
        }
    }
    fun disconnect() {
        ioScope.launch {
            withContext(Dispatchers.Main) {
                try {
                    bluetoothSocket?.close()
                    isConnected = false
                    _bluetoothState.value = BluetoothDisconnected()
                } catch (e: IOException) {
                    e.printStackTrace()
                    _bluetoothState.value = BluetoothError(e.message.toString())
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun hasBluetoothConnectPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.BLUETOOTH_CONNECT
        ) == PackageManager.PERMISSION_GRANTED
    }
}

interface BluetoothState {
    var status: String
}

class BluetoothLoading : BluetoothState {
    override var status = "Loading"
}
class BluetoothConnected : BluetoothState {
    override var status = "Connected"
}
class BluetoothDataReceived : BluetoothState {
    override var status = "Data"
}

class  BluetoothDisconnected : BluetoothState {
    override var status = "Disconnected"
}

data class BluetoothError(var msg: String) : BluetoothState {
    override var status = "error"
}

interface StateMatrix{
    var status:String
}