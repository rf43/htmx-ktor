package io.ivycreek.team

import io.ivycreek.content.respondComponent
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.teamRouter() {
    routing {
        route("/components") {
            get("/team") {
                call.respondComponent { team() }
            }
        }
    }
}
