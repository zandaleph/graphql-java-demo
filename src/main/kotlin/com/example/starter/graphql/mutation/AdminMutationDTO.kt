package com.example.starter.graphql.mutation

import com.example.starter.db.TenantDao
import com.example.starter.db.entity.TenantEntity
import com.example.starter.graphql.query.TenantComponent
import com.example.starter.graphql.query.toComponent
import graphql.schema.DataFetchingEnvironment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.future.asCompletableFuture
import org.hibernate.SessionFactory
import java.util.concurrent.CompletionStage
import javax.inject.Inject
import javax.inject.Provider

class AdminMutationDTO @Inject constructor(
    private val sessionFactory: SessionFactory,
    private val tenantComponentProvider: Provider<TenantComponent.Builder>
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
            AddTenantResultDTO(entity.toComponent(tenantComponentProvider).tenantDto())
        }.asCompletableFuture()
    }
}
