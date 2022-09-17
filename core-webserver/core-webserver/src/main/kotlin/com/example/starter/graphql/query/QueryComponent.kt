package com.example.starter.graphql.query

import dagger.Subcomponent

@Subcomponent(modules = [QueryModule::class])
interface QueryComponent {
    fun queryDTO(): QueryDTO

    @Subcomponent.Builder
    interface Builder {
        fun queryModule(module: QueryModule): Builder
        fun build(): QueryComponent
    }
}
