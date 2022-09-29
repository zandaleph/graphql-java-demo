package com.example.starter.graphql.query

import com.example.graphql.util.EdgeDTO

class TenantEdgeDTO(node: TenantDTO) : EdgeDTO<TenantDTO>(node, { checkNotNull(it.tenant.id).toString() })