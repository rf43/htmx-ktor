package io.ivycreek.dashboard

import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.routing.*
import kotlinx.html.body

fun Application.dashboardRouter() {
    routing {
        route("/components") {
            get("/dashboard") {
                call.respondHtml {
                    body {
                        dashboard()
                    }
                }
            }
        }
    }
}