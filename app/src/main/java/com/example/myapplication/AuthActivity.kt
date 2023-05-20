package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.Auth.AuthMainScreen
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInApi
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import io.reactivex.rxjava3.annotations.NonNull
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class AuthActivity : AppCompatActivity() {
    lateinit var launcher: ActivityResultLauncher<Intent>
    lateinit var auth: FirebaseAuth
    lateinit var storedVerificationId: String
    lateinit var resendToken: PhoneAuthProvider.ForceResendingToken


    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContent{
            Scaffold {
                AuthMainScreen(authActivity = this@AuthActivity)
            }
        }

        auth = Firebase.auth
//        auth.currentUser
        val cUser = auth.currentUser
        if(cUser!=null){
            Toast.makeText(this, "Пора работать!", Toast.LENGTH_SHORT).show()
        }
        else{
            Toast.makeText(this, "Добро пожаловать", Toast.LENGTH_SHORT).show()
        }
        launcher = registerForActivityResult(StartActivityForResult()){
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            try {
                val account = task.getResult(ApiException::class.java)
                if(account != null){
                    firebaseAuthWithGoogle(account.idToken!!)
                }
            }
            catch (e: ApiException){
                Log.d("MyLog", "Api exception")
            }
        }
        checkAuthState()
    }


    fun register(email: String, password: String){
        Log.d( "Email ","$email $password")
        if(!email.isEmpty() && !password.isEmpty()) {
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(this, "Пользователь $email успешно зарегистрирован", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Регистрация не прошла!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun signIn(email: String, password: String){
        if(!email.isEmpty() && !password.isEmpty()) {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(this, "Вошли в аккаунт успешно", Toast.LENGTH_SHORT).show()
                    checkAuthState()
                } else {
                    Toast.makeText(this, "Пользователь не найден", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun getClient(): GoogleSignInClient{
        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(this, gso)
    }

    fun signInWithGoogle(){
        val signInClient = getClient()
        launcher.launch(signInClient.signInIntent)
    }

    private fun firebaseAuthWithGoogle(idToken: String){
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener{
            if(it.isSuccessful){
                Log.d("MyLog", "Все круто!")
                checkAuthState()
            }
            else{
                Log.d("MyLog", "Все не круто!")
            }
        }
    }

    private fun checkAuthState(){
        if(auth.currentUser != null){
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
        }
    }

    fun sendVerificationCode(phoneNumber: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            // Автоматический вход после получения верификационного кода
            auth.signInWithCredential(credential).addOnCompleteListener {
                if(it.isSuccessful){
                    Log.d("MyLog ", "Добро пожаловать!")
                }
            }
        }

        override fun onVerificationFailed(e: FirebaseException) {
            // Обработка ошибки верификации
            Log.d("MyLog ", e.message.toString())
        }

        override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
            // Сохранение идентификатора верификации для последующего использования
            Log.d("verID = ", "onCodeSent:$verificationId")

            // Save verification ID and resending token so we can use them later
            storedVerificationId = verificationId
            resendToken = token
        }
    }
    fun signInWithCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("MyLog", "Все круто!")
                    checkAuthState()
                } else {
                    Log.d("MyLog", "Все не круто!")
                }
            }
    }

}