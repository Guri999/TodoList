package com.jess.nbcamp.challnge2.practice.signup

import androidx.annotation.StringRes
import com.jess.nbcamp.challnge2.R

data class SignUpUiState(
    val name: String? = null,
    val email: String? = null,
    // email
    val emailService: String? = null,
    val emailPosition: Int = 0,
    @StringRes val button: Int = R.string.sign_up_confirm
)