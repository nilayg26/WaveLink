package com.example.wavelink

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wavelink.ui.theme.DeepBlack
@Composable
fun IconButtonWL(imageVector: ImageVector =Icons.Filled.ShoppingCart, onClick:()->(Unit)={}, size: Int=40) {
    IconButton(modifier = Modifier.size(size.dp),onClick = onClick) {
        val picUrl=""
        Icon(imageVector = imageVector, contentDescription ="shopping cart")
    }
}
@Composable
fun TextFieldWL(text: String, password:Boolean=true ,label: String="",lamda: (String) -> String){
    var passwordVisible by remember {
        mutableStateOf(true)
    }
    OutlinedTextField(value = text, onValueChange ={newVal->lamda(newVal)}, label = { Text(
        text = label, modifier = Modifier.animateContentSize()
    )
    },
        shape = RoundedCornerShape(20.dp), modifier = Modifier.padding(top = 10.dp),
        colors = OutlinedTextFieldDefaults.colors(DeepBlack, DeepBlack),
        visualTransformation =
        if (passwordVisible && password) PasswordVisualTransformation() else VisualTransformation.None ,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {
            val image = if(!password){
                Icons.Filled.AccountBox}else if (passwordVisible)
                Icons.Filled.Favorite
            else Icons.Filled.FavoriteBorder
            val description = if (passwordVisible) "Hide password" else "Show password"
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(imageVector = image, contentDescription = description)
            }
        }
    )
}