package kh.edu.rupp.taskmanagement.ui.form

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

const val MIN_PASSWORD_LENGTH = 6

// the same shape as the task form: the values live here and the rules are read off them
class LoginFormState {
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var isPasswordVisible by mutableStateOf(false)

    // plausible, not proven: only the mail server knows whether an address exists
    val isEmailValid: Boolean
        get() = email.contains("@") && email.substringAfter("@").contains(".")

    val isPasswordValid: Boolean
        get() = password.length >= MIN_PASSWORD_LENGTH

    // an empty field has not been answered yet, so it is not wrong yet
    val showEmailError: Boolean
        get() = email.isNotEmpty() && !isEmailValid

    val showPasswordError: Boolean
        get() = password.isNotEmpty() && !isPasswordValid

    val isValid: Boolean
        get() = isEmailValid && isPasswordValid
}

@Composable
fun rememberLoginFormState(): LoginFormState = remember { LoginFormState() }
