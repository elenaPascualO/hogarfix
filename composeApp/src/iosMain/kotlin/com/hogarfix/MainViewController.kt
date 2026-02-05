package com.hogarfix

import androidx.compose.ui.window.ComposeUIViewController
import com.hogarfix.di.initKoin

fun MainViewController() = ComposeUIViewController {
    App()
}

fun initKoinIos() {
    initKoin()
}