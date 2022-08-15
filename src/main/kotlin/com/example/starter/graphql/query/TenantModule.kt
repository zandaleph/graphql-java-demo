package com.example.starter.graphql.query

import com.example.starter.db.TenantEntity
import dagger.Module
import dagger.Provides

@Module
class TenantModule(
    @get:Provides
    val tenant: TenantEntity
)
