rootProject.name = "core-webserver-project"

pluginManagement {
    includeBuild("../build-logic")
}

includeBuild("../login-webapp")

include("core-webserver")