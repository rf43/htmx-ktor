package io.ivycreek.navbar

import kotlinx.html.*

private const val DASHBOARD_PATH = "/components/dashboard"

private val navBaseClasses = setOf(
    "hover:bg-gray-700",
    "hover:text-white",
    "rounded-md",
    "px-3",
    "py-2",
    "text-sm",
    "font-medium"
)

private val activeNavClasses = navBaseClasses + setOf("bg-gray-800", "text-white")
private val inactiveNavClasses = navBaseClasses + setOf("text-gray-700")

fun FlowContent.navbar(activePath: String = DASHBOARD_PATH, vararg tabs: String) = nav {
    classes = setOf("bg-gray-400")
    div {
        classes = setOf("mx-auto", "max-w-7xl", "px-4", "sm:px-6", "lg:px-8")
        div {
            classes = setOf("flex", "min-h-24", "flex-col", "gap-4", "py-4", "sm:flex-row", "sm:items-center")
            div {
                classes = setOf("flex", "items-center")
                img {
                    classes = setOf("h-14", "w-auto")
                    src = "/pnutz-logo-transparent.svg"
                    alt = "PNutz"
                }
            }
            div {
                classes = setOf("flex", "flex-wrap", "items-center", "gap-2", "sm:ml-6")
                tabs.map {
                    val path = "/components/${it.lowercase()}"
                    val active = path == activePath || (path == DASHBOARD_PATH && activePath == "/")
                    a {
                        attributes["data-nav-link"] = "true"
                        attributes["hx-get"] = path
                        attributes["hx-target"] = "#content"
                        attributes["hx-swap"] = "outerHTML"
                        attributes["hx-push-url"] = "true"
                        if (active) {
                            attributes["aria-current"] = "page"
                        }
                        href = path
                        classes = if (active) activeNavClasses else inactiveNavClasses
                        +it
                    }
                }
            }
        }
    }
}
