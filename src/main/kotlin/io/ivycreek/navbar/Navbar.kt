package io.ivycreek.navbar

import kotlinx.html.*

private const val DASHBOARD_PATH = "/components/dashboard"
private const val SOURCE_REPOSITORY_URL = "https://github.com/rf43/htmx-ktor"

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
private val sourceLinkClasses = navBaseClasses + setOf(
    "inline-flex",
    "items-center",
    "gap-1.5",
    "text-gray-950",
    "underline",
    "decoration-gray-700",
    "underline-offset-4"
)

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
                classes = setOf("flex", "min-w-0", "flex-1", "flex-wrap", "items-center", "gap-2", "sm:ml-6")
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
                a {
                    href = SOURCE_REPOSITORY_URL
                    target = "_blank"
                    rel = "noopener noreferrer"
                    classes = sourceLinkClasses
                    githubIcon()
                    +"Source"
                }
            }
        }
    }
}

private fun FlowContent.githubIcon() {
    span {
        attributes["aria-hidden"] = "true"
        classes = setOf("inline-flex")
        unsafe {
            +"""<svg class="h-4 w-4 shrink-0 fill-current" viewBox="0 0 16 16"><path d="M8 0C3.58 0 0 3.58 0 8c0 3.54 2.29 6.53 5.47 7.59.4.07.55-.17.55-.38 0-.19-.01-.82-.01-1.49-2.01.37-2.53-.49-2.69-.94-.09-.23-.48-.94-.82-1.13-.28-.15-.68-.52-.01-.53.63-.01 1.08.58 1.23.82.72 1.21 1.87.87 2.33.66.07-.52.28-.87.51-1.07-1.78-.2-3.64-.89-3.64-3.95 0-.87.31-1.59.82-2.15-.08-.2-.36-1.02.08-2.12 0 0 .67-.21 2.2.82A7.49 7.49 0 0 1 8 3.86c.68 0 1.36.09 2 .27 1.53-1.04 2.2-.82 2.2-.82.44 1.1.16 1.92.08 2.12.51.56.82 1.27.82 2.15 0 3.07-1.87 3.75-3.65 3.95.29.25.54.73.54 1.48 0 1.07-.01 1.93-.01 2.2 0 .21.15.46.55.38A8.013 8.013 0 0 0 16 8c0-4.42-3.58-8-8-8Z"></path></svg>"""
        }
    }
}
