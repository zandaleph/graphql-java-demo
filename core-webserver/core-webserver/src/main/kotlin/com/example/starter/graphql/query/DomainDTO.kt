package com.example.starter.graphql.query

import com.example.starter.db.entity.DomainEntity
import com.example.starter.graphql.node.NodeDTO

class DomainDTO(
    domain: DomainEntity
) : NodeDTO(checkNotNull(domain.id).toString()) {

    val domainName = checkNotNull(domain.domainName)
    val enabled = checkNotNull(domain.enabled)
}
