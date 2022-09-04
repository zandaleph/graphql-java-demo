package com.example.starter.graphql.connection

class ConnectionDTO<T>(
    val edges: List<EdgeDTO<T>>,
    val pageInfo: PageInfoDTO
) {
    companion object {
        fun <T, U> fromList(
            items: List<T> = listOf(),
            first: Int,
            cursorFunc: (item: T) -> String,
            nodeFunc: (item: T) -> U
        ): ConnectionDTO<U> =
            items.take(first).map { EdgeDTO(nodeFunc(it), cursorFunc(it)) }.let { edges ->
                ConnectionDTO(
                    edges,
                    PageInfoDTO(
                        hasNextPage = items.size > first,
                        startCursor = edges.firstOrNull()?.cursor ?: "",
                        endCursor = edges.lastOrNull()?.cursor ?: ""
                    )
                )
            }
    }
}
