package io.ivycreek.projects

import io.ivycreek.content.pageHeader
import kotlinx.html.*

private data class DemoProject(
    val name: String,
    val status: String,
    val summary: String
)

private val demoProjects = listOf(
    DemoProject(
        "Component navigation",
        "Running",
        "Each tab calls a Ktor component route with hx-get and swaps the returned HTML into the page shell."
    ),
    DemoProject(
        "Server-rendered tables",
        "Ready",
        "The calendar page renders structured data on the server, so the browser receives complete semantic markup."
    ),
    DemoProject(
        "Progressive enhancement",
        "Built in",
        "Links keep real href values, which means routes stay bookmarkable and graceful when JavaScript is unavailable."
    )
)

internal fun FlowContent.projects() = div {
    id = "content"
    pageHeader("Projects")
    main {
        classes = setOf("mx-auto", "max-w-7xl", "space-y-6", "py-8", "px-4", "sm:px-6", "lg:px-8")
        p {
            classes = setOf("max-w-3xl", "text-lg", "leading-8", "text-gray-700")
            +"These project examples show how much interface behavior can be delivered with ordinary Ktor routes and small htmx attributes."
        }
        div {
            classes = setOf("grid", "gap-5", "lg:grid-cols-3")
            demoProjects.forEach { project ->
                article {
                    classes = setOf("rounded-lg", "border", "border-gray-200", "bg-white", "p-6", "shadow-sm")
                    div {
                        classes = setOf("flex", "items-center", "justify-between", "gap-4")
                        h2 {
                            classes = setOf("text-lg", "font-semibold", "text-gray-950")
                            +project.name
                        }
                        span {
                            classes = setOf("rounded-full", "bg-cyan-50", "px-3", "py-1", "text-xs", "font-medium", "text-cyan-800")
                            +project.status
                        }
                    }
                    p {
                        classes = setOf("mt-4", "text-sm", "leading-6", "text-gray-700")
                        +project.summary
                    }
                }
            }
        }
    }
}
