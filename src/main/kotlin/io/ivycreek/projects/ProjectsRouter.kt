package io.ivycreek.projects

import io.ivycreek.content.respondComponent
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.projectsRouter() {
    routing {
        route("/components") {
            get("/projects") {
                call.respondComponent { projects() }
            }
        }
    }
}
