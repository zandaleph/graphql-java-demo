pluginManagement {
    includeBuild("../build-logic")
}

includeBuild("../login-webapp")

include("core-webserver")
include("core-entities")
