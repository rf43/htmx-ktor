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
            val response = client.get(expected.path) {
                header("HX-Request", "true")
            }
            val content = response.bodyAsText()

            assertEquals(HttpStatusCode.OK, response.status, "${expected.path} should return OK")
            assertEquals(ContentType.Text.Html, response.contentType()?.withoutParameters())
            assertEquals(1, Regex("""id="content"""").findAll(content).count(), "${expected.path} should render one content root")
            assertFalse(content.contains("data-nav-link"), "${expected.path} should not include navigation links")
            assertFalse(content.contains("htmx.org@"), "${expected.path} should not include root page scripts")
            assertTrue(content.contains("<h1"), "${expected.path} should include a page heading")
            assertTrue(content.contains(expected.heading), "${expected.path} should include heading ${expected.heading}")
            expected.requiredText.forEach { text ->
                assertTrue(content.contains(text), "${expected.path} should include '$text'")
            }
        }
    }

    @Test
    fun `calendar event route renders event details for htmx requests`() = testApplication {
        application {
            module()
        }

        val response = client.get("/components/calendar/htmx-fragment-swap") {
            header("HX-Request", "true")
        }
        val content = response.bodyAsText()

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(ContentType.Text.Html, response.contentType()?.withoutParameters())
        assertTrue(content.contains("Event details"))
        assertTrue(content.contains("htmx fragment swap demo"))
        assertTrue(content.contains("Compare hx-get, hx-target, and hx-swap"))
        assertFalse(content.contains("data-nav-link"), "Detail fragments should not include navigation")
        assertFalse(content.contains("htmx.org@"), "Detail fragments should not include root page scripts")
    }

    @Test
    fun `calendar event route renders full calendar shell for direct browser requests`() = testApplication {
        application {
            module()
        }

        val response = client.get("/components/calendar/route-contract-tests")
        val content = response.bodyAsText()

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(ContentType.Text.Html, response.contentType()?.withoutParameters())
        assertTrue(content.contains("Ktor + htmx Showcase"))
        assertTrue(content.contains("Route contract test review"))
        assertTrue(content.contains("The detail endpoint can be tested directly"))
        assertTrue(content.contains("htmx.org@2.0.10"))
        assertEquals(1, Regex("""id="content"""").findAll(content).count())
        assertTrue(
            Regex("""<a(?=[^>]*href="/components/calendar")(?=[^>]*aria-current="page")[^>]*>""").containsMatchIn(content),
            "Calendar should remain active on direct event detail routes"
        )
    }

    @Test
    fun `unknown calendar event route returns not found`() = testApplication {
        application {
            module()
        }

        val response = client.get("/components/calendar/missing-event")

        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun `component routes render the full application shell for direct browser requests`() = testApplication {
        application {
            module()
        }

        componentExpectations.forEach { expected ->
            val response = client.get(expected.path)
            val content = response.bodyAsText()

            assertEquals(HttpStatusCode.OK, response.status, "${expected.path} should return OK")
            assertEquals(ContentType.Text.Html, response.contentType()?.withoutParameters())
            assertTrue(content.contains("Ktor + htmx Showcase"), "${expected.path} should include the app title")
            assertTrue(content.contains("htmx.org@2.0.10"), "${expected.path} should include the htmx script")
            assertTrue(content.contains("tailwindcss"), "${expected.path} should include Tailwind")
            assertTrue(content.contains("hx-get="), "${expected.path} should include navigation links")
            assertEquals(1, Regex("""id="content"""").findAll(content).count(), "${expected.path} should render one content root")
            assertEquals(1, Regex("""aria-current="page"""").findAll(content).count(), "${expected.path} should render one active navigation item")
            assertTrue(
                Regex("""<a(?=[^>]*href="${Regex.escape(expected.path)}")(?=[^>]*aria-current="page")[^>]*>""").containsMatchIn(content),
                "${expected.path} should mark the matching navigation item active"
            )
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
                heading = "Ktor + htmx Showcase",
                requiredText = listOf(
                    "Server-rendered interactivity",
                    "hx-target=&quot;#content&quot;"
                )
            ),
            ComponentExpectation(
                path = "/components/team",
                heading = "Team",
                requiredText = listOf(
                    "Ktor and htmx stack",
                    "Route owners"
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
                    "Ktor routing walkthrough",
                    "2026-06-03",
                    "htmx fragment swap demo",
                    "2026-06-10",
                    "Route contract test review",
                    "Test suite",
                    "Event details",
                    "Expected outcome"
                )
            ),
            ComponentExpectation(
                path = "/components/projects",
                heading = "Projects",
                requiredText = listOf(
                    "ordinary Ktor routes",
                    "Component navigation"
                )
            ),
            ComponentExpectation(
                path = "/components/about",
                heading = "About",
                requiredText = listOf(
                    "Why pair Ktor with htmx?",
                    "lightweight Kotlin server framework"
                )
            ),
            ComponentExpectation(
                path = "/components/contact",
                heading = "Contact",
                requiredText = listOf(
                    "Try extending the demo",
                    "Back to the showcase"
                )
            )
        )
    }
}
