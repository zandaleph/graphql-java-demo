package com.example.starter.db

import com.example.starter.db.entity.TenantEntity
import org.hibernate.Session
import java.util.UUID

class TenantDao(private val session: Session) {

    fun getTenant(id: UUID): TenantEntity? {
        return session.find(TenantEntity::class.java, id)
    }

    fun listTenants(first: Int = 10, after: UUID? = null): List<TenantEntity> {
        val query = if (after == null) {
            session.createSelectionQuery("SELECT te FROM TenantEntity te ORDER BY te.id", TenantEntity::class.java)
        } else {
            session.createSelectionQuery(
                "Select te from TenantEntity te where te.id > :after ORDER BY te.id",
                TenantEntity::class.java
            ).setParameter("after", after)
        }
        return query.setMaxResults(first).resultList
    }

    fun createTenant(tenant: TenantEntity) {
        session.persist(tenant)
        session.flush()
    }
}
