package com.example.graphql.util

import com.example.graphql.schema.PageInfoDTO

open class ConnectionDTO<T, E : EdgeDTO<T>>(open val edges: List<E>, val pageInfo: PageInfoDTO) {
    companion object {
        @JvmStatic
        protected fun <T, N, E : EdgeDTO<N>> fromList(
            items: List<T> = listOf(),
            first: Int,
            edgeFunc: (item: T) -> E,
        ): Pair<List<E>, PageInfoDTO> {
            val edges = items.take(first).map(edgeFunc)
            val pageInfo = PageInfoDTO(
                hasNextPage = items.size > first,
                startCursor = edges.firstOrNull()?.cursor ?: "",
                endCursor = edges.lastOrNull()?.cursor ?: ""
            )
            return edges to pageInfo
        }
    }
}
