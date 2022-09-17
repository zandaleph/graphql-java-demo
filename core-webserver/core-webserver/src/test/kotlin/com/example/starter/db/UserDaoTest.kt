package com.example.starter.db

import com.example.starter.db.entity.TenantEntity
import com.example.starter.db.entity.UserEntity
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class UserDaoTest {

    private val sessionFactory = DaggerHibernateTestComponent.builder()
        .hibernateModule(HibernateModule(false))
        .build()
        .sessionFactory()

    @Test
    fun testListUsersForTenant() {
        val tenant1 = TenantEntity("first tenant")
        val tenant2 = TenantEntity("second tenant")

        val firstUsers = setOf("Alice", "Bob", "Carol")
        val secondUsers = setOf("Xena", "Yvette", "Zelda")

        sessionFactory.inTransaction {
            val tenantDao = TenantDao(it)
            tenantDao.createTenant(tenant1)
            tenantDao.createTenant(tenant2)

            val userDao = UserDao(it)
            firstUsers.forEach { name -> userDao.createUser(UserEntity(tenant1, name)) }
            secondUsers.forEach { name -> userDao.createUser(UserEntity(tenant2, name)) }
        }

        sessionFactory.inTransaction {
            val receivedTenant1Users = UserDao(it).listUsersForTenant(tenant1)
            val receivedTenant2Users = UserDao(it).listUsersForTenant(tenant2)

            assertEquals(firstUsers, receivedTenant1Users.map { u -> u.name }.toSet())
            assertEquals(secondUsers, receivedTenant2Users.map { u -> u.name }.toSet())
        }
    }
}
