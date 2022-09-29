package com.example.routing.vertx

import com.example.routing.RoutingService
import io.vertx.core.http.HttpServer
import io.vertx.core.http.HttpServerOptions
import io.vertx.ext.web.Router
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.await

class VertxRoutingService(private val httpServerOptions: HttpServerOptions = HttpServerOptions()) : RoutingService,
    CoroutineVerticle() {

    private lateinit var server: HttpServer
    private lateinit var baseRouter: Router

    override suspend fun start() {
        server = vertx.createHttpServer(httpServerOptions)
        baseRouter = Router.router(vertx)
        server.requestHandler(baseRouter).listen().await()
    }

    override suspend fun stop() {
        server.close().await()
    }

    override fun registerPrefix(prefix: String): Router = Router.router(vertx).also {
        check(!prefix.startsWith("/")) { "RoutingService prefix must not start with \"/\"." }
        check(!prefix.endsWith("/")) { "RoutingService prefix must not end with \"/\"." }
        check(!prefix.contains("*")) { "RoutingService prefix must not contain \"*\"." }
        baseRouter.route("/$prefix/*").subRouter(it)
    }

    fun port() = server.actualPort()
}