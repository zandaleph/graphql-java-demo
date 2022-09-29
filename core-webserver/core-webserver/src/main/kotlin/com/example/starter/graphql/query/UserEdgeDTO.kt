package com.example.starter.graphql.query

import com.example.graphql.util.EdgeDTO

class UserEdgeDTO(node: UserDTO) : EdgeDTO<UserDTO>(node, { checkNotNull(it.user.name).toString() })