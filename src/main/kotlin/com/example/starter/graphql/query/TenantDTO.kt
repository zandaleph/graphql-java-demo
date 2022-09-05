package com.example.starter.graphql.query

import com.example.starter.db.DomainDao
import com.example.starter.db.UserDao
import com.example.starter.db.entity.TenantEntity
import com.example.starter.graphql.connection.ConnectionDTO
import com.example.starter.graphql.fetchData
import com.example.starter.graphql.node.NodeDTO
import graphql.schema.DataFetchingEnvironment
import org.hibernate.SessionFactory
import javax.inject.Inject

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
        val first = env.getArgument<Int>("first")
        val after = env.getArgument<String>("after")
        val users = sessionFactory.fromTransaction { DomainDao(it).listDomainsForTenant(tenant, first + 1, after) }
        ConnectionDTO.fromList(users, first, { u -> checkNotNull(u.domainName) }) { DomainDTO(it) }
    }
}
