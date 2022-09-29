pluginManagement {
    includeBuild("../build-logic")
}

includeBuild("../login-webapp")
includeBuild("../graphql-utilities")

include("core-webserver")
include("core-entities")
