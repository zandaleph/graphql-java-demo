package com.example.starter.graphql.query

import com.example.graphql.schema.PageInfoDTO
import com.example.graphql.util.ConnectionDTO

class UserConnectionDTO(edges: List<UserEdgeDTO>, pageInfo: PageInfoDTO) :
    ConnectionDTO<UserDTO, UserEdgeDTO>(edges, pageInfo) {
    companion object {
        fun fromList(items: List<UserDTO>, first: Int): UserConnectionDTO =
            fromList(items, first) { UserEdgeDTO(it) }.let { (edges, pageInfo) ->
                UserConnectionDTO(edges, pageInfo)
            }
    }
}