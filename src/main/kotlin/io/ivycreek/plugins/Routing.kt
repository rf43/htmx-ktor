package io.ivycreek.plugins

import io.ivycreek.content.index
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

private const val PUBLIC_SITE_URL_ENV = "PUBLIC_SITE_URL"
private const val DEFAULT_SITE_URL = "https://example.com/"

fun Application.configureRouting(siteUrl: String = configuredSiteUrl()) {
    val publicSiteUrl = normalizedSiteUrl(siteUrl)

    routing {
        get("/sitemap.xml") {
            call.respondText(
                text = """
                    <?xml version="1.0" encoding="UTF-8"?>
                    <urlset xmlns="http://www.sitemaps.org/schemas/sitemap/0.9">
                      <url>
                        <loc>$publicSiteUrl</loc>
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
                    Sitemap: ${publicSiteUrl}sitemap.xml
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

private fun configuredSiteUrl(): String =
    System.getenv(PUBLIC_SITE_URL_ENV) ?: DEFAULT_SITE_URL

private fun normalizedSiteUrl(siteUrl: String): String {
    val trimmedSiteUrl = siteUrl.trim().ifEmpty { DEFAULT_SITE_URL }
    return if (trimmedSiteUrl.endsWith("/")) trimmedSiteUrl else "$trimmedSiteUrl/"
}
