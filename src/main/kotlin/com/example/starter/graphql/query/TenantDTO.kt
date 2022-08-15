package com.example.starter.graphql.query

import com.example.starter.db.TenantEntity
import com.example.starter.db.UserDao
import com.example.starter.graphql.connection.ConnectionDTO
import com.example.starter.graphql.connection.EdgeDTO
import com.example.starter.graphql.connection.PageInfoDTO
import com.example.starter.graphql.node.NodeDTO
import graphql.schema.DataFetchingEnvironment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.future.asCompletableFuture
import mu.KotlinLogging
import org.hibernate.SessionFactory
import java.util.concurrent.CompletionStage
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

class TenantDTO @Inject constructor(
    private val tenant: TenantEntity,
    private val sessionFactory: SessionFactory
) : NodeDTO(checkNotNull(tenant.id).toString()) {

    companion object {
        private val logger = KotlinLogging.logger { }
    }

    fun getName(): String {
        logger.info { "Getting name for id: ${tenant.id}" }
        return checkNotNull(tenant.name)
    }

    fun getUsers(env: DataFetchingEnvironment): CompletionStage<ConnectionDTO<UserDTO>> {
        return CoroutineScope(Dispatchers.IO).async {
            val first = env.getArgument<Int>("first")
            val after = env.getArgument<String>("after")
            val session = sessionFactory.openSession()
            val userDao = UserDao(session)
            val users = userDao.listUsersForTenant(tenant, first + 1, after)
            ConnectionDTO(
                users.take(first).map { EdgeDTO(UserDTO(it), it.name.toString()) },
                PageInfoDTO(
                    hasNextPage = users.size > first,
                    endCursor = users.take(first).lastOrNull()?.name?.toString() ?: ""
                )
            )
        }.asCompletableFuture()
    }

    fun getDomains(env: DataFetchingEnvironment): CompletionStage<ConnectionDTO<DomainDTO>> {
        return CoroutineScope(Dispatchers.IO).async {
            // placeholder
            delay(500.milliseconds)
            ConnectionDTO(
                listOf(
                    EdgeDTO(DomainDTO("1", "${tenant.name}.example.com", true), "1")
                ),
                PageInfoDTO()
            )
        }.asCompletableFuture()
    }
}
