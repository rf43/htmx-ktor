package io.ivycreek.dashboard

import io.ivycreek.content.pageHeader
import kotlinx.html.*

fun FlowContent.dashboard() = div {
    id = "content"
    pageHeader("Dashboard")
    main {
        classes = setOf("mx-auto", "max-w-7xl", "py-6", "sm:px-6", "lg:px-8")
        p {
            classes = setOf("text-2xl", "text-gray-900")
            +("Welcome to the PNutz Dashboard. This is where you can find all the information you need to " +
                    "manage your projects, team, and calendar.")
        }
    }
}