package com.example.wavelink

interface Destination{
    var route:String
}
object HomePage:Destination{
    override var route="homepage"
}
object SignInPage:Destination{
    override var route="sign"
}
object LogInPage:Destination{
    override var route="login"
}
object AccountPage:Destination{
    override var route="acc"
}
object InteractivePage:Destination{
    override var route="inter"
}
