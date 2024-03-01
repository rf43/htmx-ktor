package io.ivycreek.contact

import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.routing.*
import kotlinx.html.body

fun Application.contactRouter() {
    routing {
        route("/components") {
            route("/contact") {
                get {
                    call.respondHtml {
                        body {
                            contact()
                        }
                    }
                }
            }
        }
    }
}