package com.example.advance.domain.posts.obtain

import com.example.advance.domain.posts.PostRepository
import com.example.advance.data.common.Result
import com.example.advance.data.posts.model.responses.Posts

internal class ObtainPostsUseCase(
    private val postRepository: PostRepository
) {
    suspend operator fun invoke(): Result<Posts> {
        return postRepository.getAllPosts()
    }}