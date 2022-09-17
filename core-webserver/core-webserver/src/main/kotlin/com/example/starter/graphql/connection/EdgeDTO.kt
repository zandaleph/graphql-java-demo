package com.example.starter.graphql.connection

@Suppress("MemberVisibilityCanBePrivate")
class EdgeDTO<T>(
    val node: T,
    private val cursorFunc: (T) -> String
) {

    val cursor by lazy { cursorFunc(node) }
}
