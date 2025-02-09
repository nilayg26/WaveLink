package com.example.wavelink

import android.content.Context
import android.widget.Toast

fun Context.createToastMessage(text:String,duration:Int=0){
    if(duration==0) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }
    else{
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }
}