import react.dom.render
import root.root
import kotlin.browser.document

fun main() {
    render(document.getElementById("root")) {
        root()
    }
}