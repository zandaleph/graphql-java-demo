package com.example.starter.db

import dagger.Component
import org.hibernate.SessionFactory
import javax.inject.Singleton

@Singleton
@Component(modules = [HibernateModule::class])
interface HibernateTestComponent {
    fun sessionFactory(): SessionFactory
}
