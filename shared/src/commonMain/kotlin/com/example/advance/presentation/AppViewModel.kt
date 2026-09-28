package com.example.advance.presentation


import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.example.advance.data.common.Result
import com.example.advance.data.common.onFailure
import com.example.advance.data.common.onSuccess
import com.example.advance.data.posts.model.requests.NewPost
import com.example.advance.data.posts.model.responses.Reactions
import com.example.advance.domain.posts.create.CreatePostUseCase
import com.example.advance.domain.posts.edit.EditPostUseCase
import com.example.advance.domain.posts.obtain.ObtainPostsUseCase
import com.example.advance.domain.posts.remove.RemovePostUseCase
import kotlin.time.Duration.Companion.milliseconds

@Stable
class AppViewModel internal constructor(
    private val createPostUseCase: CreatePostUseCase,
    private val editPostUseCase: EditPostUseCase,
    private val obtainPostsUseCase: ObtainPostsUseCase,
    private val removePostUseCase: RemovePostUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(AppState())
    internal val state: StateFlow<AppState> = _state.asStateFlow()

    private val _events = Channel<AppEvent>(capacity = Channel.BUFFERED)
    val events: Flow<AppEvent> = _events.receiveAsFlow()

    init {
        fetchPosts()
    }

    fun onAction(action: AppAction) {
        when (action) {
            AppAction.OnFetchPosts -> fetchPosts()
            AppAction.OnCreatePost -> createPost()
            AppAction.OnUpdatePost -> updatePost()
            AppAction.OnDeletePost -> deletePost()
        }
    }

    private fun fetchPosts() {
        toggleProgressVisibility()
        viewModelScope.launch {
            resetPreviousResults()
            delay(350.milliseconds)
            obtainPostsUseCase()
                .onSuccess { result ->
                    _state.update {
                        it.copy(
                            posts = result.posts,
                            result = result.toString()
                        )
                    }
                }.onFailure { errorMessage ->
                    _events.trySend(AppEvent.ShowGetErrorSnackbar(errorMessage))
            }
            toggleProgressVisibility()
        }
    }

        private fun createPost() {
        toggleProgressVisibility()
        viewModelScope.launch {
            resetPreviousResults()
            createPostUseCase(createNewPost())
                .onSuccess { result ->
                    _state.update {
                        it.copy(result = result)
                    }
                }.onFailure { errorMessage ->
                    _events.trySend(AppEvent.ShowPostErrorSnackbar(errorMessage))

                }
            toggleProgressVisibility()
        }
    }

        private fun updatePost() {
        toggleProgressVisibility()
        viewModelScope.launch {
            resetPreviousResults()
            delay(350.milliseconds)
            editPostUseCase(_state.value.posts.first().copy(body = "Updated body"))
                .onSuccess { result ->
                    _state.update {
                        it.copy(
                            result = result
                        )
                    }
                    toggleProgressVisibility()
                }
                .onFailure { errorMessage ->
                    _events.trySend(AppEvent.ShowPutErrorSnackbar(errorMessage))
                    toggleProgressVisibility()
                }
        }
    }

        private fun deletePost() {
        toggleProgressVisibility()
        viewModelScope.launch {
            resetPreviousResults()
            delay(350.milliseconds)
            when (val result = removePostUseCase(_state.value.posts.first().id)) {
                is Result.Success -> {
                    _state.update {
                        it.copy(
                            result = result.data
                        )
                    }
                    toggleProgressVisibility()
                }
                is Result.Failure -> {
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