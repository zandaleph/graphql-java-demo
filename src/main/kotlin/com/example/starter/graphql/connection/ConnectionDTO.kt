package com.example.starter.graphql.connection

class ConnectionDTO<T>(
    val edges: List<EdgeDTO<T>>,
    val pageInfo: PageInfoDTO
)
