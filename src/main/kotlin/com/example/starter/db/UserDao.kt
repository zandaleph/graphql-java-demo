package com.example.starter.db

import org.hibernate.Session

class UserDao(private val session: Session) {
    fun listUsersForTenant(tenant: TenantEntity, first: Int = 10, after: String? = null): List<UserEntity> {
        val query = if (after == null) {
            session.createSelectionQuery(
                "SELECT ue FROM UserEntity ue WHERE ue.tenant = :tenant ORDER BY ue.name",
                UserEntity::class.java
            )
        } else {
            session.createSelectionQuery(
                "SELECT ue FROM UserEntity ue WHERE ue.tenant = :tenant AND ue.name > :after ORDER BY ue.name",
                UserEntity::class.java
            ).setParameter("after", after)
        }
        return query.setMaxResults(first).resultList
    }

    fun createUser(user: UserEntity) {
        session.persist(user)
        session.flush()
    }
}
