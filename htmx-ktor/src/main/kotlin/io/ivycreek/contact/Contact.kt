package io.ivycreek.contact

import io.ivycreek.content.pageHeader
import kotlinx.html.*

fun FlowContent.contact() = div {
    id = "content"
    pageHeader("Contact")
    main {
        classes = setOf("mx-auto", "max-w-7xl", "py-6", "sm:px-6", "lg:px-8")
        p {
            classes = setOf("text-2xl", "text-gray-900")
            +("Contact us at")
        }
    }
}