package io.ivycreek.content

import io.ivycreek.dashboard.dashboard
import io.ivycreek.navbar.navbar
import kotlinx.html.*

private const val HTMX_SCRIPT_URL = "https://cdn.jsdelivr.net/npm/htmx.org@2.0.10/dist/htmx.min.js"
private const val HTMX_SCRIPT_INTEGRITY = "sha384-H5SrcfygHmAuTDZphMHqBJLc3FhssKjG7w/CeCpFReSfwBWDTKpkzPP8c+cLsK+V"

fun HTML.index() {
    lang = "en"
    head {
        title {
            +"PNutz + htmx + Tailwind Example"
        }
        script {
            src = HTMX_SCRIPT_URL
            attributes["integrity"] = HTMX_SCRIPT_INTEGRITY
            attributes["crossorigin"] = "anonymous"
        }
        script { src = "https://cdn.tailwindcss.com" }
    }
    body {
        classes = setOf("h-full")
        div {
            classes = setOf("min-h-full")
            navbar("Dashboard", "Team", "Projects", "Calendar", "About", "Contact")
            dashboard()
        }
    }
}
