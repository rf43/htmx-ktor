package io.ivycreek.about

import io.ivycreek.content.pageHeader
import kotlinx.html.*

fun FlowContent.about() = div {
    id = "content"
    pageHeader("About")
    main {
        classes = setOf("mx-auto", "max-w-7xl", "py-6", "sm:px-6", "lg:px-8")
        p {
            classes = setOf("text-2xl", "text-gray-900")
            +("At PNutz, we're weaving a rich tapestry of knowledge and innovation for the software development community. Our mission is to empower software engineers with comprehensive learning resources, fostering an environment of continuous growth and creativity. We believe in making complex concepts accessible, engaging, and enjoyable, bridging the gap between professional development and playful exploration. Together, we're building a brighter future for software engineering, one lesson at a time")
        }
    }
}