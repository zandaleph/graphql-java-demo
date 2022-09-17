package com.example.starter.graphql.query

import com.example.starter.db.entity.TenantEntity
import com.example.starter.graphql.mutation.TenantMutationDTO
import dagger.Subcomponent
import javax.inject.Provider

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

fun TenantEntity.toComponent(builderProvider: Provider<TenantComponent.Builder>) =
    builderProvider.get().tenantModule(TenantModule(this)).build()
