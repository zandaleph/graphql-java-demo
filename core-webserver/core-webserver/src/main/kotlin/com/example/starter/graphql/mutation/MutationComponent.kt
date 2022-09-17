package com.example.starter.graphql.mutation

import dagger.Subcomponent

@Subcomponent(modules = [MutationModule::class])
interface MutationComponent {
    fun mutationDTO(): MutationDTO

    @Subcomponent.Builder
    interface Builder {
        fun mutationModule(module: MutationModule): Builder
        fun build(): MutationComponent
    }
}
