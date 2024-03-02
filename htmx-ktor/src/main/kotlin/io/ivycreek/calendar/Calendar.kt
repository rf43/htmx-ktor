package io.ivycreek.calendar

import io.ivycreek.content.pageHeader
import kotlinx.html.*

private data class CalendarEvent(
    val title: String,
    val startDate: String,
    val endDate: String,
    val location: String
)

private val calendarEvents = listOf(
    CalendarEvent(
        "Back End Development",
        "2021-10-01",
        "2021-10-01",
        "Charlottesville, VA"
    ),
    CalendarEvent(
        "Front End Development",
        "2024-01-21",
        "2024-02-21",
        "Charlottesville, VA"
    )
)

fun FlowContent.calendar() = div {
    id = "content"
    pageHeader("Calendar")
    main {
        classes = setOf("mx-auto", "max-w-7xl", "py-6", "sm:px-6", "lg:px-8")
        div {
            classes = setOf("bg-white", "shadow", "overflow-hidden", "sm:rounded-lg")
            div {
                classes = setOf("flex", "flex-col", "w-full")
                div {
                    classes = setOf("overflow-x-auto", "sm:-mx-6", "lg:-mx-8")
                    div {
                        classes = setOf("py-2", "align-middle", "inline-block", "min-w-full", "sm:px-6", "lg:px-8")
                        div {
                            classes = setOf("shadow", "overflow-hidden", "border-b", "border-gray-200", "sm:rounded-lg")
                            table {
                                classes = setOf("min-w-full", "divide-y", "divide-gray-200")
                                thead {
                                    tr {
                                        th {
                                            classes = setOf(
                                                "px-6",
                                                "py-3",
                                                "text-left",
                                                "text-xs",
                                                "font-medium",
                                                "text-gray-500",
                                                "uppercase",
                                                "tracking-wider"
                                            )
                                            +"Title"
                                        }
                                        th {
                                            classes = setOf(
                                                "px-6",
                                                "py-3",
                                                "text-left",
                                                "text-xs",
                                                "font-medium",
                                                "text-gray-500",
                                                "uppercase",
                                                "tracking-wider"
                                            )
                                            +"Start Date"
                                        }
                                        th {
                                            classes = setOf(
                                                "px-6",
                                                "py-3",
                                                "text-left",
                                                "text-xs",
                                                "font-medium",
                                                "text-gray-500",
                                                "uppercase",
                                                "tracking-wider"
                                            )
                                            +"End Date"
                                        }
                                        th {
                                            classes = setOf(
                                                "px-6",
                                                "py-3",
                                                "text-left",
                                                "text-xs",
                                                "font-medium",
                                                "text-gray-500",
                                                "uppercase",
                                                "tracking-wider"
                                            )
                                            +"Location"
                                        }
                                    }
                                }
                                tbody {
                                    calendarEvents.map {
                                        tr {
                                            classes = setOf("bg-white", "hover:bg-gray-50")
                                            td {
                                                classes = setOf("px-6", "py-4", "whitespace-nowrap")
                                                div {
                                                    classes = setOf("flex", "items-center")
                                                    div {
                                                        classes = setOf("ml-4")
                                                        div {
                                                            classes = setOf("text-sm", "font-medium", "text-gray-900")
                                                            +it.title
                                                        }
                                                    }
                                                }
                                            }
                                            td {
                                                classes = setOf("px-6", "py-4", "whitespace-nowrap")
                                                div {
                                                    classes = setOf("text-sm", "text-gray-900")
                                                    +it.startDate
                                                }
                                            }
                                            td {
                                                classes = setOf("px-6", "py-4", "whitespace-nowrap")
                                                div {
                                                    classes = setOf("text-sm", "text-gray-900")
                                                    +it.endDate
                                                }
                                            }
                                            td {
                                                classes = setOf("px-6", "py-4", "whitespace-nowrap")
                                                div {
                                                    classes = setOf("text-sm", "text-gray-900")
                                                    +it.location
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}