package com.example.starter.graphql.query

import com.example.graphql.util.EdgeDTO

class DomainEdgeDTO(node: DomainDTO) : EdgeDTO<DomainDTO>(node, { checkNotNull(it.domain.domainName).toString() })