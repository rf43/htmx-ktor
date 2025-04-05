package io.ivycreek.dashboard

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.*

class DashboardRouterTest {
    @Test
    fun testDashboardComponent() = testApplication {
        application {
            dashboardRouter()
        }
        client.get("/components/dashboard").apply {
            assertEquals(HttpStatusCode.OK, status)
            val content = bodyAsText()
            assertTrue(content.contains("<body>", ignoreCase = true), "Response should contain body tag")
            assertTrue(content.contains("</body>", ignoreCase = true), "Response should contain closing body tag")
        }
    }

    @Test
    fun testDashboardComponentContent() = testApplication {
        application {
            dashboardRouter()
        }
        val response = client.get("/components/dashboard")
        val content = response.bodyAsText()
        
        // Verify HTML structure
        assertTrue(content.contains("<body>", ignoreCase = true), "Response should contain body tag")
        assertTrue(content.contains("</body>", ignoreCase = true), "Response should contain closing body tag")
        
        // Verify dashboard-specific content
        assertTrue(content.contains("Dashboard", ignoreCase = true), "Response should contain dashboard content")
    }
} 