package com.example.starter

import com.example.starter.graphql.DaggerGraphQLComponent
import io.vertx.core.http.HttpServer
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.BodyHandler
import io.vertx.ext.web.handler.graphql.GraphQLHandler
import io.vertx.ext.web.handler.graphql.GraphiQLHandler
import io.vertx.kotlin.core.http.httpServerOptionsOf
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.await
import mu.KotlinLogging

class MainVerticle : CoroutineVerticle() {
    companion object {
        val logger = KotlinLogging.logger { }
    }

    lateinit var server: HttpServer

    override suspend fun start() {
        val component = DaggerGraphQLComponent.create()
        val router = Router.router(vertx)

        router.route("/graphql")
            .handler(BodyHandler.create())
            .handler(
                GraphQLHandler.create(component.graphQL()).beforeExecute {
                    it.builder().root(component.rootDTO())
                }
            )
        router.route("/graphiql/*").handler(GraphiQLHandler.create())

        server = vertx.createHttpServer(httpServerOptionsOf(port = 8080))
            .requestHandler(router)
            .listen().await()
    }

    override suspend fun stop() {
        server.close().await()
    }
}
