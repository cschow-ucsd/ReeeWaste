import kotlinext.js.require
import kotlinext.js.requireAll
import react.dom.render
import root.root
import kotlin.browser.document

fun main() {
    requireAll(require.context("src", true, js("/\\.css$/")))

    render(document.getElementById("root")) {
        root()
    }
}