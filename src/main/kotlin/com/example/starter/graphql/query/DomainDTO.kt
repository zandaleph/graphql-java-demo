package com.example.starter.graphql.query

import com.example.starter.graphql.node.NodeDTO

class DomainDTO(
    domainId: String,
    val domainName: String,
    val enabled: Boolean
) : NodeDTO(domainId)
