package com.example.starter.graphql.query

import com.example.starter.graphql.mutation.TenantMutationDTO
import dagger.Subcomponent

@Subcomponent(modules = [TenantModule::class])
interface TenantComponent {
    fun tenantDto(): TenantDTO
    fun tenantMutationDto(): TenantMutationDTO

    @Subcomponent.Builder
    interface Builder {
        fun tenantModule(module: TenantModule): Builder
        fun build(): TenantComponent
    }
}
