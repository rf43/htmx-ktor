package io.ivycreek.content

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.request.*
import kotlinx.html.FlowContent
import kotlinx.html.body

private const val HTMX_REQUEST_HEADER = "HX-Request"

fun ApplicationRequest.isHtmxRequest() = headers[HTMX_REQUEST_HEADER] == "true"

suspend fun ApplicationCall.respondComponent(fragment: FlowContent.() -> Unit) {
    if (request.isHtmxRequest()) {
        respondHtml(HttpStatusCode.OK) {
            body {
                fragment()
            }
        }
    } else {
        respondHtml(HttpStatusCode.OK) {
            index(request.path(), fragment)
        }
    }
}
