package com.krashwani0908.firegram

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.firestore.FirebaseFirestore
import com.krashwani0908.firegram.models.Post
//import com.krashwani0908.firegram.models.Posts

private const val TAG = "PostsActivity"
class PostsActivity : AppCompatActivity() {

    private lateinit var fireStoreDB:FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_posts)

        fireStoreDB = FirebaseFirestore.getInstance()
        val postsReference = fireStoreDB.collection("Posts")
        postsReference.addSnapshotListener { snapshot, error ->
            if(error!=null || snapshot==null)
            {
                Log.i(TAG,"Error When Querying Posts")
                return@addSnapshotListener
            }
            val postList = snapshot.toObjects(Post::class.java)
            for(post in postList)
            {
                Log.i(TAG,"Post $post")
            }

        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_posts,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menu_logout)
        {
            val intent = Intent(this,ProfileActivity::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }
}