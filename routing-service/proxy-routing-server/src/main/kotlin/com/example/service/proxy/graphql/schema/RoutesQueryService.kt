package com.example.service.proxy.graphql.schema

import com.expediagroup.graphql.server.operations.Query

class RoutesQueryService : Query {

    companion object {
        private const val DEFAULT_PAGE_SIZE = 10

        private val routes = mapOf(
            "bar" to "localhost:9080",
            "bat" to "localhost:9080",
            "baz" to "localhost:9080",
            "foo" to "localhost:9080",
            "zool" to "localhost:12080",
            "zoom" to "localhost:10080",
            "zoop" to "localhost:11080",
        )
    }

    suspend fun routes(
        host: String? = null,
        first: Int? = null,
        after: String? = null,
    ): RouteConnectionDTO {
        val count = first ?: DEFAULT_PAGE_SIZE
        val routes = routes.toList()
            .chainIf(host) { h -> filter { it.second == h } }
            .sortedBy { it.first }
            .chainIf(after) { a -> dropWhile { it.first <= a } }
            .take(count + 1)
            .map { (prefix, host) -> RouteDTO(prefix, host) }
        return RouteConnectionDTO.fromList(routes, count)
    }

    private inline fun <T, U> T.chainIf(v: U?, action: T.(U) -> T) = v?.let { action(it) } ?: this
}