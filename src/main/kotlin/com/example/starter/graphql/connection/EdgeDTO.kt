package com.example.starter.graphql.connection

class EdgeDTO<T>(
    val node: T,
    val cursor: String
)
