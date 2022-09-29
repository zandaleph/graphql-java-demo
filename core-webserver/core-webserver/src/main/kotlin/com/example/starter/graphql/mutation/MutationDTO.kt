package com.example.starter.graphql.mutation

import com.example.graphql.schema.NodeDTO
import com.example.starter.db.TenantDao
import com.example.starter.graphql.fetchData
import com.example.starter.graphql.query.TenantComponent
import com.example.starter.graphql.query.toComponent
import graphql.schema.DataFetchingEnvironment
import org.hibernate.SessionFactory
import java.util.UUID
import javax.inject.Inject
import javax.inject.Provider

class MutationDTO @Inject constructor(
    private val sessionFactory: SessionFactory,
    val adminMutation: AdminMutationDTO,
    private val tenantComponentProvider: Provider<TenantComponent.Builder>,
) {

    fun getTenantMutation(env: DataFetchingEnvironment) = fetchData {
        val graphQlId = env.getArgument<String>("tenantId")
        val (_, tenantId) = NodeDTO.parseId(graphQlId)
        sessionFactory.fromTransaction { TenantDao(it).getTenant(UUID.fromString(tenantId)) }
            ?.toComponent(tenantComponentProvider)?.tenantMutationDto()
            ?: throw NoSuchElementException(graphQlId)
    }
}
