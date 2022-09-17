package com.example.starter.db

import com.example.starter.db.entity.TenantEntity
import graphql.Assert.assertNotNull
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.UUID

class TenantDaoTest {

    private val sessionFactory = DaggerHibernateTestComponent.builder()
        .hibernateModule(HibernateModule(false))
        .build()
        .sessionFactory()

    @Test
    fun testCreateAndGetTenant() {
        val tenant = TenantEntity("Example Tenant")
        sessionFactory.inTransaction {
            val tenantDao = TenantDao(it)
            tenantDao.createTenant(tenant)
        }
        assertNotNull(tenant.id)
        var retrievedTenant: TenantEntity? = null
        sessionFactory.inTransaction {
            val tenantDao = TenantDao(it)
            retrievedTenant = tenantDao.getTenant(checkNotNull(tenant.id))
        }
        assertEquals(tenant.name, retrievedTenant?.name)
    }

    @Test
    fun testListTenants() {
        val names = listOf("Jinteki", "Haas-Bioroid", "NBN", "Weyland Consortium")
        names.forEach { name ->
            sessionFactory.inTransaction {
                TenantDao(it).createTenant(TenantEntity(name))
            }
        }
        sessionFactory.inTransaction {
            val tenants = TenantDao(it).listTenants()
            val retrievedNames = tenants.map { it.name }
            assertEquals(names.toSet(), retrievedNames.toSet())
        }
    }

    @Test
    fun testPaginationOfListTenants() {
        val ids = mutableSetOf<UUID>()
        sessionFactory.inTransaction {
            val tenantDao = TenantDao(it)
            repeat(25) { num ->
                val tenant = TenantEntity("Business $num")
                tenantDao.createTenant(tenant)
                ids.add(checkNotNull(tenant.id))
            }
        }
        val receivedIds = mutableListOf<UUID>()
        repeat(3) {
            sessionFactory.inTransaction {
                val tenants = TenantDao(it).listTenants(first = 9, after = receivedIds.lastOrNull())
                tenants.mapTo(receivedIds) { t -> checkNotNull(t.id) }
            }
        }
        assertEquals(ids, receivedIds.toSet())
    }
}
