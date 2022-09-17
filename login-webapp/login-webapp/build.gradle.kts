import com.github.gradle.node.npm.task.NpmTask

plugins {
    java
    // You have to specify the plugin version, for instance
    // id "com.github.node-gradle.node" version "3.0.0"
    // This works as is here because we use the plugin source
    id("com.github.node-gradle.node") version "3.4.0"
}

group = "com.example"
version = "1.0.0-SNAPSHOT"

val npmInstall = tasks.named("npmInstall")

val buildTask = tasks.register<NpmTask>("buildWebapp") {
//    command.set("react-scripts")
    args.set(listOf("run", "build"))
    dependsOn(npmInstall)
    inputs.dir(fileTree("src").exclude("**/*.test.js").exclude("**/*.spec.js").exclude("**/__tests__/**/*.js"))
    inputs.dir("node_modules")
    inputs.dir("public")
    outputs.dir("$buildDir/webapp")
    environment.set(mapOf("BUILD_PATH" to "$buildDir/webapp/webroot"))
}

sourceSets {
    java {
        main {
            resources {
                // This makes the processResources task automatically depend on the buildWebapp one
                srcDir(buildTask)
            }
        }
    }
}
