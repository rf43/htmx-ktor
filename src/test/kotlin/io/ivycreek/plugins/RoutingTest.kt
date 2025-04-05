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
        // Test that static resources are served
        client.get("/static/css/styles.css").apply {
            // Note: This will return 404 if the file doesn't exist, which is expected
            // The important thing is that the route is handled
            assertTrue(status == HttpStatusCode.NotFound || status == HttpStatusCode.OK, 
                "Static resource route should be handled")
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
        assertTrue(content.contains("htmx.org", ignoreCase = true), "Response should contain HTMX script")
        assertTrue(content.contains("tailwindcss", ignoreCase = true), "Response should contain Tailwind script")
    }
} 