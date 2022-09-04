package com.example.starter.graphql

import com.example.starter.db.HibernateModule
import graphql.ExecutionInput
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

class GraphQLTest {

    companion object {
        val HELLO_QUERY = """
            query HelloQuery {
              hello
            }
        """.trimIndent()

        val ADD_TENANT_MUTATION = """
            mutation AddTenantMutation(${'$'}input: AddTenantInput) {
              adminMutation {
                addTenant(input: ${'$'}input) {
                  tenant {
                    id
                    name
                  }
                }
              }
            }
        """.trimIndent()

        val ADD_USER_MUTATION = """
            mutation AddUserMutation(${'$'}tenantId: ID!, ${'$'}input: AddUserInput) {
              tenantMutation(tenantId: ${'$'}tenantId) {
                addUser(input: ${'$'}input) {
                  user {
                    id
                    name
                  }
                }
              }
            }
        """.trimIndent()

        val LIST_TENANT_USERS_QUERY = """
            query ListTenantUsersQuery(${'$'}tenantId: ID!, ${'$'}first: Int!, ${'$'}after: String) {
              tenant(tenantId: ${'$'}tenantId) {
                users(first: ${'$'}first, after: ${'$'}after) {
                  edges {
                    node {
                      id
                      name
                    }
                  }
                  pageInfo {
                    hasNextPage
                    endCursor
                  }
                }
              }
            }
        """.trimIndent()
    }

    private val component = DaggerGraphQLComponent.builder()
        .hibernateModule(HibernateModule(true))
        .build()

    @Test
    fun testHello() {
        val result = executeGraphQL(HELLO_QUERY)
        val data = result.getData<Any>()
        assertEquals(mapOf("hello" to "world"), data)
    }

    @Test
    fun testAddTenantAndUser() {
        val tenantName = "Example Tenant"
        val userName = "Alice"

        val result1 = executeGraphQL(ADD_TENANT_MUTATION, mapOf("input" to mapOf("name" to tenantName)))
        val data1 = result1.getData<Map<String, Any>>()
        val tenant1 = data1?.getObject("adminMutation")?.getObject("addTenant")?.getObject("tenant")
        val tenantId = tenant1?.get("id") as String
        assertEquals(tenantName, tenant1["name"])
        assertNotNull(tenantId)

        val result2 = executeGraphQL(
            ADD_USER_MUTATION,
            mapOf("tenantId" to tenantId, "input" to mapOf("name" to userName))
        )
        val data2 = result2.getData<Map<String, Any>>()
        val user2 = data2?.getObject("tenantMutation")?.getObject("addUser")?.getObject("user")
        assertEquals(userName, user2?.get("name"))
        assertNotNull(user2?.get("id"))

        val result3 = executeGraphQL(LIST_TENANT_USERS_QUERY, mapOf("tenantId" to tenantId, "first" to 10))
        val data3 = result3.getData<Map<String, Any>>()
        val users = data3?.getObject("tenant")?.getObject("users")
        val pageInfo = users?.getObject("pageInfo")
        assertEquals(false, pageInfo?.get("hasNextPage"))
        assertEquals(userName, pageInfo?.get("endCursor"))
        assertEquals(user2, users?.getObjectList("edges")?.get(0)?.getObject("node"))
    }

    private fun executeGraphQL(query: String, variables: Map<String, Any> = mapOf()) = component.graphQL().execute(
        ExecutionInput.newExecutionInput(query)
            .root(component.rootDTO())
            .variables(variables)
    )

    @Suppress("UNCHECKED_CAST")
    private fun Map<String, Any>.getObject(key: String): Map<String, Any>? = get(key) as? Map<String, Any>

    @Suppress("UNCHECKED_CAST")
    private fun Map<String, Any>.getObjectList(key: String): List<Map<String, Any>>? =
        get(key) as? List<Map<String, Any>>
}
