package com.example.advance.presentation

import androidx.compose.runtime.Immutable
import com.example.advance.data.posts.model.responses.Post

@Immutable
internal data class AppState(
    val isProgressVisible: Boolean = false,
    val posts: List<Post> = emptyList(),
    val result: String? = null,
    val error: String? = null,
)