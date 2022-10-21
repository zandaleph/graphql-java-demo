package com.example.starter.db

import com.example.starter.db.entity.DomainEntity
import com.example.starter.db.entity.TenantEntity
import com.example.starter.db.entity.UserEntity
import dagger.Module
import dagger.Provides
import dagger.multibindings.ElementsIntoSet

@Module
class DataModelModule {
    @Provides
    @ElementsIntoSet
    @HibernateEntities
    fun provideDataModelEntities(): Set<Class<*>> =
        setOf(TenantEntity::class.java, UserEntity::class.java, DomainEntity::class.java)
}