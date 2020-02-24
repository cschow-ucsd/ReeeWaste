package login

import kotlinx.html.InputType
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import org.w3c.dom.HTMLInputElement
import react.*
import react.dom.button
import react.dom.div
import react.dom.input

fun RBuilder.login(): ReactElement = child(LOGIN)

private val LOGIN: FunctionalComponent<RProps> = functionalComponent {
    val (username, setUsername) = useState("")
    val (password, setPassword) = useState("")

    div {
        input {
            attrs {
                placeholder = "Username"
                onChangeFunction = {
                    val target = it.target as HTMLInputElement
                    setUsername(target.value)
                }
            }
        }
        input {
            attrs {
                type = InputType.password
                placeholder = "Password"
                onChangeFunction = {
                    val target = it.target as HTMLInputElement
                    setPassword(target.value)
                }
            }
        }
        button {
            +"Submit"
            attrs.onClickFunction = {
                println("Username: $username; Password: $password")
            }
        }
    }
}