import org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED
import org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED
import org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.10"
    application
    kotlin("kapt") version "1.6.10"
}

group = "com.example"
version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val vertxVersion = "4.3.3"
val junitJupiterVersion = "5.7.0"
val daggerVersion = "2.43.2"
val loginjectVersion = "1.1.0"

val mainVerticleName = "com.example.starter.MainVerticle"
val launcherClassName = "io.vertx.core.Launcher"

val watchForChange = "src/**/*"
val doOnChange = "$projectDir/gradlew classes"

application {
    mainClass.set(launcherClassName)
}

dependencies {
    implementation(platform("io.vertx:vertx-stack-depchain:$vertxVersion"))
    implementation("io.vertx:vertx-web-graphql")
    implementation("io.vertx:vertx-web")
    implementation("io.vertx:vertx-lang-kotlin")
    implementation("io.vertx:vertx-lang-kotlin-coroutines")

    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:1.6.4")

    implementation("org.hibernate:hibernate-core:6.1.2.Final")
    implementation("com.h2database:h2:2.1.214")

    implementation("org.loginject:loginject-api:$loginjectVersion")
    implementation("org.loginject:loginject-dagger:$loginjectVersion")
    implementation("io.github.microutils:kotlin-logging-jvm:2.1.23")
    implementation("org.slf4j:slf4j-simple:1.7.29")

    runtimeOnly("io.netty:netty-resolver-dns-native-macos:4.1.79.Final:osx-x86_64")

    implementation("com.google.dagger:dagger:$daggerVersion")
    kapt("com.google.dagger:dagger-compiler:$daggerVersion")

    testImplementation("io.vertx:vertx-junit5")
    testImplementation("org.junit.jupiter:junit-jupiter:$junitJupiterVersion")
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions.jvmTarget = "11"

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        events = setOf(PASSED, SKIPPED, FAILED)
    }
}

tasks.withType<JavaExec> {
    environment("VERTXWEB_ENVIRONMENT", "dev")
    jvmArgs = listOf("-Dorg.slf4j.simpleLogger.defaultLogLevel=DEBUG")
    args = listOf(
        "run",
        mainVerticleName,
        "--redeploy=$watchForChange",
        "--launcher-class=$launcherClassName",
        "--on-redeploy=$doOnChange"
    )
}
