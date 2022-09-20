package com.example.starter.db

import com.example.starter.db.entity.DomainEntity
import com.example.starter.db.entity.DomainEntity_
import com.example.starter.db.entity.TenantEntity
import org.hibernate.Session

class DomainDao(private val session: Session) {

    fun listDomainsForTenant(tenant: TenantEntity, first: Int = 10, after: String? = null): List<DomainEntity> {
        val builder = session.criteriaBuilder
        val query = builder.createQuery(DomainEntity::class.java)
        val root = query.from(DomainEntity::class.java)
        query.select(root)
            .where(builder.equal(root[DomainEntity_.tenant], tenant))
            .orderBy(builder.asc(root[DomainEntity_.domainName]))
        after?.let { query.where(builder.greaterThan(root[DomainEntity_.domainName], it)) }
        return session.createQuery(query).setMaxResults(first).resultList
    }

    fun createDomain(domain: DomainEntity) {
        session.persist(domain)
        session.flush()
    }
}
