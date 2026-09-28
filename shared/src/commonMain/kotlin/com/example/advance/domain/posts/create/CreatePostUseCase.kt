package com.example.advance.domain.posts.create

import com.example.advance.data.posts.model.requests.NewPost
import com.example.advance.domain.posts.PostRepository
import com.example.advance.data.common.Result

internal class CreatePostUseCase (
    private val postRepository: PostRepository
) {
    suspend operator fun invoke(post: NewPost): Result<String> {
        return postRepository.addPost(post)
    }
}