package com.example.starter.auth

import io.vertx.ext.auth.authentication.AuthenticationProvider
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.handler.FormLoginHandler
import io.vertx.ext.web.handler.SessionHandler
import io.vertx.ext.web.handler.impl.FormLoginHandlerImpl

class SessionFlushingFormLoginHandler private constructor(
    private val sessionHandler: SessionHandler,
    authProvider: AuthenticationProvider,
    usernameParam: String?,
    passwordParam: String?,
    returnURLParam: String?,
    directLoggedInOKURL: String?,
) : FormLoginHandlerImpl(authProvider, usernameParam, passwordParam, returnURLParam, directLoggedInOKURL) {

    companion object {
        fun create(sessionHandler: SessionHandler, authProvider: AuthenticationProvider): FormLoginHandler {
            return SessionFlushingFormLoginHandler(
                sessionHandler,
                authProvider,
                DEFAULT_USERNAME_PARAM,
                DEFAULT_PASSWORD_PARAM,
                DEFAULT_RETURN_URL_PARAM,
                null,
            )
        }
    }

    override fun postAuthentication(ctx: RoutingContext?) {
        sessionHandler.flush(ctx).onSuccess {
            super.postAuthentication(ctx)
        }
    }
}
