package com.example.service.proxy.graphql

import com.example.service.proxy.graphql.schema.RoutesQueryService
import com.expediagroup.graphql.generator.SchemaGeneratorConfig
import com.expediagroup.graphql.generator.TopLevelObject
import com.expediagroup.graphql.generator.scalars.IDValueUnboxer
import com.expediagroup.graphql.generator.toSchema
import graphql.GraphQL

class ProxyRoutingGraphQL {

    fun doThing(): GraphQL {
        val routesQueryService = RoutesQueryService()
        val schema = toSchema(
            config = SchemaGeneratorConfig(
                supportedPackages = listOf(
                    "com.example.service.proxy.graphql.schema",
                    "com.example.graphql.schema",
                )
            ),
            queries = listOf(TopLevelObject(routesQueryService))
        )
        return GraphQL.newGraphQL(schema).valueUnboxer(IDValueUnboxer()).build()
    }
}