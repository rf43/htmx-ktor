package io.ivycreek.incidents

import io.ivycreek.content.pageHeader
import io.ktor.http.Parameters
import io.ktor.http.formUrlEncode
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import kotlinx.html.*

internal const val INCIDENTS_PATH = "/components/incidents"
internal const val INCIDENT_ACTIVITY_PATH = "$INCIDENTS_PATH/activity"
private const val INCIDENT_WORKSPACE_ID = "incident-workspace"
private const val INCIDENT_DETAIL_ID = "incident-detail"
private const val INCIDENT_FILTERS_ID = "incident-filters"
private const val INCIDENT_ACTIVITY_ID = "incident-activity"
private const val PAGE_SIZE = 3
private val ACTIVITY_CHECK_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss 'UTC'").withZone(ZoneOffset.UTC)

internal enum class IncidentStatus(val queryValue: String, val label: String) {
    OPEN("open", "Open"),
    INVESTIGATING("investigating", "Investigating"),
    MONITORING("monitoring", "Monitoring"),
    RESOLVED("resolved", "Resolved")
}

internal enum class IncidentSeverity(val queryValue: String, val label: String) {
    CRITICAL("critical", "Critical"),
    HIGH("high", "High"),
    MEDIUM("medium", "Medium"),
    LOW("low", "Low")
}

internal enum class IncidentSort(val queryValue: String, val label: String) {
    NEWEST("newest", "Newest reported"),
    SEVERITY("severity", "Highest severity"),
    STATUS("status", "Status")
}

internal data class Incident(
    val id: String,
    val title: String,
    val service: String,
    val environment: String,
    val severity: IncidentSeverity,
    val status: IncidentStatus,
    val reportedAt: String,
    val owner: String,
    val summary: String,
    val impact: String,
    val latestUpdate: String
)

internal data class IncidentActivity(
    val incidentId: String,
    val occurredAt: String,
    val title: String,
    val summary: String
)

internal data class IncidentQuery(
    val search: String = "",
    val status: IncidentStatus? = null,
    val severity: IncidentSeverity? = null,
    val sort: IncidentSort = IncidentSort.NEWEST,
    val page: Int = 1
) {
    fun queuePath(page: Int = this.page): String = queryPath(INCIDENTS_PATH, page)

    fun detailPath(incidentId: String): String = queryPath("$INCIDENTS_PATH/$incidentId", page)

    fun containing(incident: Incident): IncidentQuery {
        val normalizedFilters = copy(
            search = search.takeIf { incident.matchesSearch(it) }.orEmpty(),
            status = status?.takeIf { it == incident.status },
            severity = severity?.takeIf { it == incident.severity },
            page = 1
        )
        val incidentIndex = matchingIncidents(normalizedFilters).indexOfFirst { it.id == incident.id }
        check(incidentIndex >= 0) { "Normalized incident query must contain ${incident.id}" }
        return normalizedFilters.copy(page = incidentIndex / PAGE_SIZE + 1)
    }

    private fun queryPath(path: String, page: Int): String {
        val parameters = buildList {
            if (search.isNotBlank()) add("q" to search)
            status?.let { add("status" to it.queryValue) }
            severity?.let { add("severity" to it.queryValue) }
            if (sort != IncidentSort.NEWEST) add("sort" to sort.queryValue)
            if (page > 1) add("page" to page.toString())
        }
        return if (parameters.isEmpty()) path else "$path?${parameters.formUrlEncode()}"
    }

    companion object {
        fun from(parameters: Parameters) = IncidentQuery(
            search = parameters["q"]?.trim().orEmpty(),
            status = IncidentStatus.entries.find { it.queryValue == parameters["status"] },
            severity = IncidentSeverity.entries.find { it.queryValue == parameters["severity"] },
            sort = IncidentSort.entries.find { it.queryValue == parameters["sort"] } ?: IncidentSort.NEWEST,
            page = parameters["page"]?.toIntOrNull()?.coerceAtLeast(1) ?: 1
        )
    }
}

internal data class IncidentResults(
    val incidents: List<Incident>,
    val totalCount: Int,
    val page: Int,
    val totalPages: Int
)

internal val incidents = listOf(
    Incident(
        id = "inc-1048",
        title = "Checkout requests returning elevated errors",
        service = "Checkout API",
        environment = "Production",
        severity = IncidentSeverity.CRITICAL,
        status = IncidentStatus.INVESTIGATING,
        reportedAt = "2026-07-10 14:32 UTC",
        owner = "Payments on-call",
        summary = "A subset of checkout requests fail while the payment provider connection pool is saturated.",
        impact = "Approximately 18% of checkout attempts are returning HTTP 503 responses.",
        latestUpdate = "Traffic has been shifted to the secondary provider while connection reuse is investigated."
    ),
    Incident(
        id = "inc-1047",
        title = "Delayed inventory synchronization",
        service = "Inventory Sync",
        environment = "Production",
        severity = IncidentSeverity.HIGH,
        status = IncidentStatus.MONITORING,
        reportedAt = "2026-07-10 12:05 UTC",
        owner = "Fulfillment platform",
        summary = "Inventory changes are reaching storefronts later than the normal processing window.",
        impact = "Stock counts may be up to twelve minutes behind warehouse updates.",
        latestUpdate = "The queue has drained and synchronization latency is returning to baseline."
    ),
    Incident(
        id = "inc-1046",
        title = "Admin authentication timeouts",
        service = "Identity Gateway",
        environment = "Production",
        severity = IncidentSeverity.HIGH,
        status = IncidentStatus.OPEN,
        reportedAt = "2026-07-10 09:48 UTC",
        owner = "Identity team",
        summary = "Some administrators cannot complete authentication before the gateway request expires.",
        impact = "Admin access is intermittent; customer authentication is not affected.",
        latestUpdate = "Gateway traces point to a slow policy lookup in the production region."
    ),
    Incident(
        id = "inc-1045",
        title = "Search index refresh stalled",
        service = "Catalog Search",
        environment = "Staging",
        severity = IncidentSeverity.MEDIUM,
        status = IncidentStatus.INVESTIGATING,
        reportedAt = "2026-07-09 21:16 UTC",
        owner = "Catalog team",
        summary = "The staging catalog index has stopped accepting scheduled refresh jobs.",
        impact = "Staging search results do not include the newest test catalog entries.",
        latestUpdate = "A malformed fixture has been isolated and a clean rebuild is running."
    ),
    Incident(
        id = "inc-1044",
        title = "Email delivery latency above target",
        service = "Notification Worker",
        environment = "Production",
        severity = IncidentSeverity.MEDIUM,
        status = IncidentStatus.RESOLVED,
        reportedAt = "2026-07-09 17:40 UTC",
        owner = "Messaging team",
        summary = "Transactional emails were delayed by a backlog in the outbound worker queue.",
        impact = "Order confirmations arrived up to twenty minutes after purchase.",
        latestUpdate = "Worker capacity was restored and all delayed messages were delivered."
    ),
    Incident(
        id = "inc-1043",
        title = "Metrics labels missing from canary pods",
        service = "Telemetry Pipeline",
        environment = "Staging",
        severity = IncidentSeverity.LOW,
        status = IncidentStatus.OPEN,
        reportedAt = "2026-07-09 13:22 UTC",
        owner = "Observability team",
        summary = "New canary pods emit metrics without the deployment-version label.",
        impact = "Canary dashboards require manual filtering; alert evaluation remains healthy.",
        latestUpdate = "The deployment template is being compared with the last known good release."
    ),
    Incident(
        id = "inc-1042",
        title = "Webhook retries elevated",
        service = "Partner Webhooks",
        environment = "Production",
        severity = IncidentSeverity.MEDIUM,
        status = IncidentStatus.MONITORING,
        reportedAt = "2026-07-08 23:09 UTC",
        owner = "Integrations team",
        summary = "Partner callback deliveries are retrying more often after a network policy update.",
        impact = "Some partner systems receive events several minutes late.",
        latestUpdate = "The policy change was reverted and retry volume continues to fall."
    ),
    Incident(
        id = "inc-1041",
        title = "Report export formatting regression",
        service = "Reporting",
        environment = "Staging",
        severity = IncidentSeverity.LOW,
        status = IncidentStatus.RESOLVED,
        reportedAt = "2026-07-08 15:51 UTC",
        owner = "Analytics team",
        summary = "CSV exports in staging included duplicate column headings.",
        impact = "Internal acceptance testing was blocked; production exports were unaffected.",
        latestUpdate = "The formatter fix passed verification and the incident is resolved."
    )
)

internal val incidentActivities = listOf(
    IncidentActivity(
        incidentId = "inc-1048",
        occurredAt = "2026-07-10 14:47 UTC",
        title = "Mitigation started",
        summary = "Checkout traffic shifted to the secondary payment provider."
    ),
    IncidentActivity(
        incidentId = "inc-1047",
        occurredAt = "2026-07-10 12:38 UTC",
        title = "Recovery confirmed",
        summary = "Inventory synchronization latency returned to its normal range."
    ),
    IncidentActivity(
        incidentId = "inc-1046",
        occurredAt = "2026-07-10 10:11 UTC",
        title = "Investigation narrowed",
        summary = "Gateway traces isolated the slow production policy lookup."
    )
)

internal fun findIncident(incidentId: String?) = incidents.find { it.id == incidentId }

internal fun findIncidents(query: IncidentQuery): IncidentResults {
    val matching = matchingIncidents(query)

    val totalPages = maxOf(1, (matching.size + PAGE_SIZE - 1) / PAGE_SIZE)
    val page = query.page.coerceAtMost(totalPages)
    val firstIndex = (page - 1) * PAGE_SIZE
    return IncidentResults(
        incidents = matching.drop(firstIndex).take(PAGE_SIZE),
        totalCount = matching.size,
        page = page,
        totalPages = totalPages
    )
}

private fun matchingIncidents(query: IncidentQuery) = incidents
    .asSequence()
    .filter { it.matchesSearch(query.search) }
    .filter { query.status == null || it.status == query.status }
    .filter { query.severity == null || it.severity == query.severity }
    .sortedWith(incidentComparator(query.sort))
    .toList()

private fun Incident.matchesSearch(search: String) = search.isBlank() || listOf(
    id,
    title,
    service,
    environment,
    summary
).any { it.contains(search, ignoreCase = true) }

private fun incidentComparator(sort: IncidentSort): Comparator<Incident> = when (sort) {
    IncidentSort.NEWEST -> compareByDescending(Incident::reportedAt)
    IncidentSort.SEVERITY -> compareBy<Incident> { it.severity.ordinal }.thenByDescending(Incident::reportedAt)
    IncidentSort.STATUS -> compareBy<Incident> { it.status.ordinal }.thenByDescending(Incident::reportedAt)
}

internal fun FlowContent.incidentsPage(
    query: IncidentQuery,
    selectedIncidentId: String? = null,
    activityCheckedAt: Instant = Instant.now()
) = div {
    val results = findIncidents(query)
    val selectedIncident = selectedIncidentId?.let(::findIncident) ?: results.incidents.firstOrNull()

    id = "content"
    pageHeader("Incidents")
    main {
        classes = setOf("mx-auto", "max-w-7xl", "space-y-6", "py-8", "px-4", "sm:px-6", "lg:px-8")
        div {
            classes = setOf("max-w-3xl")
            p {
                classes = setOf("text-lg", "leading-8", "text-gray-700")
                +"Search, filter, sort, and page through incidents on the server. Each URL remains refreshable while htmx updates only the queue or detail region."
            }
        }
        incidentActivity(activityCheckedAt)
        incidentFilters(query)
        incidentWorkspace(query, results, selectedIncident)
    }
}

internal fun FlowContent.incidentActivity(checkedAt: Instant) = section {
    id = INCIDENT_ACTIVITY_ID
    attributes["hx-get"] = INCIDENT_ACTIVITY_PATH
    attributes["hx-trigger"] = "every 15s"
    attributes["hx-target"] = "this"
    attributes["hx-swap"] = "outerHTML"
    attributes["aria-labelledby"] = "incident-activity-heading"
    classes = setOf("rounded-lg", "border", "border-gray-200", "bg-white", "p-5", "shadow-sm")
    div {
        classes = setOf("flex", "flex-col", "gap-2", "sm:flex-row", "sm:items-start", "sm:justify-between")
        div {
            h2 {
                id = "incident-activity-heading"
                classes = setOf("text-lg", "font-semibold", "text-gray-950")
                +"Operational activity"
            }
            p {
                classes = setOf("mt-1", "max-w-3xl", "text-sm", "leading-6", "text-gray-600")
                +"htmx polls this Ktor fragment every 15 seconds when JavaScript is available. The current server-rendered snapshot remains readable without it."
            }
        }
        p {
            classes = setOf("shrink-0", "text-sm", "text-gray-600")
            +"Snapshot checked "
            time {
                attributes["datetime"] = checkedAt.toString()
                classes = setOf("font-semibold", "text-gray-900")
                +ACTIVITY_CHECK_FORMATTER.format(checkedAt)
            }
        }
    }
    ol {
        classes = setOf("mt-5", "grid", "gap-4", "md:grid-cols-3")
        incidentActivities.forEach { activity ->
            li {
                classes = setOf("rounded-md", "bg-slate-50", "p-4")
                p {
                    classes = setOf("text-xs", "font-medium", "uppercase", "tracking-wide", "text-gray-500")
                    +"${activity.incidentId.uppercase()} · ${activity.occurredAt}"
                }
                h3 {
                    classes = setOf("mt-2", "text-sm", "font-semibold", "text-gray-950")
                    +activity.title
                }
                p {
                    classes = setOf("mt-2", "text-sm", "leading-6", "text-gray-700")
                    +activity.summary
                }
            }
        }
    }
}

internal fun FlowContent.incidentWorkspace(
    query: IncidentQuery,
    results: IncidentResults = findIncidents(query),
    selectedIncident: Incident? = results.incidents.firstOrNull()
) = div {
    id = INCIDENT_WORKSPACE_ID
    classes = setOf("grid", "gap-6", "lg:grid-cols-3")
    incidentQueue(query, results)
    incidentDetail(selectedIncident)
}

private fun FlowContent.incidentFilters(query: IncidentQuery) = form(
    action = INCIDENTS_PATH,
    method = FormMethod.get
) {
    id = INCIDENT_FILTERS_ID
    attributes["hx-get"] = INCIDENTS_PATH
    attributes["hx-target"] = "#$INCIDENT_WORKSPACE_ID"
    attributes["hx-swap"] = "outerHTML"
    attributes["hx-push-url"] = "true"
    classes = setOf("rounded-lg", "border", "border-gray-200", "bg-white", "p-5", "shadow-sm")
    div {
        classes = setOf("grid", "gap-4", "md:grid-cols-2", "xl:grid-cols-5")
        label {
            classes = setOf("block", "xl:col-span-2")
            span {
                classes = setOf("text-sm", "font-medium", "text-gray-700")
                +"Search incidents"
            }
            input(InputType.search) {
                id = "incident-search"
                name = "q"
                value = query.search
                placeholder = "ID, title, service, or environment"
                attributes["hx-get"] = INCIDENTS_PATH
                attributes["hx-trigger"] = "input changed delay:300ms, search"
                attributes["hx-target"] = "#$INCIDENT_WORKSPACE_ID"
                attributes["hx-swap"] = "outerHTML"
                attributes["hx-include"] = "#$INCIDENT_FILTERS_ID"
                attributes["hx-push-url"] = "true"
                classes = inputClasses
            }
        }
        incidentSelect(
            label = "Status",
            name = "status",
            selectedValue = query.status?.queryValue,
            options = IncidentStatus.entries.map { it.queryValue to it.label }
        )
        incidentSelect(
            label = "Severity",
            name = "severity",
            selectedValue = query.severity?.queryValue,
            options = IncidentSeverity.entries.map { it.queryValue to it.label }
        )
        incidentSelect(
            label = "Sort",
            name = "sort",
            selectedValue = query.sort.queryValue,
            options = IncidentSort.entries.map { it.queryValue to it.label },
            includeBlank = false
        )
    }
    div {
        classes = setOf("mt-4", "flex", "flex-wrap", "items-center", "gap-3")
        button(type = ButtonType.submit) {
            classes = setOf("rounded-md", "bg-cyan-700", "px-4", "py-2", "text-sm", "font-semibold", "text-white", "hover:bg-cyan-800", "focus:outline-none", "focus:ring-2", "focus:ring-cyan-600", "focus:ring-offset-2")
            +"Apply filters"
        }
        a {
            href = INCIDENTS_PATH
            attributes["hx-get"] = INCIDENTS_PATH
            attributes["hx-target"] = "#content"
            attributes["hx-swap"] = "outerHTML"
            attributes["hx-push-url"] = "true"
            classes = setOf("text-sm", "font-semibold", "text-cyan-700", "hover:text-cyan-900", "underline", "underline-offset-4")
            +"Clear filters"
        }
    }
}

private fun FlowContent.incidentSelect(
    label: String,
    name: String,
    selectedValue: String?,
    options: List<Pair<String, String>>,
    includeBlank: Boolean = true
) = label {
    classes = setOf("block")
    span {
        classes = setOf("text-sm", "font-medium", "text-gray-700")
        +label
    }
    select {
        this.name = name
        attributes["hx-get"] = INCIDENTS_PATH
        attributes["hx-trigger"] = "change"
        attributes["hx-target"] = "#$INCIDENT_WORKSPACE_ID"
        attributes["hx-swap"] = "outerHTML"
        attributes["hx-include"] = "#$INCIDENT_FILTERS_ID"
        attributes["hx-push-url"] = "true"
        classes = inputClasses
        if (includeBlank) {
            option {
                value = ""
                selected = selectedValue == null
                +"All"
            }
        }
        options.forEach { (value, text) ->
            option {
                this.value = value
                selected = selectedValue == value
                +text
            }
        }
    }
}

private fun FlowContent.incidentQueue(query: IncidentQuery, results: IncidentResults) = section {
    classes = setOf("min-w-0", "overflow-hidden", "rounded-lg", "border", "border-gray-200", "bg-white", "shadow-sm", "lg:col-span-2")
    div {
        classes = setOf("flex", "flex-col", "gap-1", "border-b", "border-gray-200", "px-5", "py-4", "sm:flex-row", "sm:items-center", "sm:justify-between")
        h2 {
            classes = setOf("text-lg", "font-semibold", "text-gray-950")
            +"Incident queue"
        }
        p {
            classes = setOf("text-sm", "text-gray-600")
            +resultSummary(results)
        }
    }
    if (results.incidents.isEmpty()) {
        div {
            classes = setOf("px-5", "py-12", "text-center")
            h3 {
                classes = setOf("text-base", "font-semibold", "text-gray-950")
                +"No incidents match these filters"
            }
            p {
                classes = setOf("mt-2", "text-sm", "text-gray-600")
                +"Change or clear the filters to restore the queue."
            }
        }
    } else {
        div {
            classes = setOf("overflow-x-auto")
            table {
                classes = setOf("min-w-full", "divide-y", "divide-gray-200")
                thead {
                    classes = setOf("bg-gray-50")
                    tr {
                        listOf("Incident", "Service", "Severity", "Status", "Reported").forEach { heading ->
                            th {
                                classes = setOf("px-5", "py-3", "text-left", "text-xs", "font-medium", "uppercase", "tracking-wider", "text-gray-500")
                                +heading
                            }
                        }
                    }
                }
                tbody {
                    classes = setOf("divide-y", "divide-gray-200", "bg-white")
                    results.incidents.forEach { incident ->
                        val detailPath = query.copy(page = results.page).detailPath(incident.id)
                        tr {
                            classes = setOf("hover:bg-gray-50")
                            td {
                                classes = setOf("px-5", "py-4")
                                a {
                                    href = detailPath
                                    attributes["hx-get"] = detailPath
                                    attributes["hx-target"] = "#$INCIDENT_DETAIL_ID"
                                    attributes["hx-swap"] = "outerHTML"
                                    attributes["hx-push-url"] = "true"
                                    classes = setOf("font-semibold", "text-cyan-700", "hover:text-cyan-900", "focus:outline-none", "focus:ring-2", "focus:ring-cyan-600", "focus:ring-offset-2")
                                    +incident.title
                                }
                                p {
                                    classes = setOf("mt-1", "text-xs", "font-medium", "uppercase", "tracking-wide", "text-gray-500")
                                    +incident.id.uppercase()
                                }
                            }
                            td {
                                classes = setOf("px-5", "py-4", "text-sm", "text-gray-700")
                                +incident.service
                                span {
                                    classes = setOf("block", "text-xs", "text-gray-500")
                                    +incident.environment
                                }
                            }
                            td {
                                classes = setOf("px-5", "py-4")
                                incidentBadge(incident.severity.label, severityClasses(incident.severity))
                            }
                            td {
                                classes = setOf("px-5", "py-4")
                                incidentBadge(incident.status.label, statusClasses(incident.status))
                            }
                            td {
                                classes = setOf("px-5", "py-4", "text-sm", "text-gray-600")
                                +incident.reportedAt
                            }
                        }
                    }
                }
            }
        }
        incidentPagination(query, results)
    }
}

private fun FlowContent.incidentPagination(query: IncidentQuery, results: IncidentResults) = nav {
    attributes["aria-label"] = "Incident queue pages"
    classes = setOf("flex", "items-center", "justify-between", "border-t", "border-gray-200", "px-5", "py-4")
    p {
        classes = setOf("text-sm", "text-gray-600")
        +"Page ${results.page} of ${results.totalPages}"
    }
    div {
        classes = setOf("flex", "gap-2")
        if (results.page > 1) {
            paginationLink("Previous", query.copy(page = results.page - 1).queuePath())
        }
        if (results.page < results.totalPages) {
            paginationLink("Next", query.copy(page = results.page + 1).queuePath())
        }
    }
}

private fun FlowContent.paginationLink(label: String, path: String) = a {
    href = path
    attributes["hx-get"] = path
    attributes["hx-target"] = "#$INCIDENT_WORKSPACE_ID"
    attributes["hx-swap"] = "outerHTML"
    attributes["hx-push-url"] = "true"
    classes = setOf("rounded-md", "border", "border-gray-300", "bg-white", "px-3", "py-2", "text-sm", "font-semibold", "text-gray-700", "hover:bg-gray-50", "focus:outline-none", "focus:ring-2", "focus:ring-cyan-600", "focus:ring-offset-2")
    +label
}

internal fun FlowContent.incidentDetail(incident: Incident?) = aside {
    id = INCIDENT_DETAIL_ID
    classes = setOf("self-start", "rounded-lg", "border", "border-gray-200", "bg-white", "p-6", "shadow-sm")
    if (incident == null) {
        h2 {
            classes = setOf("text-lg", "font-semibold", "text-gray-950")
            +"Incident details"
        }
        p {
            classes = setOf("mt-3", "text-sm", "leading-6", "text-gray-600")
            +"No incident is selected because the current filters returned no results."
        }
    } else {
        p {
            classes = setOf("text-sm", "font-semibold", "uppercase", "tracking-wider", "text-cyan-700")
            +incident.id.uppercase()
        }
        h2 {
            classes = setOf("mt-3", "text-2xl", "font-bold", "tracking-tight", "text-gray-950")
            +incident.title
        }
        div {
            classes = setOf("mt-4", "flex", "flex-wrap", "gap-2")
            incidentBadge(incident.severity.label, severityClasses(incident.severity))
            incidentBadge(incident.status.label, statusClasses(incident.status))
        }
        div {
            classes = setOf("mt-5", "grid", "grid-cols-2", "gap-4", "text-sm")
            detailTerm("Service", incident.service)
            detailTerm("Environment", incident.environment)
            detailTerm("Owner", incident.owner)
            detailTerm("Reported", incident.reportedAt)
        }
        detailSection("Summary", incident.summary)
        detailSection("Customer impact", incident.impact)
        detailSection("Latest update", incident.latestUpdate, emphasized = true)
    }
}

private fun FlowContent.detailTerm(term: String, description: String) = div {
    p {
        classes = setOf("font-medium", "text-gray-500")
        +term
    }
    p {
        classes = setOf("mt-1", "text-gray-900")
        +description
    }
}

private fun FlowContent.detailSection(title: String, body: String, emphasized: Boolean = false) = div {
    classes = if (emphasized) {
        setOf("mt-5", "rounded-md", "bg-cyan-50", "p-4")
    } else {
        setOf("mt-5")
    }
    h3 {
        classes = if (emphasized) setOf("text-sm", "font-semibold", "text-cyan-950") else setOf("text-sm", "font-semibold", "text-gray-950")
        +title
    }
    p {
        classes = if (emphasized) setOf("mt-2", "text-sm", "leading-6", "text-cyan-900") else setOf("mt-2", "text-sm", "leading-6", "text-gray-700")
        +body
    }
}

private fun FlowContent.incidentBadge(label: String, badgeClasses: Set<String>) = span {
    classes = setOf("inline-flex", "rounded-full", "px-2.5", "py-1", "text-xs", "font-semibold") + badgeClasses
    +label
}

private fun severityClasses(severity: IncidentSeverity) = when (severity) {
    IncidentSeverity.CRITICAL -> setOf("bg-red-100", "text-red-800")
    IncidentSeverity.HIGH -> setOf("bg-orange-100", "text-orange-800")
    IncidentSeverity.MEDIUM -> setOf("bg-amber-100", "text-amber-800")
    IncidentSeverity.LOW -> setOf("bg-slate-100", "text-slate-700")
}

private fun statusClasses(status: IncidentStatus) = when (status) {
    IncidentStatus.OPEN -> setOf("bg-rose-100", "text-rose-800")
    IncidentStatus.INVESTIGATING -> setOf("bg-violet-100", "text-violet-800")
    IncidentStatus.MONITORING -> setOf("bg-blue-100", "text-blue-800")
    IncidentStatus.RESOLVED -> setOf("bg-emerald-100", "text-emerald-800")
}

private fun resultSummary(results: IncidentResults): String = when (results.totalCount) {
    0 -> "No matching incidents"
    1 -> "1 matching incident"
    else -> "${results.totalCount} matching incidents"
}

private val inputClasses = setOf(
    "mt-1",
    "block",
    "w-full",
    "rounded-md",
    "border",
    "border-gray-300",
    "bg-white",
    "px-3",
    "py-2",
    "text-sm",
    "text-gray-950",
    "shadow-sm",
    "focus:border-cyan-600",
    "focus:outline-none",
    "focus:ring-2",
    "focus:ring-cyan-600"
)
