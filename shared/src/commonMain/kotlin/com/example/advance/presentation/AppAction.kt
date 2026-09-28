package com.example.advance.presentation

sealed class AppAction {
    data object OnFetchPosts : AppAction()
    data object OnCreatePost : AppAction()
    data object OnUpdatePost : AppAction()
    data object OnDeletePost : AppAction()
}