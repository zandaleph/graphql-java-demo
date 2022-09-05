package com.example.starter.graphql.mutation

import com.example.starter.db.DomainDao
import com.example.starter.db.UserDao
import com.example.starter.db.entity.DomainEntity
import com.example.starter.db.entity.TenantEntity
import com.example.starter.db.entity.UserEntity
import com.example.starter.graphql.fetchData
import com.example.starter.graphql.query.DomainDTO
import com.example.starter.graphql.query.UserDTO
import graphql.schema.DataFetchingEnvironment
import org.hibernate.SessionFactory
import javax.inject.Inject

class TenantMutationDTO @Inject constructor(
    private val tenant: TenantEntity,
    private val sessionFactory: SessionFactory
) {

    fun getAddUser(env: DataFetchingEnvironment) = fetchData {
        val input = env.getArgument<Map<String, String>>("input")
        val entity = UserEntity(tenant, checkNotNull(input["name"]))
        sessionFactory.fromTransaction { UserDao(it).createUser(entity) }
        AddUserResultDTO(UserDTO(entity))
    }

    fun getAddDomain(env: DataFetchingEnvironment) = fetchData {
        val input = env.getArgument<Map<String, Any>>("input")
        val entity = DomainEntity(
            tenant,
            checkNotNull(input["domainName"] as? String),
            checkNotNull(input["enabled"] as? Boolean)
        )
        sessionFactory.fromTransaction { DomainDao(it).createDomain(entity) }
        AddDomainResultDTO(DomainDTO(entity))
    }
}
