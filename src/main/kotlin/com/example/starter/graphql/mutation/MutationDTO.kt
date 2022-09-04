package com.example.starter.graphql.mutation

import com.example.starter.db.TenantDao
import com.example.starter.graphql.node.NodeDTO
import com.example.starter.graphql.query.TenantComponent
import com.example.starter.graphql.query.toComponent
import graphql.schema.DataFetchingEnvironment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.future.asCompletableFuture
import org.hibernate.SessionFactory
import java.util.UUID
import java.util.concurrent.CompletionStage
import javax.inject.Inject
import javax.inject.Provider

class MutationDTO @Inject constructor(
    private val sessionFactory: SessionFactory,
    val adminMutation: AdminMutationDTO,
    private val tenantComponentProvider: Provider<TenantComponent.Builder>
) {

    fun getTenantMutation(env: DataFetchingEnvironment): CompletionStage<TenantMutationDTO> {
        return CoroutineScope(Dispatchers.IO).async {
            val graphQlId = env.getArgument<String>("tenantId")
            val (_, tenantId) = NodeDTO.parseId(graphQlId)
            sessionFactory.fromTransaction { TenantDao(it).getTenant(UUID.fromString(tenantId)) }
                ?.toComponent(tenantComponentProvider)?.tenantMutationDto()
                ?: throw NoSuchElementException(graphQlId)
        }.asCompletableFuture()
    }
}
