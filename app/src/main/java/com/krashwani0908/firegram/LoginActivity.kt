package com.krashwani0908.firegram

import android.content.Intent
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
        val auth = FirebaseAuth.getInstance()
        if(auth.currentUser != null)
        {
            goPostsActivity()
        }
        initView()

        buton.setOnClickListener{
//            Toast.makeText(this,"Hello There",Toast.LENGTH_LONG).show()
            buton.isEnabled = false
            val email = x.text.toString()
            val pass = y.text.toString()


            if(email.isBlank() || pass.isBlank())
            {
                Toast.makeText(this,"Email and Password Can't be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email,pass).addOnCompleteListener {task ->

                if(task.isSuccessful)
                {
                    Toast.makeText(this,"Login Successful",Toast.LENGTH_SHORT).show()
                    goPostsActivity()
                }
                else
                {
                    Log.i(TAG,"Sign in with email and password failed",task.exception)
                    Toast.makeText(this,"Authentication Failed",Toast.LENGTH_SHORT).show()
                    buton.isEnabled = true
                }

            }

        }

    }
    private fun initView()
    {
        buton = findViewById(R.id.btnLogin)
        x = findViewById(R.id.etMail)
        y = findViewById(R.id.etPassword)

    }
    private fun goPostsActivity()
    {
        Log.i(TAG,"GoPosts")
        val intent = Intent(this,PostsActivity::class.java)
        startActivity(intent)
        finish()
    }
}