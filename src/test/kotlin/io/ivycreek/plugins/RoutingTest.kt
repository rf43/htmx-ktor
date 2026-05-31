package io.ivycreek.plugins

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.*

class RoutingTest {
    @Test
    fun testRootRoute() = testApplication {
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
    fun testStaticResources() = testApplication {
        application {
            configureRouting()
        }
        client.get("/pnutz-logo-transparent.svg").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals(ContentType.Image.SVG, contentType()?.withoutParameters())
        }
    }

    @Test
    fun testRootRouteContent() = testApplication {
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
        assertTrue(content.contains("PNutz + htmx + Tailwind Example", ignoreCase = true), "Response should contain title")
        assertTrue(content.contains("htmx.org@2.0.10", ignoreCase = true), "Response should contain pinned HTMX script")
        assertTrue(content.contains("integrity=\"sha384-H5SrcfygHmAuTDZphMHqBJLc3FhssKjG7w/CeCpFReSfwBWDTKpkzPP8c+cLsK+V\""), "Response should contain HTMX integrity")
        assertTrue(content.contains("tailwindcss", ignoreCase = true), "Response should contain Tailwind script")
    }
}
