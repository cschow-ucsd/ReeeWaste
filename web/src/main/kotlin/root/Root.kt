package root

import home.home
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import login.login
import react.*
import react.router.dom.hashRouter
import react.router.dom.redirect
import react.router.dom.route
import react.router.dom.switch
import signup.signup

fun RBuilder.root(): ReactElement = child(ROOT)

private val ROOT: FunctionalComponent<RProps> = functionalComponent {
    val mainScope: CoroutineScope = MainScope()
    useEffectWithCleanup(emptyList()) { { mainScope.cancel() } }

    hashRouter {
        switch {
            route("/home", exact = true) { home() }
            route("/login", exact = true) { login() }
            route("/signup", exact = true) { signup() }
            redirect("/", "/home")
        }
    }
}