package com.example.wavelink.ViewModels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wavelink.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel
class GeminiViewModel:ViewModel() {
    private val _geminiState= MutableLiveData<GeminiState>()
    val geminiState: LiveData<GeminiState> = _geminiState
    private val _result =MutableLiveData<String>()
    val result: LiveData<String> = _result
    val output= MutableLiveData<String>()
    suspend fun getGeminiOutput(context: Context, data: String):String{
        _geminiState.value= LoadingGemini
        return try{
            val apiKey=BuildConfig.API_KEY
            val generativeModel=GenerativeModel(modelName = "gemini-2.0-flash", apiKey = apiKey)
            _result.value =getData(data)
            println("Gemini data$_result")
            val response=generativeModel.generateContent(prompt = "Given 2D-plane matrix $result Your response should only onlyy consist of a hand/finger/facial gesture emoji that it represents the most, use base case as 'Not a symbol'")
            output.value=response.text.toString()
            _geminiState.value=OutputGenerated
            response.text.toString()
        }
        catch (e:Exception){
            e.printStackTrace()
            _geminiState.value=GeminiError(e.message.toString())
          //  context.createToastMessage("Cannot get output from model")
            (e.message.toString())
        }
    }
    private fun getData(data: String): String {
        println(data)
        if(data==""){
           return  "1,1,1,1\n1,1,1,1\n1,1,1,1\n1,1,1,1"
        }
        var str=""
        val row= data.split(";")
        for(i in 0..3){
            str=str+row[i]+"\n"
        }
        return str
    }

}
interface GeminiState{
    var status:String
}
object LoadingGemini:GeminiState{
    override var status="isloading"
}
object OutputGenerated:GeminiState{
    override var status="OutputGenerated"
}
data class GeminiError(var msg:String):GeminiState{
    override var status="Gemini"
}
