package com.example.service.proxy

import com.example.service.proxy.graphql.ProxyRoutingGraphQL
import io.vertx.core.http.HttpServer
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.BodyHandler
import io.vertx.ext.web.handler.graphql.GraphQLHandler
import io.vertx.ext.web.handler.graphql.GraphiQLHandler
import io.vertx.kotlin.core.http.httpServerOptionsOf
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.await

class MainProxyVerticle : CoroutineVerticle() {

    private lateinit var server: HttpServer

    override suspend fun start() {
        val proxyRoutingGraphQL = ProxyRoutingGraphQL()
//        val component = DaggerGraphQLComponent.builder()
//            .hibernateModule(HibernateModule(true))
//            .build()
//
//        val authProvider = ExampleAuthenticationProvider(mapOf("admin" to "admin"))
//        val redirectAuthHandler = RedirectAuthHandler.create(authProvider, "/")
//        val forbiddenAuthHandler = SimpleAuthenticationHandler.create().authenticate { ctx ->
//            ctx.user()?.let { Future.succeededFuture(it) } ?: Future.failedFuture(HttpException(403))
//        }

        val router = Router.router(vertx)

        router.route("/graphql")
            .handler(BodyHandler.create())
//            .handler(forbiddenAuthHandler)
            .handler(
                GraphQLHandler.create(proxyRoutingGraphQL.doThing()).beforeExecute {
//                    it.builder().root(component.rootDTO())
                }
            )

        router.route("/graphiql/*")
//            .handler(redirectAuthHandler)
            .handler(GraphiQLHandler.create())

        server = vertx.createHttpServer(httpServerOptionsOf(port = 8080))
            .requestHandler(router)
            .listen().await()
    }

    override suspend fun stop() {
        server.close().await()
    }
}