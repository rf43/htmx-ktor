package io.ivycreek.team

import io.ivycreek.content.pageHeader
import kotlinx.html.*

private data class Capability(
    val title: String,
    val detail: String
)

private val capabilities = listOf(
    Capability("Route owners", "Ktor handlers expose clear endpoints for whole pages and reusable fragments."),
    Capability("Markup owners", "Kotlin builders keep layout, conditionals, and loops in one type-checked language."),
    Capability("Interaction owners", "htmx attributes describe user intent directly on the elements that trigger it.")
)

internal fun FlowContent.team() = div {
    id = "content"
    pageHeader("Team")
    main {
        classes = setOf("mx-auto", "max-w-7xl", "space-y-6", "py-8", "px-4", "sm:px-6", "lg:px-8")
        p {
            classes = setOf("max-w-3xl", "text-lg", "leading-8", "text-gray-700")
            +("A Ktor and htmx stack keeps the team surface area small: backend developers can ship polished " +
                    "HTML interactions, frontend work stays close to the markup, and tests can assert the same " +
                    "fragments users receive.")
        }
        div {
            classes = setOf("grid", "gap-5", "md:grid-cols-3")
            capabilities.forEach { capability ->
                article {
                    classes = setOf("rounded-lg", "border", "border-gray-200", "bg-white", "p-6", "shadow-sm")
                    h2 {
                        classes = setOf("text-lg", "font-semibold", "text-gray-950")
                        +capability.title
                    }
                    p {
                        classes = setOf("mt-3", "text-sm", "leading-6", "text-gray-700")
                        +capability.detail
                    }
                }
            }
        }
    }
}
