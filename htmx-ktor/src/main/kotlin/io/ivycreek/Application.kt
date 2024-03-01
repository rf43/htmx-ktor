package io.ivycreek

import io.ivycreek.about.aboutRouter
import io.ivycreek.calendar.calendarRouter
import io.ivycreek.contact.contactRouter
import io.ivycreek.dashboard.dashboardRouter
import io.ivycreek.plugins.*
import io.ivycreek.projects.projectsRouter
import io.ivycreek.team.teamRouter
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = 8080) {
        module()
    }.start(wait = true)
}

private fun Application.module() {
    configureRouting()
    dashboardRouter()
    teamRouter()
    calendarRouter()
    projectsRouter()
    aboutRouter()
    contactRouter()
}
