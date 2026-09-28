package com.example.advance.domain.posts.edit

import com.example.advance.data.posts.model.responses.Post
import com.example.advance.domain.posts.PostRepository
import com.example.advance.data.common.Result

internal class EditPostUseCase(
    private val postRepository: PostRepository
) {
    suspend operator fun invoke(post: Post): Result<String> {
        return postRepository.updatePost(post)
    }
}