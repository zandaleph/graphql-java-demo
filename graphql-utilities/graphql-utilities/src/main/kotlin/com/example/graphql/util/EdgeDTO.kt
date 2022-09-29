package com.example.graphql.util

//@Suppress("MemberVisibilityCanBePrivate")
open class EdgeDTO<T>(
    val node: T,
    private val cursorFunc: (T) -> String,
) {

    val cursor by lazy { cursorFunc(node) }
}
