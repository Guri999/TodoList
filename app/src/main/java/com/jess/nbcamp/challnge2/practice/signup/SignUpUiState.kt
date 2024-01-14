package com.jess.nbcamp.challnge2.practice.signup

import androidx.annotation.StringRes
import com.jess.nbcamp.challnge2.R

data class SignUpUserUiState(
    val name: String?,
    val email: String?,
    val emailService: String?,
    val emailPosition: Int
)

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

data class SignUpButtonUiState(
    @StringRes val text: Int,
    val enabled: Boolean
) {
    companion object {
        fun init() = SignUpButtonUiState(
            text = R.string.sign_up_confirm,
            enabled = false,
        )
    }
}