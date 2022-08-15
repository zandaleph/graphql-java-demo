package com.example.starter.db

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(
    indexes = [Index(name = "idx_user_tenant_username", columnList = "tenant,name", unique = true)]
)
class UserEntity(
    @Id
    @GeneratedValue
    var id: UUID?,
    @ManyToOne(fetch = FetchType.LAZY, optional = false) var tenant: TenantEntity?,
    @Column(nullable = false) var name: String?,
    @Suppress("UnusedPrivateMember") unused: Void?
) {
    constructor() : this(null, null, null, null)
    constructor(tenant: TenantEntity, name: String) : this(null, tenant, name, null)
}
