package io.ivycreek.contact

import io.ivycreek.content.respondComponent
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.contactRouter() {
    routing {
        route("/components") {
            route("/contact") {
                get {
                    call.respondComponent { contact() }
                }
            }
        }
    }
}
