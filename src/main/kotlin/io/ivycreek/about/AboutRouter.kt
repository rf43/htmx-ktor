package io.ivycreek.about

import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.routing.*
import kotlinx.html.body

fun Application.aboutRouter() {
    routing {
        route("/components") {
            route("/about") {
                get {
                    call.respondHtml {
                        body {
                            about()
                        }
                    }
                }
            }
        }
    }
}