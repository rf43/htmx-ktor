package io.ivycreek.calendar

import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.routing.*
import kotlinx.html.body

fun Application.calendarRouter() {
    routing {
        route("/components") {
            route("/calendar") {
                get {
                    call.respondHtml {
                        body {
                            calendar()
                        }
                    }
                }
            }
        }
    }
}