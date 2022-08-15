package com.example.starter.db

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import java.util.UUID

@Entity
class TenantEntity private constructor(
    @Id @GeneratedValue
    var id: UUID?,
    @Column(nullable = false) var name: String?,
    @Suppress("UnusedPrivateMember") unused: Void?
) {
    constructor() : this(null, null, null)
    constructor(name: String) : this(null, name, null)
}
