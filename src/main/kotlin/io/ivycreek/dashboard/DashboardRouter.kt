package io.ivycreek.dashboard

import io.ivycreek.content.respondComponent
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.dashboardRouter() {
    routing {
        route("/components") {
            get("/dashboard") {
                call.respondComponent { dashboard() }
            }
        }
    }
}
