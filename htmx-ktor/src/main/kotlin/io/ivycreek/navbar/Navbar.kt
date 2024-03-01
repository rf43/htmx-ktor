package io.ivycreek.navbar

import kotlinx.html.*

fun FlowContent.navbar(vararg tabs: String) = nav {
    classes = setOf("bg-gray-800")
    div {
        classes = setOf("mx-auto", "max-w-7xl", "px-4", "sm:px-6", "lg:px-8")
        div {
            classes = setOf("flex", "h-24", "items-center", "justify-between")
            div {
                classes = setOf("flex", "items-center")
                div {
                    img {
                        classes = setOf("h-48", "w-auto")
                        src = "logo.png"
                        alt = "IvyCreek"
                    }
                }
                div {
                    classes = setOf("flex-shrink-0")
                    div {
                        classes = setOf("block")
                        div {
                            classes = setOf("ml-10", "flex", "items-baseline", "space-x-4")
                            tabs.map {
                                a {
                                    attributes["hx-get"] = "/components/${it.lowercase()}"
                                    attributes["hx-target"] = "#content"
                                    href = "#"
                                    classes = setOf(
                                        "text-gray-300",
                                        "hover:bg-gray-700",
                                        "hover:text-white",
                                        "rounded-md",
                                        "px-3",
                                        "py-2",
                                        "text-sm",
                                        "font-medium"
                                    )
                                    +it
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}