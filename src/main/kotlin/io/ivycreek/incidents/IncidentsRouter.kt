package io.ivycreek.incidents

import io.ivycreek.content.index
import io.ivycreek.content.isHtmxRequest
import io.ivycreek.content.respondComponent
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.time.Instant
import kotlinx.html.body

private const val HTMX_TARGET_HEADER = "HX-Target"
private const val INCIDENT_WORKSPACE_TARGET = "incident-workspace"

fun Application.incidentsRouter(activityNow: () -> Instant = Instant::now) {
    routing {
        route(INCIDENTS_PATH) {
            get {
                val query = IncidentQuery.from(call.request.queryParameters)

                if (call.request.isHtmxRequest() && call.request.headers[HTMX_TARGET_HEADER] == INCIDENT_WORKSPACE_TARGET) {
                    call.respondHtml(HttpStatusCode.OK) {
                        body {
                            incidentWorkspace(query)
                        }
                    }
                } else {
                    call.respondComponent { incidentsPage(query, activityCheckedAt = activityNow()) }
                }
            }
            get("/activity") {
                val checkedAt = activityNow()

                if (call.request.isHtmxRequest()) {
                    call.respondHtml(HttpStatusCode.OK) {
                        body {
                            incidentActivity(checkedAt)
                        }
                    }
                } else {
                    val query = IncidentQuery.from(call.request.queryParameters)
                    call.respondHtml(HttpStatusCode.OK) {
                        index(INCIDENTS_PATH) {
                            incidentsPage(query, activityCheckedAt = checkedAt)
                        }
                    }
                }
            }
            get("/{incidentId}") {
                val incident = findIncident(call.parameters["incidentId"])

                if (incident == null) {
                    call.respond(HttpStatusCode.NotFound)
                } else if (call.request.isHtmxRequest()) {
                    call.respondHtml(HttpStatusCode.OK) {
                        body {
                            incidentDetail(incident)
                        }
                    }
                } else {
                    val query = IncidentQuery.from(call.request.queryParameters)
                    val canonicalQuery = query.containing(incident)

                    if (canonicalQuery != query) {
                        call.respondRedirect(canonicalQuery.detailPath(incident.id))
                    } else {
                        call.respondHtml(HttpStatusCode.OK) {
                            index(INCIDENTS_PATH) {
                                incidentsPage(query, incident.id, activityNow())
                            }
                        }
                    }
                }
            }
        }
    }
}
