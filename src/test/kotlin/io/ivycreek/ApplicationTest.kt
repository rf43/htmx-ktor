package io.ivycreek

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.*

class ApplicationTest {
    @Test
    fun `root route renders the application shell`() = testApplication {
        application {
            module()
        }
        client.get("/").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals(ContentType.Text.Html, contentType()?.withoutParameters())
            assertTrue(bodyAsText().contains("<body"))
            assertTrue(bodyAsText().contains("</body>"))
        }
    }

    @Test
    fun `module registers all component routes`() = testApplication {
        application {
            module()
        }

        // Test each component route
        listOf(
            "/components/dashboard",
            "/components/team",
            "/components/incidents",
            "/components/projects",
            "/components/about",
            "/components/contact"
        ).forEach { route ->
            client.get(route).apply {
                assertEquals(HttpStatusCode.OK, status)
                assertEquals(ContentType.Text.Html, contentType()?.withoutParameters())
                assertTrue(bodyAsText().contains("<body"))
                assertTrue(bodyAsText().contains("</body>"))
            }
        }
    }
}
