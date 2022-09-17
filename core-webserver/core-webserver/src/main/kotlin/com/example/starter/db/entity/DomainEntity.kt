package com.example.starter.db.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(
    name = "Domain",
    indexes = [Index(name = "idx_domain_tenant_domain_name", columnList = "tenant_id,domain_name", unique = true)]
)
class DomainEntity private constructor(
    @Id @GeneratedValue
    var id: UUID?,
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tenant_id", nullable = false, updatable = false)
    var tenant: TenantEntity?,
    @Column(nullable = false, name = "domain_name") var domainName: String?,
    @Column(nullable = false) var enabled: Boolean?,
    @Suppress("UnusedPrivateMember") unused: Void?
) {
    constructor() : this(null, null, null, null, null)
    constructor(tenant: TenantEntity, domainName: String, enabled: Boolean = true) : this(
        null,
        tenant,
        domainName,
        enabled,
        null
    )
}
