package io.ivycreek

import io.ivycreek.incidents.incidentsRouter
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import java.time.Instant
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
            assertFalse(content.contains("/app.css"), "${expected.path} should not include root page stylesheets")
            assertTrue(content.contains("<h1"), "${expected.path} should include a page heading")
            assertTrue(content.contains(expected.heading), "${expected.path} should include heading ${expected.heading}")
            expected.requiredText.forEach { text ->
                assertTrue(content.contains(text), "${expected.path} should include '$text'")
            }
        }
    }

    @Test
    fun `incident detail route renders only the detail region for htmx requests`() = testApplication {
        application {
            module()
        }

        val response = client.get("/components/incidents/inc-1047") {
            header("HX-Request", "true")
        }
        val content = response.bodyAsText()

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(ContentType.Text.Html, response.contentType()?.withoutParameters())
        assertTrue(content.contains("id=\"incident-detail\""))
        assertTrue(content.contains("Delayed inventory synchronization"))
        assertTrue(content.contains("Fulfillment platform"))
        assertTrue(content.contains("The queue has drained"))
        assertFalse(content.contains("id=\"incident-workspace\""))
        assertFalse(content.contains("data-nav-link"), "Detail fragments should not include navigation")
        assertFalse(content.contains("htmx.org@"), "Detail fragments should not include root page scripts")
    }

    @Test
    fun `incident controls preserve normal browser fallbacks and add focused htmx swaps`() = testApplication {
        application {
            module()
        }

        val response = client.get("/components/incidents") {
            header("HX-Request", "true")
        }
        val content = response.bodyAsText()

        assertEquals(HttpStatusCode.OK, response.status)
        assertTrue(
            Regex("""<form(?=[^>]*action="/components/incidents")(?=[^>]*method="get")(?=[^>]*id="incident-filters")(?=[^>]*hx-get="/components/incidents")(?=[^>]*hx-target="#incident-workspace")(?=[^>]*hx-push-url="true")[^>]*>""").containsMatchIn(content),
            "Filters should use an ordinary GET form enhanced with htmx"
        )
        assertTrue(content.contains("name=\"q\""))
        assertTrue(content.contains("hx-trigger=\"input changed delay:300ms, search\""))
        assertTrue(content.contains("name=\"status\""))
        assertTrue(content.contains("name=\"severity\""))
        assertTrue(content.contains("name=\"sort\""))
        assertTrue(
            Regex("""<section(?=[^>]*id="incident-activity")(?=[^>]*hx-get="/components/incidents/activity")(?=[^>]*hx-trigger="every 15s")(?=[^>]*hx-target="this")(?=[^>]*hx-swap="outerHTML")[^>]*>""").containsMatchIn(content),
            "The activity snapshot should poll only its own server-rendered region"
        )
        assertTrue(content.contains("The current server-rendered snapshot remains readable without it."))
        assertTrue(
            Regex("""<a(?=[^>]*href="/components/incidents/inc-1048")(?=[^>]*hx-get="/components/incidents/inc-1048")(?=[^>]*hx-target="#incident-detail")(?=[^>]*hx-swap="outerHTML")(?=[^>]*hx-push-url="true")[^>]*>""").containsMatchIn(content),
            "Incident details should use normal links enhanced with focused htmx swaps"
        )
        assertTrue(
            Regex("""<a(?=[^>]*href="/components/incidents\?page=2")(?=[^>]*hx-get="/components/incidents\?page=2")(?=[^>]*hx-target="#incident-workspace")(?=[^>]*hx-push-url="true")[^>]*>Next</a>""").containsMatchIn(content),
            "Pagination should retain a normal URL fallback"
        )
        assertTrue(
            Regex("""<a(?=[^>]*href="/components/incidents")(?=[^>]*hx-get="/components/incidents")(?=[^>]*hx-target="#content")(?=[^>]*hx-swap="outerHTML")(?=[^>]*hx-push-url="true")[^>]*>Clear filters</a>""").containsMatchIn(content),
            "Clearing filters should replace the form and workspace together"
        )
    }

    @Test
    fun `incident activity route returns only the polling region for htmx requests`() = testApplication {
        application {
            incidentsRouter { Instant.parse("2026-07-14T03:45:00Z") }
        }

        val response = client.get("/components/incidents/activity") {
            header("HX-Request", "true")
            header("HX-Target", "incident-activity")
        }
        val content = response.bodyAsText()

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(ContentType.Text.Html, response.contentType()?.withoutParameters())
        assertEquals(1, Regex("""id="incident-activity"""").findAll(content).count())
        assertTrue(content.contains("Operational activity"))
        assertTrue(content.contains("Mitigation started"))
        assertTrue(content.contains("Recovery confirmed"))
        assertTrue(content.contains("Investigation narrowed"))
        assertTrue(content.contains("datetime=\"2026-07-14T03:45:00Z\""))
        assertTrue(content.contains("03:45:00 UTC"))
        assertFalse(content.contains("id=\"content\""))
        assertFalse(content.contains("id=\"incident-workspace\""))
        assertFalse(content.contains("data-nav-link"))
        assertFalse(content.contains("htmx.org@"))
    }

    @Test
    fun `incident activity route renders the full incidents page for direct requests`() = testApplication {
        application {
            module()
        }

        val response = client.get("/components/incidents/activity")
        val content = response.bodyAsText()

        assertEquals(HttpStatusCode.OK, response.status)
        assertTrue(content.contains("Ktor + htmx Showcase"))
        assertEquals(1, Regex("""id="content"""").findAll(content).count())
        assertEquals(1, Regex("""id="incident-activity"""").findAll(content).count())
        assertTrue(content.contains("id=\"incident-workspace\""))
        assertTrue(
            Regex("""<a(?=[^>]*href="/components/incidents")(?=[^>]*aria-current="page")[^>]*>""").containsMatchIn(content),
            "Incidents should remain active on direct activity routes"
        )
    }

    @Test
    fun `incident queue applies search filters sorting and paging on the server`() = testApplication {
        application {
            module()
        }

        val filteredResponse = client.get("/components/incidents?q=production&status=open&sort=severity") {
            header("HX-Request", "true")
            header("HX-Target", "incident-workspace")
        }
        val filteredContent = filteredResponse.bodyAsText()

        assertEquals(HttpStatusCode.OK, filteredResponse.status)
        assertTrue(filteredContent.contains("id=\"incident-workspace\""))
        assertFalse(filteredContent.contains("id=\"content\""))
        assertFalse(filteredContent.contains("id=\"incident-filters\""))
        assertTrue(filteredContent.contains("Admin authentication timeouts"))
        assertFalse(filteredContent.contains("Metrics labels missing"))
        assertFalse(filteredContent.contains("Checkout requests returning"))
        assertTrue(filteredContent.contains("1 matching incident"))
        assertTrue(
            filteredContent.contains("href=\"/components/incidents/inc-1046?q=production&amp;status=open&amp;sort=severity\""),
            "Detail links should preserve the active queue state"
        )

        val sortedContent = client.get("/components/incidents?sort=status") {
            header("HX-Request", "true")
            header("HX-Target", "incident-workspace")
        }.bodyAsText()

        assertTrue(sortedContent.indexOf("Admin authentication timeouts") < sortedContent.indexOf("Metrics labels missing"))
        assertTrue(sortedContent.indexOf("Metrics labels missing") < sortedContent.indexOf("Checkout requests returning"))

        val pagedResponse = client.get("/components/incidents?page=2") {
            header("HX-Request", "true")
            header("HX-Target", "incident-workspace")
        }
        val pagedContent = pagedResponse.bodyAsText()

        assertEquals(HttpStatusCode.OK, pagedResponse.status)
        assertTrue(pagedContent.contains("Page 2 of 3"))
        assertTrue(pagedContent.contains("Search index refresh stalled"))
        assertTrue(pagedContent.contains("Metrics labels missing from canary pods"))
        assertFalse(pagedContent.contains("Checkout requests returning"))
        assertTrue(pagedContent.contains("href=\"/components/incidents\""))
        assertTrue(pagedContent.contains("href=\"/components/incidents?page=3\""))
    }

    @Test
    fun `incident filters return an explicit empty state`() = testApplication {
        application {
            module()
        }

        val response = client.get("/components/incidents?q=does-not-exist") {
            header("HX-Request", "true")
            header("HX-Target", "incident-workspace")
        }
        val content = response.bodyAsText()

        assertEquals(HttpStatusCode.OK, response.status)
        assertTrue(content.contains("No incidents match these filters"))
        assertTrue(content.contains("No incident is selected"))
        assertTrue(content.contains("No matching incidents"))
    }

    @Test
    fun `incident detail route renders full filtered workspace for direct browser requests`() = testApplication {
        application {
            module()
        }

        val response = client.get("/components/incidents/inc-1045?q=staging&severity=medium&sort=severity")
        val content = response.bodyAsText()

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(ContentType.Text.Html, response.contentType()?.withoutParameters())
        assertTrue(content.contains("Ktor + htmx Showcase"))
        assertTrue(content.contains("value=\"staging\""))
        assertTrue(content.contains("Search index refresh stalled"))
        assertTrue(content.contains("A malformed fixture has been isolated"))
        assertTrue(content.contains("htmx.org@2.0.10"))
        assertTrue(content.contains("href=\"/app.css\""))
        assertFalse(content.contains("cdn.tailwindcss.com"))
        assertEquals(1, Regex("""id="content"""").findAll(content).count())
        assertTrue(
            Regex("""<a(?=[^>]*href="/components/incidents")(?=[^>]*aria-current="page")[^>]*>""").containsMatchIn(content),
            "Incidents should remain active on direct detail routes"
        )
    }

    @Test
    fun `direct incident routes normalize filters and paging around the selected incident`() = testApplication {
        application {
            module()
        }
        val noRedirectClient = createClient {
            followRedirects = false
        }

        val conflictingFilters = noRedirectClient.get(
            "/components/incidents/inc-1048?q=staging&status=resolved&severity=low&page=3&sort=status"
        )
        assertEquals(HttpStatusCode.Found, conflictingFilters.status)
        assertEquals(
            "/components/incidents/inc-1048?sort=status",
            conflictingFilters.headers[HttpHeaders.Location]
        )

        val wrongPage = noRedirectClient.get("/components/incidents/inc-1041?page=1")
        assertEquals(HttpStatusCode.Found, wrongPage.status)
        assertEquals(
            "/components/incidents/inc-1041?page=3",
            wrongPage.headers[HttpHeaders.Location]
        )
    }

    @Test
    fun `unknown incident route returns not found`() = testApplication {
        application {
            module()
        }

        val response = client.get("/components/incidents/missing-incident")

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
            assertTrue(content.contains("href=\"/app.css\""), "${expected.path} should link compiled CSS")
            assertFalse(content.contains("cdn.tailwindcss.com"), "${expected.path} should not include Tailwind Play CDN")
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
                path = "/components/incidents",
                heading = "Incidents",
                requiredText = listOf(
                    "Incident queue",
                    "Operational activity",
                    "Snapshot checked",
                    "Search incidents",
                    "Checkout requests returning elevated errors",
                    "Checkout API",
                    "Critical",
                    "Investigating",
                    "Customer impact",
                    "Latest update"
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
