package com.example.starter.db

import dagger.Module
import dagger.Provides
import org.hibernate.SessionFactory
import org.hibernate.boot.MetadataSources
import org.hibernate.boot.registry.StandardServiceRegistryBuilder
import java.util.Properties
import javax.inject.Singleton

@Module
class HibernateModule {

    @Provides
    @Singleton
    fun providesSessionFactory(): SessionFactory {
        val properties = Properties().apply {
            putAll(
                mapOf(
                    "hibernate.connection.driver_class" to "org.h2.Driver",
                    "hibernate.connection.url" to "jdbc:h2:mem:db",
                    "hibernate.connection.username" to "sa",
                    "hibernate.connection.password" to "",
                    "hibernate.hbm2ddl.auto" to "create-drop",
                    "hibernate.dialect" to "org.hibernate.dialect.H2Dialect",
                    "hibernate.show_sql" to "true"
                )
            )
        }
        val serviceRegistry = StandardServiceRegistryBuilder()
            .applySettings(properties)
            .build()
        val metadata = MetadataSources(serviceRegistry)
            .addAnnotatedClass(TenantEntity::class.java)
            .metadataBuilder
            .build()
        return metadata.sessionFactoryBuilder.build()
    }
}
