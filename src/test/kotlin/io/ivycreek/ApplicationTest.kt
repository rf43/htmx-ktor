package io.ivycreek

import io.ivycreek.about.aboutRouter
import io.ivycreek.calendar.calendarRouter
import io.ivycreek.contact.contactRouter
import io.ivycreek.dashboard.dashboardRouter
import io.ivycreek.plugins.configureRouting
import io.ivycreek.projects.projectsRouter
import io.ivycreek.team.teamRouter
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.testing.*
import kotlin.test.*

class ApplicationTest {
    @Test
    fun testRoot() = testApplication {
        application {
            module()
        }
        client.get("/").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertTrue(bodyAsText().contains("<body"))
            assertTrue(bodyAsText().contains("</body>"))
        }
    }

    @Test
    fun testAllRoutes() = testApplication {
        application {
            module()
        }

        // Test each component route
        listOf(
            "/components/dashboard",
            "/components/team",
            "/components/calendar",
            "/components/projects",
            "/components/about",
            "/components/contact"
        ).forEach { route ->
            client.get(route).apply {
                assertEquals(HttpStatusCode.OK, status)
                assertTrue(bodyAsText().contains("<body"))
                assertTrue(bodyAsText().contains("</body>"))
            }
        }
    }

    @Test
    fun testDashboard() = testApplication {
        application {
            configureRouting()
            dashboardRouter()
            teamRouter()
            calendarRouter()
            projectsRouter()
            aboutRouter()
            contactRouter()
        }
        client.get("/components/dashboard").apply {
            assertEquals(HttpStatusCode.OK, status)
        }
    }

    @Test
    fun testTeam() = testApplication {
        application {
            configureRouting()
            dashboardRouter()
            teamRouter()
            calendarRouter()
            projectsRouter()
            aboutRouter()
            contactRouter()
        }
        client.get("/components/team").apply {
            assertEquals(HttpStatusCode.OK, status)
        }
    }

    @Test
    fun testCalendar() = testApplication {
        application {
            configureRouting()
            dashboardRouter()
            teamRouter()
            calendarRouter()
            projectsRouter()
            aboutRouter()
            contactRouter()
        }
        client.get("/components/calendar").apply {
            assertEquals(HttpStatusCode.OK, status)
        }
    }

    @Test
    fun testProjects() = testApplication {
        application {
            configureRouting()
            dashboardRouter()
            teamRouter()
            calendarRouter()
            projectsRouter()
            aboutRouter()
            contactRouter()
        }
        client.get("/components/projects").apply {
            assertEquals(HttpStatusCode.OK, status)
        }
    }

    @Test
    fun testAbout() = testApplication {
        application {
            configureRouting()
            dashboardRouter()
            teamRouter()
            calendarRouter()
            projectsRouter()
            aboutRouter()
            contactRouter()
        }
        client.get("/components/about").apply {
            assertEquals(HttpStatusCode.OK, status)
        }
    }

    @Test
    fun testContact() = testApplication {
        application {
            configureRouting()
            dashboardRouter()
            teamRouter()
            calendarRouter()
            projectsRouter()
            aboutRouter()
            contactRouter()
        }
        client.get("/components/contact").apply {
            assertEquals(HttpStatusCode.OK, status)
        }
    }
} 