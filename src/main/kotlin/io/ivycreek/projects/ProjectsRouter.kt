package io.ivycreek.projects

import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.routing.*
import kotlinx.html.body

fun Application.projectsRouter() {
    routing {
        route("/components") {
            get("/projects") {
                call.respondHtml {
                    body {
                        projects()
                    }
                }
            }
        }
    }
}