package com.example.starter.graphql.mutation

import com.example.starter.graphql.query.TenantComponent
import dagger.Module

@Module(subcomponents = [TenantComponent::class])
class MutationModule
