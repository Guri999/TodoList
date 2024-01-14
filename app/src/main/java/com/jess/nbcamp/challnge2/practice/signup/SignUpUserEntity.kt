package com.jess.nbcamp.challnge2.practice.signup

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SignUpUserEntity(
    val name: String?,
    val email: String?,
    val emailService: String?,
    val emailPosition: Int = 0
) : Parcelable