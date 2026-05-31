package io.ivycreek.about

import io.ivycreek.content.respondComponent
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.aboutRouter() {
    routing {
        route("/components") {
            route("/about") {
                get {
                    call.respondComponent { about() }
                }
            }
        }
    }
}
