package com.example.starter

import com.example.starter.auth.ExampleAuthenticationProvider
import com.example.starter.db.HibernateModule
import com.example.starter.graphql.DaggerGraphQLComponent
import io.vertx.core.Future
import io.vertx.core.http.HttpServer
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.BodyHandler
import io.vertx.ext.web.handler.FormLoginHandler
import io.vertx.ext.web.handler.HttpException
import io.vertx.ext.web.handler.RedirectAuthHandler
import io.vertx.ext.web.handler.SessionHandler
import io.vertx.ext.web.handler.SimpleAuthenticationHandler
import io.vertx.ext.web.handler.StaticHandler
import io.vertx.ext.web.handler.graphql.GraphQLHandler
import io.vertx.ext.web.handler.graphql.GraphiQLHandler
import io.vertx.ext.web.sstore.LocalSessionStore
import io.vertx.kotlin.core.http.httpServerOptionsOf
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.await

class MainVerticle : CoroutineVerticle() {

    private lateinit var server: HttpServer

    override suspend fun start() {
        val component = DaggerGraphQLComponent.builder()
            .hibernateModule(HibernateModule(true))
            .build()

        val authProvider = ExampleAuthenticationProvider(mapOf("admin" to "admin"))
        val redirectAuthHandler = RedirectAuthHandler.create(authProvider, "/")
        val forbiddenAuthHandler = SimpleAuthenticationHandler.create().authenticate { ctx ->
            ctx.user()?.let { Future.succeededFuture(it) } ?: Future.failedFuture(HttpException(403))
        }

        val router = Router.router(vertx)
        router.route().handler(SessionHandler.create(LocalSessionStore.create(vertx)))

        router.route("/login")
            .handler(BodyHandler.create())
            .handler(FormLoginHandler.create(authProvider).setDirectLoggedInOKURL("/graphiql/index.html"))

        router.route("/graphql")
            .handler(BodyHandler.create())
            .handler(forbiddenAuthHandler)
            .handler(
                GraphQLHandler.create(component.graphQL()).beforeExecute {
                    it.builder().root(component.rootDTO())
                }
            )

        router.route("/graphiql/*")
            .handler(redirectAuthHandler)
            .handler(GraphiQLHandler.create())

        router.route().handler(StaticHandler.create())

        server = vertx.createHttpServer(httpServerOptionsOf(port = 8080))
            .requestHandler(router)
            .listen().await()
    }

    override suspend fun stop() {
        server.close().await()
    }
}
