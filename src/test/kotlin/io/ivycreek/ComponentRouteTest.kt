package io.ivycreek

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.*

class ComponentRouteTest {
    @Test
    fun `component routes render expected content fragments`() = testApplication {
        application {
            module()
        }

        componentExpectations.forEach { expected ->
            val response = client.get(expected.path)
            val content = response.bodyAsText()

            assertEquals(HttpStatusCode.OK, response.status, "${expected.path} should return OK")
            assertEquals(ContentType.Text.Html, response.contentType()?.withoutParameters())
            assertEquals(1, Regex("""id="content"""").findAll(content).count(), "${expected.path} should render one content root")
            assertFalse(content.contains("hx-get="), "${expected.path} should not include navigation links")
            assertFalse(content.contains("htmx.org@"), "${expected.path} should not include root page scripts")
            assertTrue(content.contains("<h1"), "${expected.path} should include a page heading")
            assertTrue(content.contains(expected.heading), "${expected.path} should include heading ${expected.heading}")
            expected.requiredText.forEach { text ->
                assertTrue(content.contains(text), "${expected.path} should include '$text'")
            }
        }
    }

    @Test
    fun `unknown component route returns not found`() = testApplication {
        application {
            module()
        }

        val response = client.get("/components/missing")

        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    private data class ComponentExpectation(
        val path: String,
        val heading: String,
        val requiredText: List<String>
    )

    private companion object {
        private val componentExpectations = listOf(
            ComponentExpectation(
                path = "/components/dashboard",
                heading = "Dashboard",
                requiredText = listOf(
                    "Welcome to the PNutz Dashboard",
                    "manage your projects, team, and calendar"
                )
            ),
            ComponentExpectation(
                path = "/components/team",
                heading = "Team",
                requiredText = listOf(
                    "The team at PNutz",
                    "dedicated professionals"
                )
            ),
            ComponentExpectation(
                path = "/components/calendar",
                heading = "Calendar",
                requiredText = listOf(
                    "Title",
                    "Start Date",
                    "End Date",
                    "Location",
                    "Back End Development",
                    "2021-10-01",
                    "Front End Development",
                    "2024-01-21",
                    "2024-02-21",
                    "Charlottesville, VA"
                )
            ),
            ComponentExpectation(
                path = "/components/projects",
                heading = "Projects",
                requiredText = listOf(
                    "Welcome to the PNutz Projects",
                    "manage your projects, team, and calendar"
                )
            ),
            ComponentExpectation(
                path = "/components/about",
                heading = "About",
                requiredText = listOf(
                    "At PNutz",
                    "software development community"
                )
            ),
            ComponentExpectation(
                path = "/components/contact",
                heading = "Contact",
                requiredText = listOf("Contact us at")
            )
        )
    }
}
