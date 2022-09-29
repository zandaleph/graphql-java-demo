package com.example.starter.graphql.query

import com.example.graphql.schema.NodeDTO
import com.example.starter.db.TenantDao
import com.example.starter.graphql.fetchData
import graphql.schema.DataFetchingEnvironment
import org.hibernate.SessionFactory
import java.util.UUID
import javax.inject.Inject
import javax.inject.Provider

class QueryDTO @Inject constructor(
    private val sessionFactory: SessionFactory,
    private val tenantComponentProvider: Provider<TenantComponent.Builder>,
) {
    val hello = "world"

    fun getTenant(env: DataFetchingEnvironment) = fetchData {
        val graphQlId = env.getArgument<String>("tenantId")
        val (_, tenantId) = NodeDTO.parseId(graphQlId)
        sessionFactory.fromTransaction { TenantDao(it).getTenant(UUID.fromString(tenantId)) }
            ?.toComponent(tenantComponentProvider)?.tenantDto()
            ?: throw NoSuchElementException(graphQlId)
    }

    fun getTenants(env: DataFetchingEnvironment) = fetchData {
        val first = env.getArgument<Int>("first")
        val after = env.getArgument<String>("after")
        val tenants = sessionFactory.fromTransaction {
            TenantDao(it).listTenants(first + 1, after?.let { a -> UUID.fromString(a) })
        }
        TenantConnectionDTO.fromList(tenants.map { it.toComponent(tenantComponentProvider).tenantDto() }, first)
    }
}
