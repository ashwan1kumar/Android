package com.krashwani0908.firegram

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.krashwani0908.firegram.models.Post
import com.krashwani0908.firegram.models.User

//import com.krashwani0908.firegram.models.Posts
const val EXTRA_USERNAME = "EXTRA_USERNAME"
private const val TAG = "PostsActivity"
open class PostsActivity : AppCompatActivity() {
    private var signedInUser: User? = null
    private lateinit var fireStoreDB:FirebaseFirestore
    private lateinit var firestoreDb: FirebaseFirestore
    private lateinit var posts:MutableList<Post>
    private lateinit var adapter:PostsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_posts)
        posts = mutableListOf()
        adapter = PostsAdapter(this ,posts)

        findViewById<RecyclerView>(R.id.rvPosts).adapter = adapter
        findViewById<RecyclerView>(R.id.rvPosts).layoutManager = LinearLayoutManager(this)

        fireStoreDB = FirebaseFirestore.getInstance()

        fireStoreDB.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid as String)
            .get()
            .addOnSuccessListener { userSnapshot ->
                signedInUser = userSnapshot.toObject(User::class.java)
                Log.i(TAG, "signed in user: $signedInUser")
            }
            .addOnFailureListener { exception ->
                Log.i(TAG, "Failure fetching signed in user", exception)
            }
        var postsReference = fireStoreDB
            .collection("posts")
            .limit(20)
            .orderBy("creation_time_ms",Query.Direction.DESCENDING)
        val username = intent.getStringExtra(EXTRA_USERNAME)
        if (username != null) {
            supportActionBar?.title = username
            postsReference = postsReference.whereEqualTo("user.username", username)
        }
        postsReference.addSnapshotListener { snapshot, error ->
            if(error!=null || snapshot==null)
            {
                Log.i(TAG,"Error When Querying Posts")
                return@addSnapshotListener
            }
            val postList = snapshot.toObjects(Post::class.java)
            posts.clear()
            posts.addAll(postList)
            adapter.notifyDataSetChanged()
            for(post in postList)
            {
                Log.i(TAG,"Post $post")
            }

        }
        findViewById<FloatingActionButton>(R.id.fabCreate).setOnClickListener {
            val intent  = Intent(this,CreateActivity::class.java)
            startActivity(intent)

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
            intent.putExtra(EXTRA_USERNAME, signedInUser?.username)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }
}