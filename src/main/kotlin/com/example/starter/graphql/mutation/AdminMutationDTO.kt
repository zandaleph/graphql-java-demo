package com.example.starter.graphql.mutation

import com.example.starter.db.TenantDao
import com.example.starter.db.entity.TenantEntity
import com.example.starter.graphql.query.TenantDTO
import graphql.schema.DataFetchingEnvironment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.future.asCompletableFuture
import org.hibernate.SessionFactory
import java.util.concurrent.CompletionStage
import javax.inject.Inject

class AdminMutationDTO @Inject constructor(
    private val sessionFactory: SessionFactory
) {

    fun getAddTenant(env: DataFetchingEnvironment): CompletionStage<AddTenantResultDTO> {
        return CoroutineScope(Dispatchers.IO).async {
            val input = env.getArgument<Map<String, String>>("input")
            val entity = TenantEntity(checkNotNull(input["name"]))
            val session = sessionFactory.openSession()
            val tenantDao = TenantDao(session)
            session.transaction.begin()
            tenantDao.createTenant(entity)
            session.transaction.commit()
            val tenant = TenantDTO(entity, sessionFactory)
            AddTenantResultDTO(tenant)
        }.asCompletableFuture()
    }
}
