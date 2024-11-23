package com.example.enginifyassignment

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Update

@Entity(tableName = "posts")
data class Post(
    @PrimaryKey val id: Int,
    val userId: Int,
    val title: String,
    val body: String
)

@Dao
interface PostDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(posts: List<Post>)

    @Query("SELECT * FROM posts")
    fun getAllPosts(): LiveData<List<Post>>

    @Query("DELETE FROM posts")
    suspend fun deleteAll()

    @Query("DELETE FROM posts WHERE id = :id")
    suspend fun deletePostById(id: Int)

    @Update
    suspend fun updatePost(post: Post)
}
