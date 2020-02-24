package signup

import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import org.w3c.dom.HTMLInputElement
import react.*
import react.dom.button
import react.dom.div
import react.dom.input
import kotlin.browser.window

fun RBuilder.signup(): ReactElement = child(SIGN_UP)

private val SIGN_UP: FunctionalComponent<RProps> = functionalComponent {
    val (email, setEmail) = useState("")
    val (pass1, setPass1) = useState("")
    val (pass2, setPass2) = useState("")

    div {
        input {
            attrs {
                placeholder = "Email"
                onChangeFunction = {
                    val target = it.target as HTMLInputElement
                    setEmail(target.value)
                }
            }
        }
        input {
            attrs {
                placeholder = "Password"
                onChangeFunction = {
                    val target = it.target as HTMLInputElement
                    setPass1(target.value)
                }
            }
        }
        input {
            attrs {
                placeholder = "Retype Password"
                onChangeFunction = {
                    val target = it.target as HTMLInputElement
                    setPass2(target.value)
                }
            }
        }
        button {
            +"Submit"
            attrs.onClickFunction = {
                if (pass1 != pass2) window.alert("Passwords do not match.")
                else println("Email: $email; Password $pass1")
            }
        }
    }
}