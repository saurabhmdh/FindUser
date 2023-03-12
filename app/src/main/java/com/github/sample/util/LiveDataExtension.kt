package com.github.sample.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.channelFlow

@OptIn(ExperimentalCoroutinesApi::class)
fun <T> LiveData<T>.asFlow() = channelFlow {
    trySend(value)
    val observer = Observer<T> {t -> trySend(t)}
    observeForever(observer)
    invokeOnClose {
        removeObserver(observer)
    }
}