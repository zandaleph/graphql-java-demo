package com.example.starter.graphql.connection

class PageInfoDTO(
    val hasPreviousPage: Boolean = false,
    val hasNextPage: Boolean = false,
    val startCursor: String = "",
    val endCursor: String = ""
)
