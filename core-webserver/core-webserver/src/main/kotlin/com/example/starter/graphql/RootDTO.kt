package com.example.starter.graphql

import com.example.starter.graphql.mutation.AdminMutationDTO
import com.example.starter.graphql.mutation.MutationComponent
import com.example.starter.graphql.mutation.MutationModule
import com.example.starter.graphql.mutation.TenantMutationDTO
import com.example.starter.graphql.query.QueryComponent
import com.example.starter.graphql.query.QueryModule
import com.example.starter.graphql.query.TenantConnectionDTO
import com.example.starter.graphql.query.TenantDTO
import graphql.schema.DataFetchingEnvironment
import java.util.concurrent.CompletionStage
import javax.inject.Inject
import javax.inject.Provider

class RootDTO @Inject constructor(
    private val queryComponentProvider: Provider<QueryComponent.Builder>,
    private val mutationComponentProvider: Provider<MutationComponent.Builder>,
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

    fun getTenants(env: DataFetchingEnvironment): CompletionStage<TenantConnectionDTO> {
        val queryDTO = queryComponentProvider.get().queryModule(QueryModule()).build().queryDTO()
        return queryDTO.getTenants(env)
    }

    // Mutation
    fun getTenantMutation(env: DataFetchingEnvironment): CompletionStage<TenantMutationDTO> {
        val mutationDTO = mutationComponentProvider.get().mutationModule(MutationModule()).build().mutationDTO()
        return mutationDTO.getTenantMutation(env)
    }

    fun getAdminMutation(): AdminMutationDTO {
        val mutationDTO = mutationComponentProvider.get().mutationModule(MutationModule()).build().mutationDTO()
        return mutationDTO.adminMutation
    }
}
