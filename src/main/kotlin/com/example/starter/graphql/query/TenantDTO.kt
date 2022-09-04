package com.example.starter.graphql.query

import com.example.starter.db.UserDao
import com.example.starter.db.entity.TenantEntity
import com.example.starter.graphql.connection.ConnectionDTO
import com.example.starter.graphql.fetchData
import com.example.starter.graphql.node.NodeDTO
import graphql.schema.DataFetchingEnvironment
import kotlinx.coroutines.delay
import org.hibernate.SessionFactory
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

class TenantDTO @Inject constructor(
    private val tenant: TenantEntity,
    private val sessionFactory: SessionFactory
) : NodeDTO(checkNotNull(tenant.id).toString()) {

    fun getName() = checkNotNull(tenant.name)

    fun getUsers(env: DataFetchingEnvironment) = fetchData {
        val first = env.getArgument<Int>("first")
        val after = env.getArgument<String>("after")
        val users = sessionFactory.fromTransaction { UserDao(it).listUsersForTenant(tenant, first + 1, after) }
        ConnectionDTO.fromList(users, first, { u -> checkNotNull(u.name) }) { UserDTO(it) }
    }

    fun getDomains(env: DataFetchingEnvironment) = fetchData {
        // placeholder
        delay(500.milliseconds)
        val domains = listOf(DomainDTO("1", "${tenant.name}.example.com", true))
        ConnectionDTO.fromList(domains, 10, { d -> d.id }) { it }
    }
}
