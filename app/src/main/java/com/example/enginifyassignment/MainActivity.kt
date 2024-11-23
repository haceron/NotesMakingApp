package com.example.enginifyassignment

import android.os.Bundle
import android.widget.EditText

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView

import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.enginifyAssignment.R


class MainActivity : AppCompatActivity() {

    private lateinit var postViewModel: PostViewModel
    private lateinit var adapter: PostAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val swipeRefreshLayout = findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayout)
        val searchView = findViewById<SearchView>(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Handle search on submit if needed
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val filteredList = postViewModel.allPosts.value?.filter {
                    it.title.contains(newText ?: "", ignoreCase = true) ||
                            it.body.contains(newText ?: "", ignoreCase = true)
                }
               adapter.updatePost(filteredList ?: emptyList())
                return true
            }
        })


        postViewModel = ViewModelProvider(this).get(PostViewModel::class.java)

        postViewModel.allPosts.observe(this) { posts ->
            adapter = PostAdapter(posts, { post ->
                postViewModel.deletePost(post.id)
            }, { post ->  showEditDialog(post)
            })
            recyclerView.adapter = adapter
        }

        swipeRefreshLayout.setOnRefreshListener {
            postViewModel.deleteAll()
            postViewModel.fetchAndSavePosts()
            swipeRefreshLayout.isRefreshing = false
        }

        postViewModel.fetchAndSavePosts()
    }

    private fun showEditDialog(post: Post) {
        val dialog = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.dialog_edit_post, null)
        dialog.setView(dialogView)

        val titleInput = dialogView.findViewById<EditText>(R.id.editTitle)
        val bodyInput = dialogView.findViewById<EditText>(R.id.editBody)

        titleInput.setText(post.title)
        bodyInput.setText(post.body)

        dialog.setPositiveButton("Save") { _, _ ->
            val updatedPost = post.copy(
                title = titleInput.text.toString(),
                body = bodyInput.text.toString()
            )
            postViewModel.updatePost(updatedPost)
        }

        dialog.setNegativeButton("Cancel", null)
        dialog.show()
    }
}
