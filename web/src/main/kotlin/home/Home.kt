package home

import react.*
import react.dom.div
import react.dom.h1
import react.dom.ul
import react.router.dom.navLink

fun RBuilder.home(): ReactElement = child(HOME)

val HOME: FunctionalComponent<RProps> = functionalComponent {
    div {
        h1 { +"Reee-waste" }
        ul {
            navLink("/login") {
                +"Login"
            }
            navLink("/signup") {
                +"Sign Up"
            }
        }
    }
}