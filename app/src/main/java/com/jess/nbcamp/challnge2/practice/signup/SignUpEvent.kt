package com.jess.nbcamp.challnge2.practice.signup

sealed interface SignUpEvent {

    data class VisibleEmailService(
        val visible: Boolean
    ) : SignUpEvent
}