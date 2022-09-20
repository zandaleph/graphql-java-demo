package com.example.starter.db

import com.example.starter.db.entity.TenantEntity
import com.example.starter.db.entity.TenantEntity_
import org.hibernate.Session
import java.util.UUID

class TenantDao(private val session: Session) {

    fun getTenant(id: UUID): TenantEntity? {
        return session.find(TenantEntity::class.java, id)
    }

    fun listTenants(first: Int = 10, after: UUID? = null): List<TenantEntity> {
        val builder = session.criteriaBuilder
        val query = builder.createQuery(TenantEntity::class.java)
        val root = query.from(TenantEntity::class.java)
        query.select(root)
            .orderBy(builder.asc(root[TenantEntity_.id]))
        after?.let { query.where(builder.greaterThan(root[TenantEntity_.id], it)) }
        return session.createQuery(query).setMaxResults(first).resultList
    }

    fun createTenant(tenant: TenantEntity) {
        session.persist(tenant)
        session.flush()
    }
}
