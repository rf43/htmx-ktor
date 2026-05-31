package io.ivycreek.calendar

import io.ivycreek.content.pageHeader
import kotlinx.html.*

internal data class CalendarEvent(
    val id: String,
    val title: String,
    val startDate: String,
    val endDate: String,
    val location: String,
    val summary: String,
    val outcome: String,
    val htmxCue: String
)

internal val calendarEvents = listOf(
    CalendarEvent(
        id = "ktor-routing",
        title = "Ktor routing walkthrough",
        startDate = "2026-06-03",
        endDate = "2026-06-03",
        location = "Server",
        summary = "Trace a request from the browser through a typed Ktor route and into a kotlinx.html response.",
        outcome = "You can see where the server owns routing, rendering, validation, and testable behavior.",
        htmxCue = "Clicking this row fetches only the detail panel instead of replacing the full page."
    ),
    CalendarEvent(
        id = "htmx-fragment-swap",
        title = "htmx fragment swap demo",
        startDate = "2026-06-10",
        endDate = "2026-06-10",
        location = "Browser",
        summary = "Compare hx-get, hx-target, and hx-swap while the browser updates one focused part of the UI.",
        outcome = "The page keeps its shell, navigation, and styling while new server-rendered content appears in place.",
        htmxCue = "This is the same pattern the top-level navigation uses, applied to a smaller region."
    ),
    CalendarEvent(
        id = "route-contract-tests",
        title = "Route contract test review",
        startDate = "2026-06-17",
        endDate = "2026-06-17",
        location = "Test suite",
        summary = "Review the Ktor testApplication checks that lock down routes, fragments, assets, and htmx contracts.",
        outcome = "The interaction remains easy to verify because each endpoint returns ordinary HTML.",
        htmxCue = "The detail endpoint can be tested directly, without needing a browser automation layer."
    )
)

internal fun findCalendarEvent(eventId: String?) = calendarEvents.find { it.id == eventId }

fun FlowContent.calendar(selectedEventId: String = calendarEvents.first().id) = div {
    val selectedEvent = findCalendarEvent(selectedEventId) ?: calendarEvents.first()

    id = "content"
    pageHeader("Calendar")
    main {
        classes = setOf("mx-auto", "max-w-7xl", "space-y-6", "py-8", "px-4", "sm:px-6", "lg:px-8")
        p {
            classes = setOf("max-w-3xl", "text-lg", "leading-8", "text-gray-700")
            +"This table is rendered on the server from Kotlin data, then delivered as a complete HTML fragment when htmx asks for the calendar component."
        }
        div {
            classes = setOf("grid", "gap-6", "lg:grid-cols-3")
            div {
                classes = setOf("bg-white", "shadow", "overflow-hidden", "sm:rounded-lg", "lg:col-span-2")
                div {
                    classes = setOf("flex", "flex-col", "w-full")
                    div {
                        classes = setOf("overflow-x-auto")
                        div {
                            classes = setOf("inline-block", "min-w-full", "align-middle")
                            table {
                                classes = setOf("min-w-full", "divide-y", "divide-gray-200")
                                thead {
                                    classes = setOf("bg-gray-50")
                                    tr {
                                        calendarHeaders.forEach { heading ->
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
                                                +heading
                                            }
                                        }
                                    }
                                }
                                tbody {
                                    classes = setOf("divide-y", "divide-gray-200", "bg-white")
                                    calendarEvents.map {
                                        tr {
                                            classes = setOf("bg-white", "hover:bg-gray-50")
                                            td {
                                                classes = setOf("px-6", "py-4", "whitespace-nowrap")
                                                div {
                                                    classes = setOf("flex", "items-center")
                                                    button {
                                                        type = ButtonType.button
                                                        attributes["hx-get"] = "/components/calendar/${it.id}"
                                                        attributes["hx-target"] = "#calendar-event-detail"
                                                        attributes["hx-swap"] = "innerHTML"
                                                        classes = setOf("text-left", "text-sm", "font-medium", "text-cyan-700", "hover:text-cyan-900", "focus:outline-none", "focus:ring-2", "focus:ring-cyan-600", "focus:ring-offset-2")
                                                        +it.title
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
            aside {
                id = "calendar-event-detail"
                classes = setOf("rounded-lg", "border", "border-gray-200", "bg-white", "p-6", "shadow-sm")
                calendarEventDetail(selectedEvent)
            }
        }
    }
}

private val calendarHeaders = listOf("Title", "Start Date", "End Date", "Location")

internal fun FlowContent.calendarEventDetail(event: CalendarEvent) = article {
    p {
        classes = setOf("text-sm", "font-semibold", "uppercase", "tracking-wider", "text-cyan-700")
        +"Event details"
    }
    h2 {
        classes = setOf("mt-3", "text-2xl", "font-bold", "tracking-tight", "text-gray-950")
        +event.title
    }
    dl {
        classes = setOf("mt-5", "grid", "grid-cols-2", "gap-4", "text-sm")
        div {
            p {
                classes = setOf("font-medium", "text-gray-500")
                +"Date"
            }
            p {
                classes = setOf("mt-1", "text-gray-900")
                +event.startDate
            }
        }
        div {
            p {
                classes = setOf("font-medium", "text-gray-500")
                +"Focus"
            }
            p {
                classes = setOf("mt-1", "text-gray-900")
                +event.location
            }
        }
    }
    p {
        classes = setOf("mt-5", "text-sm", "leading-6", "text-gray-700")
        +event.summary
    }
    div {
        classes = setOf("mt-5", "rounded-md", "bg-slate-50", "p-4")
        h3 {
            classes = setOf("text-sm", "font-semibold", "text-gray-950")
            +"Expected outcome"
        }
        p {
            classes = setOf("mt-2", "text-sm", "leading-6", "text-gray-700")
            +event.outcome
        }
    }
    div {
        classes = setOf("mt-4", "rounded-md", "bg-cyan-50", "p-4")
        h3 {
            classes = setOf("text-sm", "font-semibold", "text-cyan-950")
            +"htmx behavior"
        }
        p {
            classes = setOf("mt-2", "text-sm", "leading-6", "text-cyan-900")
            +event.htmxCue
        }
    }
}
