package com.example.graphql.schema

import com.expediagroup.graphql.generator.annotations.GraphQLName

@GraphQLName("PageInfo")
class PageInfoDTO(
    val hasPreviousPage: Boolean = false,
    val hasNextPage: Boolean = false,
    val startCursor: String = "",
    val endCursor: String = "",
)
