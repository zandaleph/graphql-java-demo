package com.example.service.proxy.graphql.schema

import com.example.graphql.schema.PageInfoDTO
import com.example.graphql.util.ConnectionDTO
import com.expediagroup.graphql.generator.annotations.GraphQLName

@GraphQLName("RouteConnection")
class RouteConnectionDTO(override val edges: List<RouteEdgeDTO>, pageInfo: PageInfoDTO) :
    ConnectionDTO<RouteDTO, RouteEdgeDTO>(edges, pageInfo) {
    companion object {
        fun fromList(items: List<RouteDTO>, first: Int): RouteConnectionDTO =
            fromList(items, first) { RouteEdgeDTO(it) }.let { (edges, pageInfo) ->
                RouteConnectionDTO(edges, pageInfo)
            }
    }
}