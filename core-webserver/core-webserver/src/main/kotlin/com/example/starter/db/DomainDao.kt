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
        val predicate = builder.equal(root[DomainEntity_.tenant], tenant).let { pred ->
            after?.let { builder.and(pred, builder.greaterThan(root[DomainEntity_.domainName], it)) } ?: pred
        }
        query.select(root)
            .where(predicate)
            .orderBy(builder.asc(root[DomainEntity_.domainName]))
        return session.createQuery(query).setMaxResults(first).resultList
    }

    fun createDomain(domain: DomainEntity) {
        session.persist(domain)
        session.flush()
    }
}
