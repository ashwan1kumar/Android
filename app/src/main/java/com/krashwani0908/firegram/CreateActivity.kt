package com.krashwani0908.firegram

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.opengl.EGLImage
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.krashwani0908.firegram.models.Post
import com.krashwani0908.firegram.models.User

private const val PICK_PHOTO_CODE = 1234
class CreateActivity : AppCompatActivity() {
    private var signedInUser: User? = null
    private var photoUri:Uri?=null
    private lateinit var fireStoreDB: FirebaseFirestore
    private lateinit var storageReference: StorageReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create)
        storageReference = FirebaseStorage.getInstance().reference
        fireStoreDB = FirebaseFirestore.getInstance()
        fireStoreDB.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid as String)
            .get()
            .addOnSuccessListener { userSnapshot ->
                signedInUser = userSnapshot.toObject(User::class.java)
//                Log.i(TAG, "signed in user: $signedInUser")
            }
        findViewById<Button>(R.id.btnPickImage).setOnClickListener {
            val imagePickerIntent = Intent(Intent.ACTION_GET_CONTENT)
            imagePickerIntent.type = "image/*"
            if(imagePickerIntent.resolveActivity(packageManager)!=null)
            {
                startActivityForResult(imagePickerIntent,PICK_PHOTO_CODE)
            }
        }
        findViewById<Button>(R.id.btnSubmit).setOnClickListener {
            handlesubmit()
        }
    }
    private fun handlesubmit()
    {
        if(photoUri == null)
        {
            Toast.makeText(this,"Image not selected",Toast.LENGTH_SHORT).show()
            return
        }
        if(findViewById<EditText>(R.id.etDescription).text.isBlank())
        {
            Toast.makeText(this,"Caption can't be empty",Toast.LENGTH_SHORT).show()
            return
        }
        if(signedInUser == null)
        {
            Toast.makeText(this,"No signed in user",Toast.LENGTH_SHORT).show()
            return
        }
        val photoUploadUri = photoUri as Uri
        findViewById<Button>(R.id.btnSubmit).isEnabled = false
        val photoRef = storageReference.child("images/${System.currentTimeMillis()}-photo.jpg")

        photoRef.putFile(photoUploadUri)
            .continueWithTask { photoUploadTask ->
                photoRef.downloadUrl
            }.continueWithTask { downloadUrlTask ->
                val post = Post(
                    findViewById<EditText>(R.id.etDescription).text.toString(),
                    downloadUrlTask.result.toString(),
                    System.currentTimeMillis(),
                    signedInUser
                )
                fireStoreDB.collection("posts").add(post)
            }.addOnCompleteListener { postCreationTask ->
                findViewById<Button>(R.id.btnSubmit).isEnabled = true
                if(!postCreationTask.isSuccessful)
                {
                    Toast.makeText(this,"Failed To save Image",Toast.LENGTH_SHORT).show()
                }
                findViewById<EditText>(R.id.etDescription).text.clear()
                findViewById<ImageView>(R.id.imageView).setImageResource(0)
                Toast.makeText(this,"Success",Toast.LENGTH_SHORT).show()
                val profileintent = Intent(this,ProfileActivity::class.java)
                profileintent.putExtra(EXTRA_USERNAME,signedInUser?.username)
                startActivity(profileintent)
                finish()
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode== PICK_PHOTO_CODE)
        {
            if(resultCode== Activity.RESULT_OK)
            {
                photoUri = data?.data
                findViewById<ImageView>(R.id.imageView).setImageURI(photoUri)
            }
            else
            {
                Toast.makeText(this,"Image Pick cancelled",Toast.LENGTH_LONG).show()
            }
        }
    }
}