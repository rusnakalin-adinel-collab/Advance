package com.example.advance.presentation


import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.example.advance.data.common.NetworkResult
import com.example.advance.data.posts.model.requests.NewPost
import com.example.advance.data.posts.model.responses.Reactions
import com.example.advance.domain.posts.PostRepository
import kotlin.time.Duration.Companion.milliseconds

@Stable
class AppViewModel internal constructor(
    private val postRepository: PostRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AppState())
    internal val state: StateFlow<AppState> = _state.asStateFlow()

    init {
        fetchPosts()
    }

    internal fun fetchPosts() {
        toggleProgressVisibility()
        viewModelScope.launch {
            resetPreviousResults()
            delay(350.milliseconds)
            when (val result = postRepository.getAllPosts()) {
                is NetworkResult.Success -> {
                    _state.update {
                        it.copy(
                            posts = result.data.posts,
                            result = result.data.toString()
                        )
                    }
                    toggleProgressVisibility()
                }
                is NetworkResult.Failure -> {
                    _state.update { it.copy(error = result.errorMessage) }
                    toggleProgressVisibility()
                }
            }
        }
    }

    internal fun createPost() {
        toggleProgressVisibility()
        viewModelScope.launch {
            resetPreviousResults()
            delay(350.milliseconds)
            when (val result = postRepository.addPost(createNewPost())) {
                is NetworkResult.Success -> {
                    _state.update {
                        it.copy(
                            result = result.data.toString()
                        )
                    }
                    toggleProgressVisibility()
                }
                is NetworkResult.Failure -> {
                    _state.update { it.copy(error = result.errorMessage) }
                    toggleProgressVisibility()
                }
            }
        }
    }

    internal fun updatePost() {
        toggleProgressVisibility()
        viewModelScope.launch {
            resetPreviousResults()
            delay(350.milliseconds)
            when (val result = postRepository.updatePost(_state.value.posts.first().copy(body = "Updated body"))) {
                is NetworkResult.Success -> {
                    _state.update {
                        it.copy(
                            result = result.data.toString()
                        )
                    }
                    toggleProgressVisibility()
                }
                is NetworkResult.Failure -> {
                    _state.update { it.copy(error = result.errorMessage) }
                    toggleProgressVisibility()
                }
            }
        }
    }

    internal fun deletePost() {
        toggleProgressVisibility()
        viewModelScope.launch {
            resetPreviousResults()
            delay(350.milliseconds)
            when (val result = postRepository.deletePost(_state.value.posts.first().id)) {
                is NetworkResult.Success -> {
                    _state.update {
                        it.copy(
                            result = result.data.toString()
                        )
                    }
                    toggleProgressVisibility()
                }
                is NetworkResult.Failure -> {
                    _state.update { it.copy(error = result.errorMessage) }
                    toggleProgressVisibility()
                }
            }
        }
    }

    private fun toggleProgressVisibility() {
        _state.update { it.copy(isProgressVisible = !it.isProgressVisible) }
    }

    private fun resetPreviousResults() {
        _state.update { it.copy(result = null) }
        _state.update { it.copy(error = null) }
    }

    private fun createNewPost(): NewPost {
        return NewPost(
            body = "Body text",
            reactions = Reactions(),
            tags = listOf("Tag 1", "Tag 2"),
            title = "Title text",
            userId = 5,
        )
    }
}