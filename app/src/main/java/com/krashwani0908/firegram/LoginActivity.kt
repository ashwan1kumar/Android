package com.krashwani0908.firegram

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast


private const val TAG = "LoginActivity"
class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        btnLogin.setOnClickListener{
            Toast.makeText(this,"Hello There",8).show()
        }
    }
}