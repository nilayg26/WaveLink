package com.example.wavelink.ViewModels
import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wavelink.R
import com.example.wavelink.createToastMessage
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseViewModel :ViewModel(){
    private val _authState=MutableLiveData<FirebaseState>()
    val authState:LiveData<FirebaseState> = _authState
    private var auth= FirebaseAuth.getInstance()
    private var db= FirebaseFirestore.getInstance()
    @SuppressLint("SuspiciousIndentation")
    fun logIn(context: Context, sharedPreferences: SharedPreferences, info: MutableList<String>){
        _authState.value= LoadingFirebase
        val email=info[0]
        val pass=info[1]
        auth.signInWithEmailAndPassword(email,pass).addOnCompleteListener {
                task->
            if (task.isSuccessful){
                if (auth.currentUser?.isEmailVerified==true) {
                    sharedPreferences.edit()
                        .putBoolean("LoginStatus", true)
                        .putString("email",email)
                        .apply()
                    val uid = auth.uid
                    getUser(uid.toString(), sharedPreferences, context)
                    _authState.value = Authenticated
                }
                else{
                    context.createToastMessage("Check your email to verify")
                    _authState.value=EmailNotVerified
                }
            }
            else {
                context.createToastMessage(task.exception?.message.toString())
                _authState.value = FirebaseError(task.exception?.message ?: "Something went wrong")
            }
        }
    }
    @SuppressLint("SuspiciousIndentation")
    fun signUp(context: Context, sharedPreferences: SharedPreferences, info:MutableList<String>){
        _authState.value= LoadingFirebase
        val name=info[0]
        val email=info[1]
        val pass=info[2]
            auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener {
                    task->
                if (task.isSuccessful) {
                    auth.currentUser?.sendEmailVerification()?.addOnSuccessListener {
                        context.createToastMessage("Check your inbox to verify and login")
                        val boolean= addUser(email, name,context)
                        if (boolean){
                            _authState.value = EmailNotVerified
                            sharedPreferences.edit()
                                .putString("uid",auth.uid)
                                .putString("name",name)
                                .putString("email",email)
                                .putBoolean("loginStatus", true)
                                .apply()
                        }
                        else{
                            context.createToastMessage("Something went wrong")
                            _authState.value=FirebaseError("Something went wrong")
                        }
                    }
                }
                else{
                    context.createToastMessage(task.exception?.message.toString())
                    _authState.value = FirebaseError(task.exception?.message ?: "Something went wrong")
                }
            }

    }
    private fun addUser(email: String, name: String, context: Context) :Boolean {
        val user = User(email = email, name = name)
        try {
            auth.uid?.let {
                db.collection("Users").document(it).set(user)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            println("User Added");
                        } else {
                            context.createToastMessage(task.exception?.message.toString())
                            println("error from add user: " + task.exception?.message)
                        }
                    }
            }
            return true
        } catch (e: Exception) {
            context.createToastMessage(e.message.toString())
            return false
        }
    }
    private fun getUser(uid: String, sharedPreferences: SharedPreferences, context: Context){
        db.collection("Users").document(uid).get().addOnFailureListener { e ->
            Log.d("Firestore", "Error fetching documents", e)
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                if (task.result.exists()) {
                    val snapshot = task.result
                    val name = snapshot.data?.get("name").toString()
                    context.createToastMessage("Welcome $name")
                    sharedPreferences.edit().putString("name",name)
                        .apply()
                } else {
                    println("From getUser(): User does not exists")
                }
            } else {
                context.createToastMessage(task.exception?.message.toString())
                println("From getUser(): ${task.exception?.message.toString()}")
                _authState.value = FirebaseError(task.exception?.message.toString())
            }
        }
    }
    fun forgotPassword(email: String,context: Context){
        _authState.value= LoadingFirebase
        auth.sendPasswordResetEmail(email).addOnCompleteListener{
                task->
            if (task.isSuccessful){
                try {
                    context.createToastMessage("Check inbox")
                    _authState.value = EmailNotVerified
                }
                catch (e:Exception){
                    val err="Account not found"
                    context.createToastMessage(err)
                    println("From forgotPassword(): ${e.message}")
                    _authState.value=FirebaseError(err)
                }
            }
            else{
                context.createToastMessage(task.exception?.message.toString())
                println("From forgotPassword(): ${task.exception?.message.toString()}")
                _authState.value=FirebaseError(task.exception?.message.toString())
            }
        }
    }
    suspend fun googleLogin(sharedPreferences: SharedPreferences, context: Context) {
        _authState.value = LoadingFirebase
        val request = getRequest(context = context)
        val credentialManager = CredentialManager.create(context = context)
        try {
            val result = credentialManager.getCredential(request = request, context = context)
            when (result.credential) {
                is CustomCredential -> {
                    if (result.credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                        val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(
                            result.credential.data
                        )
                        val googleIdTokenId = googleIdTokenCredential.idToken
                        val authCredential = GoogleAuthProvider.getCredential(googleIdTokenId, null)
                        auth.signInWithCredential(authCredential).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val name= auth.currentUser?.displayName?:"No name can be found"
                                val email= auth.currentUser?.email?:"No email can be found"
                                val picUrl=auth.currentUser?.photoUrl?:"null"
                                sharedPreferences.edit()
                                    .putBoolean("loginStatus",true)
                                    .putString("name",name)
                                    .putString("email",email)
                                    .putString("picUrl",picUrl.toString())
                                    .apply()
                                _authState.value = Authenticated
                            } else {
                                val msg=task.exception?.message ?: "Something went wrong"
                                context.createToastMessage(msg)
                                _authState.value =FirebaseError(msg)
                            }
                        }
                    } else {
                        context.createToastMessage("Try Again!")
                        _authState.value=Unauthenticated
                    }
                }
            }

        }
        catch (e:Exception) {
            context.createToastMessage("Could not get to your Google Account")
            println("Error from googleLogIn Function: "+e.message.toString())
            _authState.value= Unauthenticated
        }
    }
    private fun getRequest(context: Context): GetCredentialRequest {
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(context.getString(R.string.web_client_id))
            .build()
        return (GetCredentialRequest.Builder().addCredentialOption(googleIdOption).build())
    }
    fun logOut(context: Context, sharedPreferences: SharedPreferences) {
        sharedPreferences.edit().clear().apply()
        auth.signOut()
        _authState.value = Unauthenticated
    }
}
interface FirebaseState{
    var status:String
}
object LoadingFirebase:FirebaseState{
    override var status="loading"
}
object EmailNotVerified:FirebaseState{
    override var status="emailnotverified"
}
object ForgotPassword:FirebaseState{
    override var status="Loading"
}
object Authenticated:FirebaseState{
    override var status="authenticated"
}
object Unauthenticated:FirebaseState{
    override var status="unauthenticated"
}
data class FirebaseError(var msg:String):FirebaseState{
    override var status="Error"
}
data class User(
    var name: String,
    var email: String,
)