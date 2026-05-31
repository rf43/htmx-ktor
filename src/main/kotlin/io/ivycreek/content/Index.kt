package io.ivycreek.content

import io.ivycreek.dashboard.dashboard
import io.ivycreek.navbar.navbar
import kotlinx.html.*

private const val HTMX_SCRIPT_URL = "https://cdn.jsdelivr.net/npm/htmx.org@2.0.10/dist/htmx.min.js"
private const val HTMX_SCRIPT_INTEGRITY = "sha384-H5SrcfygHmAuTDZphMHqBJLc3FhssKjG7w/CeCpFReSfwBWDTKpkzPP8c+cLsK+V"
private const val ACTIVE_NAVIGATION_SCRIPT = """
(() => {
  function currentNavigationPath() {
    const path = window.location.pathname;
    if (path === "/") {
      return "/components/dashboard";
    }
    if (path.startsWith("/components/calendar/")) {
      return "/components/calendar";
    }
    return path;
  }

  function setActiveNavigation() {
    const currentPath = currentNavigationPath();
    document.querySelectorAll("[data-nav-link]").forEach((link) => {
      const active = link.getAttribute("href") === currentPath;
      link.classList.toggle("bg-gray-800", active);
      link.classList.toggle("text-white", active);
      link.classList.toggle("text-gray-700", !active);
      if (active) {
        link.setAttribute("aria-current", "page");
      } else {
        link.removeAttribute("aria-current");
      }
    });
  }

  document.addEventListener("DOMContentLoaded", setActiveNavigation);
  document.addEventListener("htmx:afterSettle", setActiveNavigation);
  window.addEventListener("popstate", () => requestAnimationFrame(setActiveNavigation));
})();
"""

fun HTML.index(activePath: String = "/components/dashboard", pageContent: FlowContent.() -> Unit = { dashboard() }) {
    lang = "en"
    head {
        title {
            +"Ktor + htmx Showcase"
        }
        script {
            src = HTMX_SCRIPT_URL
            attributes["integrity"] = HTMX_SCRIPT_INTEGRITY
            attributes["crossorigin"] = "anonymous"
        }
        link(rel = "stylesheet", href = "/app.css")
        script {
            unsafe {
                +ACTIVE_NAVIGATION_SCRIPT
            }
        }
    }
    body {
        classes = setOf("h-full", "bg-slate-50")
        div {
            classes = setOf("min-h-full")
            navbar(activePath, "Dashboard", "Team", "Projects", "Calendar", "About", "Contact")
            pageContent()
        }
    }
}
