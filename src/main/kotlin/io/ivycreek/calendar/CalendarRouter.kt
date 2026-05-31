package io.ivycreek.calendar

import io.ivycreek.content.index
import io.ivycreek.content.isHtmxRequest
import io.ivycreek.content.respondComponent
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.html.body

fun Application.calendarRouter() {
    routing {
        route("/components") {
            route("/calendar") {
                get {
                    call.respondComponent { calendar() }
                }
            }
            get("/calendar/{eventId}") {
                val event = findCalendarEvent(call.parameters["eventId"])

                if (event == null) {
                    call.respond(HttpStatusCode.NotFound)
                } else if (call.request.isHtmxRequest()) {
                    call.respondHtml(HttpStatusCode.OK) {
                        body {
                            calendarEventDetail(event)
                        }
                    }
                } else {
                    call.respondHtml(HttpStatusCode.OK) {
                        index("/components/calendar") {
                            calendar(event.id)
                        }
                    }
                }
            }
        }
    }
}
