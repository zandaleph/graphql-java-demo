package com.example.starter.graphql.node

open class NodeDTO(private val internalId: String) {
    companion object {
        const val DELIMITER = "::"
        fun parseId(id: String): Pair<String, String> {
            val parts = id.split(DELIMITER, limit = 2)
            return parts[0] to parts[1]
        }
    }

    private val type by lazy { javaClass.simpleName.removeSuffix("DTO") }
    val id by lazy { "$type$DELIMITER$internalId" }
}
