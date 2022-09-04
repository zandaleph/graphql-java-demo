package com.example.starter.graphql.query

import com.example.starter.db.TenantDao
import com.example.starter.graphql.connection.ConnectionDTO
import com.example.starter.graphql.connection.EdgeDTO
import com.example.starter.graphql.connection.PageInfoDTO
import com.example.starter.graphql.node.NodeDTO
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

class QueryDTO @Inject constructor(
    private val sessionFactory: SessionFactory,
    private val tenantComponentProvider: Provider<TenantComponent.Builder>
) {
    val hello = "world"

    fun getTenant(env: DataFetchingEnvironment): CompletionStage<TenantDTO> {
        return CoroutineScope(Dispatchers.IO).async {
            val graphQlId = env.getArgument<String>("tenantId")
            val (_, tenantId) = NodeDTO.parseId(graphQlId)
            val session = sessionFactory.openSession()
            val tenantDao = TenantDao(session)
            tenantDao.getTenant(UUID.fromString(tenantId))?.toComponent(tenantComponentProvider)?.tenantDto()
                ?: throw NoSuchElementException(graphQlId)
        }.asCompletableFuture()
    }

    fun getTenants(env: DataFetchingEnvironment): CompletionStage<ConnectionDTO<TenantDTO>> {
        return CoroutineScope(Dispatchers.IO).async {
            val first = env.getArgument<Int>("first")
            val after = env.getArgument<String>("after")
            val session = sessionFactory.openSession()
            val tenantDao = TenantDao(session)
            val tenants = tenantDao.listTenants(first + 1, after?.let { UUID.fromString(it) })
            ConnectionDTO(
                tenants.take(first)
                    .map { EdgeDTO(it.toComponent(tenantComponentProvider).tenantDto(), it.id.toString()) },
                PageInfoDTO(
                    hasNextPage = tenants.size > first,
                    endCursor = tenants.take(first).lastOrNull()?.id?.toString() ?: ""
                )
            )
        }.asCompletableFuture()
    }
}
