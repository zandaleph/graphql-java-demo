package com.example.starter.graphql

import com.example.starter.graphql.query.QueryComponent
import dagger.Module
import dagger.Provides
import graphql.GraphQL
import graphql.schema.idl.RuntimeWiring
import graphql.schema.idl.SchemaGenerator
import graphql.schema.idl.SchemaParser
import mu.KotlinLogging

@Module(subcomponents = [QueryComponent::class])
class GraphQLModule {

    companion object {
        val logger = KotlinLogging.logger { }
    }

    @Provides
    fun providesGraphQL(): GraphQL {
        logger.info("Providing graphql")
        val typeRegistry = SchemaParser().parse(javaClass.getResourceAsStream("schema.graphqls"))
        val wiring = RuntimeWiring.newRuntimeWiring()
            .build()
        val schema = SchemaGenerator().makeExecutableSchema(typeRegistry, wiring)
        return GraphQL.newGraphQL(schema).build()
    }
}
