package com.example.routing

import io.vertx.ext.web.Router

interface RoutingService {

    fun registerPrefix(prefix: String): Router
}