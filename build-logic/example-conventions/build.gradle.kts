plugins {
    `java-gradle-plugin`
    kotlin("jvm") version "1.6.10"
}

repositories {
    mavenCentral()
}

gradlePlugin {
    plugins {
        create("simplePlugin") {
            id = "com.example.convention"
            implementationClass = "com.example.convention.ConventionPlugin"
        }
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.10")
}