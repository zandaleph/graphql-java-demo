package com.example.starter.auth

import io.vertx.core.AsyncResult
import io.vertx.core.Future
import io.vertx.core.Handler
import io.vertx.core.json.JsonObject
import io.vertx.ext.auth.User
import io.vertx.ext.auth.authentication.AuthenticationProvider

class ExampleAuthenticationProvider(private val userCredentials: Map<String, String>) : AuthenticationProvider {
    override fun authenticate(credentials: JsonObject, resultHandler: Handler<AsyncResult<User>>) {
        val username = credentials.getString("username")
        val password = credentials.getString("password")
        if (password == userCredentials[username]) {
            resultHandler.handle(Future.succeededFuture(User.fromName(username)))
        } else {
            resultHandler.handle(Future.failedFuture("Invalid password"))
        }
    }
}
