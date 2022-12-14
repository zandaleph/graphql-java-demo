package com.example.starter.graphql

import com.example.starter.auth.AuthModule
import com.example.starter.db.DataModelModule
import com.example.starter.db.HibernateModule
import dagger.Component
import graphql.GraphQL
import org.hibernate.SessionFactory
import javax.inject.Singleton

@Singleton
@Component(modules = [GraphQLModule::class, HibernateModule::class, AuthModule::class, DataModelModule::class])
interface GraphQLComponent {
    fun graphQL(): GraphQL
    fun rootDTO(): RootDTO
    fun sessionFactory(): SessionFactory

    @Component.Builder
    interface Builder {
        fun hibernateModule(hibernateModule: HibernateModule): Builder
        fun build(): GraphQLComponent
    }
}
