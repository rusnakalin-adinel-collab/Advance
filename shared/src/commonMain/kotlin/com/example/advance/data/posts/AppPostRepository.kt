package com.example.advance.data.posts

import com.example.advance.data.common.Result
import com.example.advance.data.common.map
import com.example.advance.data.posts.model.requests.NewPost
import com.example.advance.data.posts.model.responses.Post
import com.example.advance.data.posts.model.responses.Posts
import com.example.advance.data.posts.service.PostApiService
import com.example.advance.domain.posts.PostRepository


internal class AppPostRepository(
    private val postApiService: PostApiService
) : PostRepository {

    override suspend fun getAllPosts(): Result<Posts> {
        return postApiService.getAllPosts()
    }

    override suspend fun addPost(post: NewPost): Result<String> {
        return postApiService.addPost(post).map {
            it.toString()
        }
    }

    override suspend fun updatePost(post: Post): Result<String> {
        return postApiService.updatePost(post).map {
            it.toString()
        }
    }

    override suspend fun deletePost(postId: Int): Result<String> {
        return postApiService.deletePost(postId).map {
            it.toString()
        }
    }
}