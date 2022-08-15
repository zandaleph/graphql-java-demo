package com.example.starter.graphql

import com.example.starter.db.HibernateModule
import dagger.Component
import graphql.GraphQL
import javax.inject.Singleton

@Singleton
@Component(modules = [GraphQLModule::class, HibernateModule::class])
interface GraphQLComponent {
    fun graphQL(): GraphQL
    fun rootDTO(): RootDTO
}
