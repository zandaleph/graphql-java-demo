package com.example.starter.graphql.query

import com.example.starter.db.TenantDao
import com.example.starter.db.TenantEntity
import com.example.starter.graphql.connection.ConnectionDTO
import com.example.starter.graphql.connection.EdgeDTO
import com.example.starter.graphql.connection.PageInfoDTO
import com.example.starter.graphql.node.NodeDTO
import graphql.schema.DataFetchingEnvironment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.future.asCompletableFuture
import mu.KotlinLogging
import org.hibernate.SessionFactory
import java.util.UUID
import java.util.concurrent.CompletionStage
import javax.inject.Inject
import javax.inject.Provider

class QueryDTO @Inject constructor(
    private val sessionFactory: SessionFactory,
    private val tenantComponentProvider: Provider<TenantComponent.Builder>
) {

    companion object {
        private val logger = KotlinLogging.logger { }
    }

    val hello = "world"

    fun getTenant(env: DataFetchingEnvironment): CompletionStage<TenantDTO> {
        return CoroutineScope(Dispatchers.IO).async {
            val graphQlId = env.getArgument<String>("tenantId")
            val (_, tenantId) = NodeDTO.parseId(graphQlId)
            val id = UUID.fromString(tenantId)
            val session = sessionFactory.openSession()
            val tenantDao = TenantDao(session)
            logger.info { "About to call tenantDao.getTenant($id), session: $session" }
            val tenant =
                tenantDao.getTenant(UUID.fromString(tenantId)) ?: throw NoSuchElementException(graphQlId)
            tenantEntityToDTO(tenant)
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
                tenants.take(first).map { EdgeDTO(tenantEntityToDTO(it), it.id.toString()) },
                PageInfoDTO(
                    hasNextPage = tenants.size > first,
                    endCursor = tenants.take(first).lastOrNull()?.id?.toString() ?: ""
                )
            )
        }.asCompletableFuture()
    }

    private fun tenantEntityToDTO(entity: TenantEntity): TenantDTO {
        val tenantModule = TenantModule(entity)
        val tenantComponent = tenantComponentProvider.get().tenantModule(tenantModule).build()
        return tenantComponent.tenantDto()
    }
}
