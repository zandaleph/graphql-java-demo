package com.example.starter.graphql.mutation

import com.example.starter.db.UserDao
import com.example.starter.db.entity.TenantEntity
import com.example.starter.db.entity.UserEntity
import com.example.starter.graphql.query.UserDTO
import graphql.schema.DataFetchingEnvironment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.future.asCompletableFuture
import org.hibernate.SessionFactory
import java.util.concurrent.CompletionStage
import javax.inject.Inject

class TenantMutationDTO @Inject constructor(
    private val tenant: TenantEntity,
    private val sessionFactory: SessionFactory
) {

    fun getAddUser(env: DataFetchingEnvironment): CompletionStage<AddUserResultDTO> {
        return CoroutineScope(Dispatchers.IO).async {
            val input = env.getArgument<Map<String, String>>("input")
            val entity = UserEntity(tenant, checkNotNull(input["name"]))
            sessionFactory.fromTransaction { UserDao(it).createUser(entity) }
            AddUserResultDTO(UserDTO(entity))
        }.asCompletableFuture()
    }
}
