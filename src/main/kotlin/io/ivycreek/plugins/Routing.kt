package io.ivycreek.plugins

import io.ivycreek.content.index
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

private const val SITE_URL = "https://htmx-ktor.cursedfunction.io/"

fun Application.configureRouting() {
    routing {
        get("/sitemap.xml") {
            call.respondText(
                text = """
                    <?xml version="1.0" encoding="UTF-8"?>
                    <urlset xmlns="http://www.sitemaps.org/schemas/sitemap/0.9">
                      <url>
                        <loc>$SITE_URL</loc>
                      </url>
                    </urlset>
                """.trimIndent(),
                contentType = ContentType.Application.Xml,
            )
        }
        get("/robots.txt") {
            call.respondText(
                text = """
                    User-agent: *
                    Allow: /
                    Sitemap: ${SITE_URL}sitemap.xml
                """.trimIndent(),
                contentType = ContentType.Text.Plain,
            )
        }
        staticResources("/", "static") {
            preCompressed(CompressedFileType.GZIP)
        }
        get("/") {
            call.respondHtml(HttpStatusCode.OK) {
                index()
            }
        }
    }
}
