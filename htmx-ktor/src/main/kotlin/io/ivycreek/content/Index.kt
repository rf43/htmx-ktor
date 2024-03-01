package io.ivycreek.content

import io.ivycreek.dashboard.dashboard
import io.ivycreek.navbar.navbar
import kotlinx.html.*

fun HTML.index() {
    head {
        title {
            +"IvyCreek + htmx + Tailwind Example"
        }
        script { src = "https://cdn.tailwindcss.com" }
        script { src = "https://unpkg.com/htmx.org@1.9.10" }
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