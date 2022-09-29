pluginManagement {
    includeBuild("../build-logic")
}

includeBuild("../graphql-utilities")

include("routing-service")
include("vertx-routing-service")

include("proxy-routing-server")

