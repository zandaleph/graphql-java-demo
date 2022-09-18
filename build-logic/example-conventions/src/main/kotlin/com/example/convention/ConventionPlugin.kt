package com.example.convention

import org.gradle.api.Plugin
import org.gradle.api.Project

class ConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.group = "com.example"
        target.version = "1.0.0-SNAPSHOT"
    }
}