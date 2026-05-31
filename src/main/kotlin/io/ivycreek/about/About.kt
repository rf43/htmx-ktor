package io.ivycreek.about

import io.ivycreek.content.pageHeader
import kotlinx.html.*

fun FlowContent.about() = div {
    id = "content"
    pageHeader("About")
    main {
        classes = setOf("mx-auto", "max-w-7xl", "space-y-6", "py-8", "px-4", "sm:px-6", "lg:px-8")
        section {
            classes = setOf("rounded-lg", "border", "border-gray-200", "bg-white", "p-6", "shadow-sm")
            h2 {
                classes = setOf("text-2xl", "font-bold", "tracking-tight", "text-gray-950")
                +"Why pair Ktor with htmx?"
            }
            p {
                classes = setOf("mt-4", "max-w-4xl", "text-base", "leading-7", "text-gray-700")
                +("Ktor is a lightweight Kotlin server framework with explicit routing and strong test support. " +
                        "htmx complements it by moving common browser behavior back into HTML attributes, so " +
                        "the server can keep returning simple, cacheable, inspectable markup.")
            }
        }
        section {
            classes = setOf("grid", "gap-5", "md:grid-cols-2")
            div {
                classes = setOf("rounded-lg", "border", "border-gray-200", "bg-white", "p-6", "shadow-sm")
                h2 {
                    classes = setOf("text-lg", "font-semibold", "text-gray-950")
                    +"Less client state"
                }
                p {
                    classes = setOf("mt-3", "text-sm", "leading-6", "text-gray-700")
                    +"The browser asks for the next piece of UI and receives HTML, reducing the need for duplicated client models."
                }
            }
            div {
                classes = setOf("rounded-lg", "border", "border-gray-200", "bg-white", "p-6", "shadow-sm")
                h2 {
                    classes = setOf("text-lg", "font-semibold", "text-gray-950")
                    +"Better server leverage"
                }
                p {
                    classes = setOf("mt-3", "text-sm", "leading-6", "text-gray-700")
                    +"Authentication, validation, templates, and tests stay close to the Ktor code that already owns the request."
                }
            }
        }
    }
}
