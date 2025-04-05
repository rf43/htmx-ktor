package io.ivycreek.team

import io.ivycreek.content.pageHeader
import kotlinx.html.*

internal fun FlowContent.team() = div {
    id = "content"
    pageHeader("Team")
    main {
        classes = setOf("mx-auto", "max-w-7xl", "py-6", "sm:px-6", "lg:px-8")
        p {
            classes = setOf("text-2xl", "text-gray-900")
            +("The team at PNutz is a group of dedicated professionals who are passionate about the work they do. " +
                    "We are committed to providing the best possible service to our clients and are always looking " +
                    "for ways to improve our processes and services.")
        }
    }
}