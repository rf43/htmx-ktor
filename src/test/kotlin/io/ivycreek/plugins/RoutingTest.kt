package io.ivycreek.plugins

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.*

class RoutingTest {
    @Test
    fun `root route returns html`() = testApplication {
        application {
            configureRouting()
        }
        client.get("/").apply {
            assertEquals(HttpStatusCode.OK, status)
            val content = bodyAsText()
            assertTrue(content.contains("<html", ignoreCase = true), "Response should contain html tag")
        }
    }

    @Test
    fun `static resources are served from the root path`() = testApplication {
        application {
            configureRouting()
        }
        client.get("/pnutz-logo-transparent.svg").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals(ContentType.Image.SVG, contentType()?.withoutParameters())
        }
    }

    @Test
    fun `root route renders page assets and shell content`() = testApplication {
        application {
            configureRouting()
        }
        val response = client.get("/")
        val content = response.bodyAsText()

        // Verify HTML structure
        assertTrue(content.contains("<html", ignoreCase = true), "Response should contain html tag")
        assertTrue(content.contains("</html>", ignoreCase = true), "Response should contain closing html tag")
        assertTrue(content.contains("<head", ignoreCase = true), "Response should contain head tag")
        assertTrue(content.contains("</head>", ignoreCase = true), "Response should contain closing head tag")
        assertTrue(content.contains("<body", ignoreCase = true), "Response should contain body tag")
        assertTrue(content.contains("</body>", ignoreCase = true), "Response should contain closing body tag")
        
        // Verify specific content
        assertTrue(content.contains("Ktor + htmx Showcase", ignoreCase = true), "Response should contain title")
        assertTrue(content.contains("htmx.org@2.0.10", ignoreCase = true), "Response should contain pinned HTMX script")
        assertTrue(content.contains("integrity=\"sha384-H5SrcfygHmAuTDZphMHqBJLc3FhssKjG7w/CeCpFReSfwBWDTKpkzPP8c+cLsK+V\""), "Response should contain HTMX integrity")
        assertTrue(content.contains("tailwindcss", ignoreCase = true), "Response should contain Tailwind script")
        assertTrue(content.contains("src=\"/pnutz-logo-transparent.svg\""), "Response should use an absolute logo asset path")
        assertTrue(content.contains("href=\"https://github.com/rf43/htmx-ktor\""), "Response should link back to the source repository")
        assertTrue(content.contains("rel=\"noopener noreferrer\""), "Source repository link should use safe external-link attributes")
        assertTrue(content.contains("aria-hidden=\"true\""), "Source repository icon should be hidden from assistive technology")
        assertTrue(content.contains("<svg class=\"h-4 w-4 shrink-0 fill-current\""), "Source repository link should include a GitHub icon")
        assertTrue(content.contains("htmx:afterSettle"), "Response should include active navigation sync after htmx swaps")
        assertTrue(content.contains("path.startsWith"), "Response should normalize nested component URLs for active navigation")
    }

    @Test
    fun `root route renders htmx navigation contract`() = testApplication {
        application {
            configureRouting()
        }
        val content = client.get("/").bodyAsText()

        listOf("Dashboard", "Team", "Projects", "Calendar", "About", "Contact").forEach { tab ->
            val path = "/components/${tab.lowercase()}"

            assertTrue(content.contains(">$tab</a>"), "Navigation should include $tab link text")
            assertTrue(content.contains("href=\"$path\""), "$tab should link to $path")
            assertTrue(content.contains("hx-get=\"$path\""), "$tab should fetch $path")
        }
        assertEquals(6, Regex("""data-nav-link="true"""").findAll(content).count(), "Each nav item should be tracked for active state")
        assertEquals(6, Regex("""hx-target="#content"""").findAll(content).count(), "Each nav item should target #content")
        assertEquals(6, Regex("""hx-swap="outerHTML"""").findAll(content).count(), "Each nav item should replace the content root")
        assertEquals(6, Regex("""hx-push-url="true"""").findAll(content).count(), "Each nav item should push browser history")
        assertEquals(1, Regex("""aria-current="page"""").findAll(content).count(), "One nav item should be active")
        assertTrue(
            Regex("""<a(?=[^>]*href="/components/dashboard")(?=[^>]*aria-current="page")[^>]*>""").containsMatchIn(content),
            "Dashboard should be active on the root route"
        )
        assertTrue(content.contains("id=\"content\""), "Root page should include the htmx swap target")
    }
}
