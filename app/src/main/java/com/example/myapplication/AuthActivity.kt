package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
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
import java.util.concurrent.TimeUnit

class AuthActivity : AppCompatActivity() {
    lateinit var launcher: ActivityResultLauncher<Intent>
    lateinit var auth: FirebaseAuth

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            Scaffold {
                var emailState by remember { mutableStateOf(TextFieldValue()) }
                var passwordState by remember { mutableStateOf(TextFieldValue()) }

                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Регистрация",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    OutlinedTextField(
                        value = emailState,
                        onValueChange = { emailState = it },
                        label = { Text("Email") },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done
                        ),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = passwordState,
                        onValueChange = { passwordState = it },
                        label = { Text("Пароль") },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            val email = emailState.text
                            val password = passwordState.text
                            register(email.toString(), password.toString())
                        },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text(text = "Зарегистрироваться")
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { signInWithGoogle() }) {

                    }
                }
            }
        }

        auth = Firebase.auth
//        auth.currentUser
        val cUser = auth.currentUser
        if(cUser!=null){
            Toast.makeText(this, "Пользователь не нулевой", Toast.LENGTH_SHORT).show()
        }
        else{
            Toast.makeText(this, "Пользователь нулевой", Toast.LENGTH_SHORT).show()
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

    private fun register(email: String, password: String){
        Log.d( "Email ","$email $password")
        if(!email.isEmpty() && !password.isEmpty()) {
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(applicationContext, "Зарегало", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(applicationContext, "Не зарегало", Toast.LENGTH_SHORT).show()
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

    private fun signInWithGoogle(){
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




//    private fun sendVerificationCode(phoneNumber: String) {
//        val options = PhoneAuthOptions.newBuilder(auth)
//            .setPhoneNumber(phoneNumber)
//            .setTimeout(60L, TimeUnit.SECONDS)
//            .setActivity(this)
//            .setCallbacks(callbacks)
//            .build()
//        PhoneAuthProvider.verifyPhoneNumber(options)
//    }
//
//    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
//            // Автоматический вход после получения верификационного кода
//            auth.signInWithCredential(credential).addOnCompleteListener {
//                if(it.isSuccessful){
//                    Log.d("MyLog ", "Добро пожаловать!")
//                }
//            }
//        }
//
//        override fun onVerificationFailed(e: FirebaseException) {
//            // Обработка ошибки верификации
//            Log.d("MyLog ", e.message.toString())
//        }
//
//        override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
//            // Сохранение идентификатора верификации для последующего использования
//            super.onCodeSent(verificationId, token)
//        }
//    }
//
//    private fun signInWithCredential(credential: PhoneAuthCredential) {
//        auth.signInWithCredential(credential)
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    Log.d("MyLog", "Все круто!")
//                    checkAuthState()
//                } else {
//                    Log.d("MyLog", "Все не круто!")
//                }
//            }
//    }

}