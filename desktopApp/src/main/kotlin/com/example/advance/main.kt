package com.example.advance

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.example.advance.di.initKoin
import com.example.advance.presentation.App

fun main() = application {
    initKoin { printLogger() }
    Window(
        onCloseRequest = ::exitApplication,
        title = "Advance",
    ) {
        App()
    }
}