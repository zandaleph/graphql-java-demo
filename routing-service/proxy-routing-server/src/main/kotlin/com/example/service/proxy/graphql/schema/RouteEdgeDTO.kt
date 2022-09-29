package com.example.service.proxy.graphql.schema

import com.example.graphql.util.EdgeDTO
import com.expediagroup.graphql.generator.annotations.GraphQLName

@GraphQLName("RouteEdge")
class RouteEdgeDTO(node: RouteDTO) : EdgeDTO<RouteDTO>(node, { it.prefix })