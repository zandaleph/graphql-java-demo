package com.example.starter.auth

import com.example.starter.auth.db.entity.DbSessionEntity
import com.example.starter.db.HibernateEntities
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet

@Module
class AuthModule {

    @Provides
    @IntoSet
    @HibernateEntities
    fun provideAuthDataModelEntities(): Class<*> = DbSessionEntity::class.java

}