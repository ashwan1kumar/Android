package com.krashwani0908.firegram

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

private const val TAG = "LoginActivity"
class LoginActivity : AppCompatActivity() {
    private lateinit var buton:Button
    private lateinit var x:EditText
    private lateinit var y:EditText


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initView()

        buton.setOnClickListener{
//            Toast.makeText(this,"Hello There",Toast.LENGTH_LONG).show()
            val email = x.text.toString()
            val pass = y.text.toString()
            Log.e("this", email)
            Log.e("this2", pass)
            Log.e("this3", "asdasd")
            Toast.makeText(this,"Password = $pass",Toast.LENGTH_SHORT).show()

            if(email.isBlank() || pass.isBlank())
            {
                Toast.makeText(this,"Email and Password Can't be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val auth = FirebaseAuth.getInstance()
            auth.signInWithEmailAndPassword(email,pass).addOnCompleteListener {task ->
                if(task.isSuccessful)
                {
                    Toast.makeText(this,"Login Successful",Toast.LENGTH_SHORT).show()
                }
                else
                {
                    Log.i(TAG,"Sign in with email and password failed",task.exception)
                    Toast.makeText(this,"Authentication Failed",Toast.LENGTH_SHORT).show()
                }
                Toast.makeText(this,"Password = $pass",Toast.LENGTH_SHORT).show()
            }

        }

    }
    private fun initView()
    {
        buton = findViewById(R.id.btnLogin)
        x = findViewById(R.id.etMail)
        y = findViewById(R.id.etPassword)

    }
}