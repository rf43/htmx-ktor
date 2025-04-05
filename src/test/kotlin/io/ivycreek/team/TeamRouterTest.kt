package io.ivycreek.team

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.*

class TeamRouterTest {
    @Test
    fun testTeamComponent() = testApplication {
        application {
            teamRouter()
        }
        client.get("/components/team").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertTrue(bodyAsText().contains("team"))
        }
    }

    @Test
    fun testTeamComponentContent() = testApplication {
        application {
            teamRouter()
        }
        val response = client.get("/components/team")
        val content = response.bodyAsText()
        
        // Verify HTML structure
        assertTrue(content.contains("<body>"))
        assertTrue(content.contains("</body>"))
        
        // Verify team-specific content
        assertTrue(content.contains("team"))
    }
} 