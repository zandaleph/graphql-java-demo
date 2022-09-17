package com.example.starter.graphql.query

import com.example.starter.db.entity.UserEntity
import com.example.starter.graphql.node.NodeDTO

class UserDTO(
    user: UserEntity
) : NodeDTO(checkNotNull(user.id).toString()) {

    val name = checkNotNull(user.name)
}
