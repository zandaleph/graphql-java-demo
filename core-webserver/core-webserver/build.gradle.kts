import org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED
import org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED
import org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    application
    kotlin("kapt")
    id("com.example.convention")
}

repositories {
    mavenCentral()
}

val vertxVersion = "4.3.3"
val junitJupiterVersion = "5.9.0"
val daggerVersion = "2.43.2"
val hibernateVersion = "6.1.2.Final"

val mainVerticleName = "com.example.starter.MainVerticle"
val launcherClassName = "io.vertx.core.Launcher"

val watchForChange = "src/**/*"
val doOnChange = "$projectDir/../gradlew :core-webserver:core-webserver:classes"

application {
    mainClass.set(launcherClassName)
}

dependencies {
    implementation("com.example:graphql-utilities")
    implementation("com.example:login-webapp")
    implementation(project(":core-entities"))

    implementation(platform("io.vertx:vertx-stack-depchain:$vertxVersion"))
    implementation("io.vertx:vertx-web-graphql")
    implementation("io.vertx:vertx-web")
    implementation("io.vertx:vertx-lang-kotlin")
    implementation("io.vertx:vertx-lang-kotlin-coroutines")

    implementation("com.expediagroup:graphql-kotlin-schema-generator:6.2.5")

    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:1.6.4")

    implementation("org.hibernate:hibernate-core:$hibernateVersion")
    kapt("org.hibernate.orm:hibernate-jpamodelgen:$hibernateVersion")
    implementation("com.h2database:h2:2.1.214")

    implementation("io.github.microutils:kotlin-logging-jvm:2.1.23")
    implementation("org.slf4j:slf4j-simple:2.0.0")

    runtimeOnly("io.netty:netty-resolver-dns-native-macos:4.1.80.Final:osx-x86_64")

    implementation("com.google.dagger:dagger:$daggerVersion")
    kapt("com.google.dagger:dagger-compiler:$daggerVersion")
    kaptTest("com.google.dagger:dagger-compiler:$daggerVersion")

    testImplementation("io.vertx:vertx-junit5")
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
    systemProperty("org.jboss.logging.provider", "slf4j")
//    systemProperty("org.slf4j.simpleLogger.defaultLogLevel", "DEBUG")
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
