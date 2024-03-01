package io.ivycreek.plugins

import io.ivycreek.content.index
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
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