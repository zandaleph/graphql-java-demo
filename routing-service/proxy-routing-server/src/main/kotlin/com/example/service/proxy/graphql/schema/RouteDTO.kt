package com.example.service.proxy.graphql.schema

import com.example.graphql.schema.NodeDTO
import com.expediagroup.graphql.generator.annotations.GraphQLName

@GraphQLName("Route")
class RouteDTO(val prefix: String, val host: String) : NodeDTO(prefix)

