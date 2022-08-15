package com.example.starter.graphql

import com.example.starter.graphql.connection.ConnectionDTO
import com.example.starter.graphql.mutation.MutationDTO
import com.example.starter.graphql.mutation.TenantMutationDTO
import com.example.starter.graphql.query.QueryComponent
import com.example.starter.graphql.query.QueryModule
import com.example.starter.graphql.query.TenantDTO
import graphql.schema.DataFetchingEnvironment
import java.util.concurrent.CompletionStage
import javax.inject.Inject
import javax.inject.Provider

class RootDTO @Inject constructor(
    private val queryComponentProvider: Provider<QueryComponent.Builder>,
    private val mutationDTO: MutationDTO
) {

    // Query methods
    fun getHello(): String {
        val queryDTO = queryComponentProvider.get().queryModule(QueryModule()).build().queryDTO()
        return queryDTO.hello
    }

    fun getTenant(env: DataFetchingEnvironment): CompletionStage<TenantDTO> {
        val queryDTO = queryComponentProvider.get().queryModule(QueryModule()).build().queryDTO()
        return queryDTO.getTenant(env)
    }

    fun getTenants(env: DataFetchingEnvironment): CompletionStage<ConnectionDTO<TenantDTO>> {
        val queryDTO = queryComponentProvider.get().queryModule(QueryModule()).build().queryDTO()
        return queryDTO.getTenants(env)
    }

    // Mutation
    fun getTenantMutation(env: DataFetchingEnvironment): CompletionStage<TenantMutationDTO> {
        return mutationDTO.getTenantMutation(env)
    }

    val adminMutation = mutationDTO.adminMutation
}
