package com.krashwani0908.firegram

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.krashwani0908.firegram.models.Post
//import com.krashwani0908.firegram.models.Posts

private const val TAG = "PostsActivity"
class PostsActivity : AppCompatActivity() {
    private lateinit var fireStoreDB:FirebaseFirestore
    private lateinit var posts:MutableList<Post>
    private lateinit var adapter:PostsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_posts)
        posts = mutableListOf()
        adapter = PostsAdapter(this ,posts)

        fireStoreDB = FirebaseFirestore.getInstance()
        val postsReference = fireStoreDB
            .collection("posts")
            .limit(20)
            .orderBy("creation_time_ms",Query.Direction.DESCENDING)
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