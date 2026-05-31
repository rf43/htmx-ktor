package io.ivycreek.dashboard

import io.ivycreek.content.pageHeader
import kotlinx.html.*

private data class ShowcasePillar(
    val label: String,
    val title: String,
    val body: String
)

private data class FlowStep(
    val number: String,
    val title: String,
    val body: String
)

private val showcasePillars = listOf(
    ShowcasePillar(
        "01",
        "Ktor owns the request",
        "Routes stay small, typed, and testable while the server decides which HTML fragment to return."
    ),
    ShowcasePillar(
        "02",
        "htmx owns the interaction",
        "Navigation, history, and targeted swaps happen through attributes instead of a client-side router."
    ),
    ShowcasePillar(
        "03",
        "Kotlin owns the markup",
        "kotlinx.html keeps page structure close to application code with compiler-checked HTML builders."
    )
)

private val flowSteps = listOf(
    FlowStep(
        "1",
        "A user clicks a tab",
        "The link still has a normal href, so the page remains useful without custom JavaScript."
    ),
    FlowStep(
        "2",
        "htmx requests a component route",
        "hx-get calls endpoints like /components/projects and targets only the #content region."
    ),
    FlowStep(
        "3",
        "Ktor returns a server-rendered fragment",
        "The response is plain HTML, which makes rendering fast, debuggable, and easy to test."
    )
)

fun FlowContent.dashboard() = div {
    id = "content"
    pageHeader("Ktor + htmx Showcase")
    main {
        classes = setOf("mx-auto", "max-w-7xl", "space-y-10", "py-8", "px-4", "sm:px-6", "lg:px-8")
        section {
            classes = setOf("grid", "gap-8", "lg:grid-cols-3")
            div {
                classes = setOf("lg:col-span-2")
                p {
                    classes = setOf("text-sm", "font-semibold", "uppercase", "tracking-wider", "text-cyan-700")
                    +"Server-rendered interactivity"
                }
                h2 {
                    classes = setOf("mt-3", "text-4xl", "font-bold", "tracking-tight", "text-gray-950")
                    +"Build fast web screens without turning every page into a JavaScript app."
                }
                p {
                    classes = setOf("mt-5", "max-w-3xl", "text-lg", "leading-8", "text-gray-700")
                    +("This sample uses Ktor for routing and HTML generation, then lets htmx progressively enhance " +
                            "navigation by swapping server-rendered fragments into the existing page shell.")
                }
            }
            div {
                classes = setOf("rounded-lg", "border", "border-gray-200", "bg-white", "p-6", "shadow-sm")
                h3 {
                    classes = setOf("text-base", "font-semibold", "text-gray-950")
                    +"What this page demonstrates"
                }
                ul {
                    classes = setOf("mt-4", "space-y-3", "text-sm", "leading-6", "text-gray-700")
                    li { +"Shared layout served once from the root Ktor route" }
                    li { +"Component routes that return focused HTML fragments" }
                    li { +"htmx attributes for fetch, target, and browser history" }
                    li { +"Tests that lock down the navigation contract" }
                }
            }
        }

        section {
            classes = setOf("grid", "gap-5", "md:grid-cols-3")
            showcasePillars.forEach { pillar ->
                article {
                    classes = setOf("rounded-lg", "border", "border-gray-200", "bg-white", "p-6", "shadow-sm")
                    p {
                        classes = setOf("text-sm", "font-semibold", "text-cyan-700")
                        +pillar.label
                    }
                    h3 {
                        classes = setOf("mt-3", "text-lg", "font-semibold", "text-gray-950")
                        +pillar.title
                    }
                    p {
                        classes = setOf("mt-3", "text-sm", "leading-6", "text-gray-700")
                        +pillar.body
                    }
                }
            }
        }

        section {
            classes = setOf("rounded-lg", "border", "border-gray-200", "bg-white", "p-6", "shadow-sm")
            div {
                classes = setOf("flex", "flex-col", "gap-3", "sm:flex-row", "sm:items-end", "sm:justify-between")
                div {
                    h2 {
                        classes = setOf("text-2xl", "font-bold", "tracking-tight", "text-gray-950")
                        +"How the request flow works"
                    }
                    p {
                        classes = setOf("mt-2", "max-w-3xl", "text-sm", "leading-6", "text-gray-700")
                        +"The browser gets useful HTML at every step. htmx improves the experience without hiding the server contract."
                    }
                }
                code {
                    classes = setOf("rounded-md", "bg-slate-900", "px-3", "py-2", "text-sm", "text-cyan-100")
                    +"hx-target=\"#content\""
                }
            }
            ol {
                classes = setOf("mt-6", "grid", "gap-4", "md:grid-cols-3")
                flowSteps.forEach { step ->
                    li {
                        classes = setOf("rounded-md", "bg-slate-50", "p-4")
                        span {
                            classes = setOf("inline-flex", "h-8", "w-8", "items-center", "justify-center", "rounded-full", "bg-cyan-700", "text-sm", "font-bold", "text-white")
                            +step.number
                        }
                        h3 {
                            classes = setOf("mt-4", "text-base", "font-semibold", "text-gray-950")
                            +step.title
                        }
                        p {
                            classes = setOf("mt-2", "text-sm", "leading-6", "text-gray-700")
                            +step.body
                        }
                    }
                }
            }
        }
    }
}
