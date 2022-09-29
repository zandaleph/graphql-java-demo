import org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED
import org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED
import org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    application
    kotlin("kapt")
    id("com.example.convention")
    id("com.expediagroup.graphql") version "6.2.5"
}

repositories {
    mavenCentral()
}

val vertxVersion = "4.3.3"
val junitJupiterVersion = "5.9.0"
val daggerVersion = "2.43.2"
val hibernateVersion = "6.1.2.Final"

val mainVerticleName = "com.example.service.proxy.MainProxyVerticle"
val launcherClassName = "io.vertx.core.Launcher"

val watchForChange = "src/**/*"
val doOnChange = "$projectDir/gradlew classes"

application {
    mainClass.set(launcherClassName)
}

graphql {
    schema {
        packages = listOf(
            "com.example.service.proxy.graphql.schema",
            "com.example.graphql.schema",
        )
    }
}

dependencies {
    implementation("com.example:graphql-utilities")

    implementation(platform("io.vertx:vertx-stack-depchain:$vertxVersion"))
    implementation("io.vertx:vertx-web-graphql")
    implementation("io.vertx:vertx-web")
    implementation("io.vertx:vertx-lang-kotlin")
    implementation("io.vertx:vertx-lang-kotlin-coroutines")

    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:1.6.4")

    implementation("com.expediagroup:graphql-kotlin-schema-generator:6.2.5")
    implementation("com.expediagroup:graphql-kotlin-server:6.2.5")

    implementation("io.github.microutils:kotlin-logging-jvm:2.1.23")
    implementation("org.slf4j:slf4j-simple:2.0.0")

    runtimeOnly("io.netty:netty-resolver-dns-native-macos:4.1.80.Final:osx-x86_64")

    implementation("com.google.dagger:dagger:$daggerVersion")
    kapt("com.google.dagger:dagger-compiler:$daggerVersion")
    kaptTest("com.google.dagger:dagger-compiler:$daggerVersion")

    testImplementation("org.junit.jupiter:junit-jupiter:$junitJupiterVersion")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        events = setOf(PASSED, SKIPPED, FAILED)
    }
}

tasks.withType<JavaExec> {
    environment("VERTXWEB_ENVIRONMENT", "dev")
    args = listOf(
        "run",
        mainVerticleName,
//        "--java-opts='-Dorg.slf4j.simpleLogger.defaultLogLevel=DEBUG'",
        "--redeploy=$watchForChange",
        "--launcher-class=$launcherClassName",
        "--on-redeploy=$doOnChange"
    )
}
