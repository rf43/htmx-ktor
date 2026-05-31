package io.ivycreek.contact

import io.ivycreek.content.pageHeader
import kotlinx.html.*

fun FlowContent.contact() = div {
    id = "content"
    pageHeader("Contact")
    main {
        classes = setOf("mx-auto", "max-w-7xl", "space-y-6", "py-8", "px-4", "sm:px-6", "lg:px-8")
        section {
            classes = setOf("rounded-lg", "border", "border-gray-200", "bg-white", "p-6", "shadow-sm")
            h2 {
                classes = setOf("text-2xl", "font-bold", "tracking-tight", "text-gray-950")
                +"Try extending the demo"
            }
            p {
                classes = setOf("mt-4", "max-w-3xl", "text-base", "leading-7", "text-gray-700")
                +("Add a form route, return validation errors as an HTML fragment, or stream a small server event " +
                        "panel. Those are natural next steps for seeing how Ktor and htmx keep interaction logic " +
                        "simple and server-centered.")
            }
            a {
                href = "/"
                classes = setOf("mt-6", "inline-flex", "items-center", "rounded-md", "bg-cyan-700", "px-4", "py-2", "text-sm", "font-semibold", "text-white", "shadow-sm", "hover:bg-cyan-800")
                +"Back to the showcase"
            }
        }
    }
}
