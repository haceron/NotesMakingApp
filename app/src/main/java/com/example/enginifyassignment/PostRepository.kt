package com.example.enginifyassignment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class PostRepository(private val postDao: PostDao) {

    val allPosts: LiveData<List<Post>> = postDao.getAllPosts()

    suspend fun fetchAndSavePosts() {
        val posts = RetrofitInstance.api.getPosts()
        postDao.insertAll(posts)
    }

    suspend fun deletePost(id: Int) {
        postDao.deletePostById(id)
    }

    suspend fun updatePost(post: Post) {
        postDao.updatePost(post)
    }

    suspend fun deleteAll() {
        postDao.deleteAll()
    }
}

class PostViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PostRepository

    val allPosts: LiveData<List<Post>>

    init {
        val postDao = PostDatabase.getDatabase(application).postDao()
        repository = PostRepository(postDao)
        allPosts = repository.allPosts
    }

    fun fetchAndSavePosts() = viewModelScope.launch {
        repository.fetchAndSavePosts()
    }

    fun deletePost(id: Int) = viewModelScope.launch {
        repository.deletePost(id)
    }

    fun updatePost(post: Post) = viewModelScope.launch {
        repository.updatePost(post)
    }

    fun deleteAll() = viewModelScope.launch {
        repository.deleteAll()
    }
}
