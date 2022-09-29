package com.example.starter.graphql.query

import com.example.graphql.schema.PageInfoDTO
import com.example.graphql.util.ConnectionDTO

class DomainConnectionDTO(edges: List<DomainEdgeDTO>, pageInfo: PageInfoDTO) :
    ConnectionDTO<DomainDTO, DomainEdgeDTO>(edges, pageInfo) {
    companion object {
        fun fromList(items: List<DomainDTO>, first: Int): DomainConnectionDTO =
            fromList(items, first) { DomainEdgeDTO(it) }.let { (edges, pageInfo) ->
                DomainConnectionDTO(edges, pageInfo)
            }
    }
}