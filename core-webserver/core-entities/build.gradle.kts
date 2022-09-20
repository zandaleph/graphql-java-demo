plugins {
    `java-library`
    id("com.example.convention")
    id("org.hibernate.orm") version "6.1.0.Final"
}

repositories {
    mavenCentral()
}

val hibernateVersion = "6.1.2.Final"

hibernate {
    enhancement {
        lazyInitialization(true)
        dirtyTracking(true)
    }
    disableJpaMetamodel()
}

dependencies {
    implementation("org.jetbrains:annotations:23.0.0")

    implementation("org.hibernate:hibernate-core:$hibernateVersion")
    annotationProcessor("org.hibernate.orm:hibernate-jpamodelgen:$hibernateVersion")
}


java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of("11"))
    }
}