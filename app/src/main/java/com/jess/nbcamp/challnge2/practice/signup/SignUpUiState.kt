package com.jess.nbcamp.challnge2.practice.signup

import androidx.annotation.StringRes
import com.jess.nbcamp.challnge2.R

data class SignUpUiState(
    val name: String?,
    val email: String?,
    val emailService: String?,
    val emailPosition: Int,
    val emailServices: List<String>,
    @StringRes val button: Int,
    val buttonEnable: Boolean,
) {
    companion object {
        fun init() = SignUpUiState(
            name = null,
            email = null,
            emailService = null,
            emailPosition = 0,
            emailServices = emptyList(),
            button = R.string.sign_up_confirm,
            buttonEnable = false
        )
    }
}

data class SignUpErrorUiState(
    val name: SignUpErrorMessage,
    val email: SignUpErrorMessage,
    val emailService: SignUpErrorMessage,
    val password: SignUpErrorMessage,
    val passwordEnabled: Boolean,
    val passwordConfirm: SignUpErrorMessage,
) {
    companion object {
        fun init() = SignUpErrorUiState(
            name = SignUpErrorMessage.PASS,
            email = SignUpErrorMessage.PASS,
            emailService = SignUpErrorMessage.PASS,
            password = SignUpErrorMessage.PASS,
            passwordEnabled = false,
            passwordConfirm = SignUpErrorMessage.PASS,

            )
    }
}