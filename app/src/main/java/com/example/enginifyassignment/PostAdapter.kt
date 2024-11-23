package com.example.enginifyassignment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.enginifyAssignment.R

class PostAdapter(
    private var posts: List<Post>,
    private val onDelete: (Post) -> Unit,
    private val onEdit: (Post) -> Unit
) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    inner class PostViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val userId: TextView = view.findViewById(R.id.tvUserId)
        val title: TextView = view.findViewById(R.id.tvTitle)
        val body: TextView = view.findViewById(R.id.tvBody)
        val deleteButton: Button = view.findViewById(R.id.btnDelete)

        fun bind(post: Post) {
            userId.text = "User ID: ${post.userId}"
            title.text = post.title
            body.text = post.body
            deleteButton.setOnClickListener { onDelete(post) }
            itemView.setOnClickListener { onEdit(post) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(posts[position])
    }

    override fun getItemCount(): Int = posts.size

    // Method to update the posts list and refresh the RecyclerView
    fun updatePost(newPosts: List<Post>) {
        posts = newPosts
        notifyDataSetChanged() // Refresh the RecyclerView
    }

}
