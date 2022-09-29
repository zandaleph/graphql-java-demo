package com.example.starter.graphql.query

import com.example.graphql.schema.PageInfoDTO
import com.example.graphql.util.ConnectionDTO

class TenantConnectionDTO(edges: List<TenantEdgeDTO>, pageInfo: PageInfoDTO) :
    ConnectionDTO<TenantDTO, TenantEdgeDTO>(edges, pageInfo) {
    companion object {
        fun fromList(items: List<TenantDTO>, first: Int): TenantConnectionDTO =
            fromList(items, first) { TenantEdgeDTO(it) }.let { (edges, pageInfo) ->
                TenantConnectionDTO(edges, pageInfo)
            }
    }
}