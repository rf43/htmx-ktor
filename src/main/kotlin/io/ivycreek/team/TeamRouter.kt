package io.ivycreek.team

import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.routing.*
import kotlinx.html.body

fun Application.teamRouter() {
    routing {
        route("/components") {
            get("/team") {
                call.respondHtml {
                    body {
                        team()
                    }
                }
            }
        }
    }
}