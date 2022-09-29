package com.example.starter.graphql.mutation

import com.example.starter.db.TenantDao
import com.example.starter.db.entity.TenantEntity
import com.example.starter.graphql.fetchData
import com.example.starter.graphql.query.TenantComponent
import com.example.starter.graphql.query.toComponent
import graphql.schema.DataFetchingEnvironment
import org.hibernate.SessionFactory
import javax.inject.Inject
import javax.inject.Provider

class AdminMutationDTO @Inject constructor(
    private val sessionFactory: SessionFactory,
    private val tenantComponentProvider: Provider<TenantComponent.Builder>,
) {

    fun getAddTenant(env: DataFetchingEnvironment) = fetchData {
        val input = env.getArgument<Map<String, String>>("input")
        val entity = TenantEntity(checkNotNull(input["name"]))
        sessionFactory.fromTransaction { TenantDao(it).createTenant(entity) }
        AddTenantResultDTO(entity.toComponent(tenantComponentProvider).tenantDto())
    }
}
