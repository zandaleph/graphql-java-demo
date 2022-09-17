package com.example.starter.db

import com.example.starter.db.entity.DomainEntity
import com.example.starter.db.entity.TenantEntity
import org.hibernate.Session

class DomainDao(private val session: Session) {
    fun listDomainsForTenant(tenant: TenantEntity, first: Int = 10, after: String? = null): List<DomainEntity> {
        val query = if (after == null) {
            session.createSelectionQuery(
                "SELECT d FROM DomainEntity d WHERE d.tenant = :tenant ORDER BY d.domainName",
                DomainEntity::class.java
            )
        } else {
            session.createSelectionQuery(
                "SELECT d FROM DomainEntity d WHERE d.tenant = :tenant AND d.domainName > :after ORDER BY d.domainName",
                DomainEntity::class.java
            ).setParameter("after", after)
        }
        return query.setParameter("tenant", tenant).setMaxResults(first).resultList
    }

    fun createDomain(domain: DomainEntity) {
        session.persist(domain)
        session.flush()
    }
}
