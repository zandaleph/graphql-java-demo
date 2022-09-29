package com.example.routing.vertx

import io.vertx.core.Vertx
import io.vertx.ext.web.client.WebClient
import io.vertx.kotlin.coroutines.await
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test


class VertxRoutingServiceTest {

    @Test
    fun testSingleRouting() = runBlocking {
        val vertx = Vertx.vertx()
        val routingService = VertxRoutingService()
        vertx.deployVerticle(routingService).await()

        val prefix = "doggo"
        val router = routingService.registerPrefix(prefix)
        router.routeWithRegex("/?").handler { ctx ->
            ctx.end(ctx.request().path())
        }

        val client = WebClient.create(vertx)
        val response = client.getAbs("http://localhost:${routingService.port()}/$prefix/").send().await()

        assertEquals("/$prefix/", response.bodyAsString())
    }
}