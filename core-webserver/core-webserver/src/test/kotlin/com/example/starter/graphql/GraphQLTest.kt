package com.example.starter.graphql

import com.example.starter.db.HibernateModule
import graphql.ExecutionInput
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class GraphQLTest {

    companion object {
        @Suppress("UNCHECKED_CAST")
        private fun Map<String, Any>?.getObject(key: String): Map<String, Any>? =
            orEmpty()[key] as? Map<String, Any>

        @Suppress("UNCHECKED_CAST")
        private fun Map<String, Any>?.getObjectList(key: String): List<Map<String, Any>>? =
            orEmpty()[key] as? List<Map<String, Any>>
    }

    private val component = DaggerGraphQLComponent.builder()
        .hibernateModule(HibernateModule(true))
        .build()

    @Test
    fun testHello() {
        val result = executeGraphQL(
            """
            query HelloQuery {
              hello
            }
            """.trimIndent()
        )
        val data = result.getData<Any>()
        assertEquals(mapOf("hello" to "world"), data)
    }

    @Test
    fun testAddTenantAndUser() {
        val tenantName = "Example Tenant"
        val userName = "Alice"
        val domainName = "example.com"

        val tenant = addTenantMutation(tenantName)
        val tenantId = checkNotNull(tenant["id"]) as String
        assertEquals(tenantName, tenant["name"])

        val user = addUserMutation(tenantId, userName)
        assertEquals(userName, user["name"])
        assertNotNull(user["id"])

        val usersConnection = listTenantUsersQuery(tenantId, 10)
        assertEquals(user, usersConnection.nodes.first())
        assertFalse(usersConnection.pageInfo.hasNextPage)
        assertEquals(userName, usersConnection.pageInfo.endCursor)

        val domain = addDomainMutation(tenantId, domainName, enabled = true)
        assertEquals(domainName, domain["domainName"])
        assertNotNull(domain["id"])

        val domainsConnection = listTenantDomainsQuery(tenantId, 10)
        assertEquals(domain, domainsConnection.nodes.first())
        assertFalse(domainsConnection.pageInfo.hasNextPage)
        assertEquals(domainName, domainsConnection.pageInfo.endCursor)
    }

    @Test
    fun testPaginationOfTenants() {
        val names = (0..12).map { num ->
            "Business $num".also { addTenantMutation(it) }
        }.toSet()

        val (receivedNames, _) = (0..2).fold(setOf<String>() to null as String?) { (s, cursor), num ->
            val (tenants, pageInfo) = listTenantsQuery(5, cursor)
            assertTrue((num != 2) == pageInfo.hasNextPage)
            s.plus(tenants.map { it["name"] as String }) to pageInfo.endCursor
        }
        assertEquals(names, receivedNames)
    }

    @Test
    fun testPaginationOfTenantUsers() {
        val tenantId = checkNotNull(addTenantMutation("Example Tenant")["id"] as? String)

        val userNames = (0..12).map { num ->
            "User $num".also { addUserMutation(tenantId, it) }
        }.toSet()

        val (receivedNames, _) = (0..2).fold(setOf<String>() to null as String?) { (s, cursor), num ->
            val (users, pageInfo) = listTenantUsersQuery(tenantId, 5, cursor)
            println(pageInfo)
            assertTrue((num != 2) == pageInfo.hasNextPage)
            s.plus(users.map { it["name"] as String }) to pageInfo.endCursor
        }
        assertEquals(userNames, receivedNames)
    }

    @Test
    fun testPaginationOfTenantDomains() {
        val tenantId = checkNotNull(addTenantMutation("Example Tenant")["id"] as? String)

        val domainNames = (0..12).map { num ->
            "Domain $num".also { addDomainMutation(tenantId, it, true) }
        }.toSet()

        val (receivedNames, _) = (0..2).fold(setOf<String>() to null as String?) { (s, cursor), num ->
            val (users, pageInfo) = listTenantDomainsQuery(tenantId, 5, cursor)
            assertTrue((num != 2) == pageInfo.hasNextPage)
            s.plus(users.map { it["domainName"] as String }) to pageInfo.endCursor
        }
        assertEquals(domainNames, receivedNames)
    }

    private fun addTenantMutation(tenantName: String): Map<String, Any> {
        val result = executeGraphQL(
            """
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
            """.trimIndent(),
            mapOf("input" to mapOf("name" to tenantName))
        )
        val data = result.getData<Map<String, Any>>()
        return checkNotNull(data.getObject("adminMutation").getObject("addTenant").getObject("tenant"))
    }

    private fun listTenantsQuery(first: Int, after: String? = null): TestConnection {
        val result = executeGraphQL(
            """
            query ListTenantsQuery(${'$'}first: Int!, ${'$'}after: String) {
              tenants(first: ${'$'}first, after: ${'$'}after) {
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
            """.trimIndent(),
            mapOf("first" to first, "after" to after)
        )
        val data = result.getData<Map<String, Any>>()
        println("Here's data: $data")
        return TestConnection.fromMap(data.getObject("tenants"))
    }

    private fun addUserMutation(tenantId: String, userName: String): Map<String, Any> {
        val result = executeGraphQL(
            """
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
            """.trimIndent(),
            mapOf("tenantId" to tenantId, "input" to mapOf("name" to userName))
        )
        val data = result.getData<Map<String, Any>>()
        return checkNotNull(data.getObject("tenantMutation").getObject("addUser").getObject("user"))
    }

    private fun listTenantUsersQuery(tenantId: String, first: Int, after: String? = null): TestConnection {
        val result = executeGraphQL(
            """
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
            """.trimIndent(),
            mapOf("tenantId" to tenantId, "first" to first, "after" to after)
        )
        val data = result.getData<Map<String, Any>>()
        return TestConnection.fromMap(data.getObject("tenant").getObject("users"))
    }

    private fun addDomainMutation(tenantId: String, domainName: String, enabled: Boolean): Map<String, Any> {
        val result = executeGraphQL(
            """
            mutation AddDomainMutation(${'$'}tenantId: ID!, ${'$'}input: AddDomainInput) {
              tenantMutation(tenantId: ${'$'}tenantId) {
                addDomain(input: ${'$'}input) {
                  domain {
                    id
                    domainName
                    enabled
                  }
                }
              }
            }
            """.trimIndent(),
            mapOf("tenantId" to tenantId, "input" to mapOf("domainName" to domainName, "enabled" to enabled))
        )
        val data = result.getData<Map<String, Any>>()
        return checkNotNull(data.getObject("tenantMutation").getObject("addDomain").getObject("domain"))
    }

    private fun listTenantDomainsQuery(tenantId: String, first: Int, after: String? = null): TestConnection {
        val result = executeGraphQL(
            """
            query ListTenantDomainsQuery(${'$'}tenantId: ID!, ${'$'}first: Int!, ${'$'}after: String) {
              tenant(tenantId: ${'$'}tenantId) {
                domains(first: ${'$'}first, after: ${'$'}after) {
                  edges {
                    node {
                      id
                      domainName
                      enabled
                    }
                  }
                  pageInfo {
                    hasNextPage
                    endCursor
                  }
                }
              }
            }
            """.trimIndent(),
            mapOf("tenantId" to tenantId, "first" to first, "after" to after)
        )
        val data = result.getData<Map<String, Any>>()
        return TestConnection.fromMap(data.getObject("tenant").getObject("domains"))
    }

    private fun executeGraphQL(query: String, variables: Map<String, Any?> = mapOf()) = component.graphQL().execute(
        ExecutionInput.newExecutionInput(query)
            .root(component.rootDTO())
            .variables(variables)
    )

    data class TestConnection(val nodes: List<Map<String, Any>>, val pageInfo: TestPageInfo) {
        companion object {
            fun fromMap(map: Map<String, Any>?) = TestConnection(
                nodes = checkNotNull(map.getObjectList("edges")?.map { checkNotNull(it.getObject("node")) }),
                pageInfo = TestPageInfo.fromMap(checkNotNull(map.getObject("pageInfo")))
            )
        }
    }

    data class TestPageInfo(val hasNextPage: Boolean, val endCursor: String) {
        companion object {
            fun fromMap(map: Map<String, Any>) = TestPageInfo(map["hasNextPage"] as Boolean, map["endCursor"] as String)
        }
    }
}
