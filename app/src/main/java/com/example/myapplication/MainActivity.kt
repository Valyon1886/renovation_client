package com.example.myapplication

import android.content.Intent
import android.media.Image
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import com.example.myapplication.Navigation.Bottom_Navigation.MainScreen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class MainActivity : ComponentActivity() {
    lateinit var auth: FirebaseAuth
//    var imageUri: Image? = null



    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        setContent {
            MainScreen(auth, mainActivity = this@MainActivity)
        }
        Log.d("ID = ", "${auth.currentUser?.uid}")
    }

    fun signOut(){
        auth.signOut()
        finish()
    }

//    @Deprecated("Deprecated in Java")
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if(requestCode == 1 && data != null && data.data != null){
//            if(resultCode == RESULT_OK){
//                Log.d("Image ", "URL = ${data.data}")
//
//            }
//        }
//    }
//    fun getImage(){
//        val intentChooser = Intent(Intent.ACTION_GET_CONTENT).apply {
//            type = "image/*"
//        }
//        startActivityForResult(intentChooser, 1)
//    }
}






