package com.example.starter.db

import com.example.starter.db.entity.TenantEntity
import com.example.starter.db.entity.UserEntity
import com.example.starter.db.entity.UserEntity_
import org.hibernate.Session

class UserDao(private val session: Session) {

    fun listUsersForTenant(tenant: TenantEntity, first: Int = 10, after: String? = null): List<UserEntity> {
        val builder = session.criteriaBuilder
        val query = builder.createQuery(UserEntity::class.java)
        val root = query.from(UserEntity::class.java)
        query.select(root)
            .where(builder.equal(root[UserEntity_.tenant], tenant))
            .orderBy(builder.asc(root[UserEntity_.name]))
        after?.let { query.where(builder.greaterThan(root[UserEntity_.name], it)) }
        return session.createQuery(query).setMaxResults(first).resultList
    }

    fun createUser(user: UserEntity) {
        session.persist(user)
        session.flush()
    }
}
