package root

import react.*
import react.dom.button
import react.dom.div
import react.dom.h1

fun RBuilder.root() = child(ROOT)

private val ROOT: FunctionalComponent<RProps> = functionalComponent {
    div {
        h1 { +"Reee-waste" }
        button { +"Login" }
        button { +"Sign up" }
    }
}