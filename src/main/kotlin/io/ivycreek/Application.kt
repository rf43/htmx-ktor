package io.ivycreek

import io.ivycreek.about.aboutRouter
import io.ivycreek.contact.contactRouter
import io.ivycreek.dashboard.dashboardRouter
import io.ivycreek.incidents.incidentsRouter
import io.ivycreek.plugins.*
import io.ivycreek.projects.projectsRouter
import io.ivycreek.team.teamRouter
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    val port = System.getenv("PORT")?.toIntOrNull() ?: 8080

    embeddedServer(Netty, port = port, host = "0.0.0.0") {
        module()
    }.start(wait = true)
}

fun Application.module() {
    configureRouting()
    dashboardRouter()
    teamRouter()
    incidentsRouter()
    projectsRouter()
    aboutRouter()
    contactRouter()
}
