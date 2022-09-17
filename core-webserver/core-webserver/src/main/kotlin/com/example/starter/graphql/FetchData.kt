package com.example.starter.graphql

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.future.asCompletableFuture
import java.util.concurrent.CompletionStage

fun <T> fetchData(block: suspend CoroutineScope.() -> T): CompletionStage<T> =
    CoroutineScope(Dispatchers.IO).async(block = block).asCompletableFuture()
