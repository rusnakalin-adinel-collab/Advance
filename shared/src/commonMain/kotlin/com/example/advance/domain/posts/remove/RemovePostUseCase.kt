package com.example.advance.domain.posts.remove

import com.example.advance.domain.posts.PostRepository
import com.example.advance.data.common.Result


internal class RemovePostUseCase(
    private val postRepository: PostRepository
) {
    suspend operator fun invoke(postId: Int): Result<String> {
        return postRepository.deletePost(postId)
    }
}