package io.ivycreek.content

import kotlinx.html.*

fun FlowContent.pageHeader(title: String) = header {
    classes = setOf("bg-white", "shadow")
    div {
        classes = setOf("max-w-7xl", "mx-auto", "py-6", "px-4", "sm:px-6", "lg:px-8")
        h1 {
            classes = setOf("text-3xl", "font-bold", "tracking-tight", "text-gray-900")
            +title
        }
    }
}