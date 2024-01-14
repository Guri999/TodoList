package com.jess.nbcamp.challnge2.practice.signup

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jess.nbcamp.challnge2.R
import com.jess.nbcamp.challnge2.provider.ResourceProvider
import com.jess.nbcamp.challnge2.provider.ResourceProviderImpl

class SignUpViewModel(
    private val resourceProvider: ResourceProvider,
    private val entryType: SignUpEntryType,
    private val userEntity: SignUpUserEntity?
) : ViewModel() {

    private val emailServices = listOf(
        resourceProvider.getString(R.string.sign_up_email_provider_gmail),
        resourceProvider.getString(R.string.sign_up_email_provider_kakao),
        resourceProvider.getString(R.string.sign_up_email_provider_naver),
        resourceProvider.getString(R.string.sign_up_email_provider_direct)
    )

    // event
    private val _event: MutableLiveData<SignUpEvent> = MutableLiveData()
    val event: LiveData<SignUpEvent> get() = _event

    // email service
    private val _emailService: MutableLiveData<List<String>> = MutableLiveData()
    val emailService: LiveData<List<String>> get() = _emailService

    // user
    private val _user: MutableLiveData<SignUpUserEntity> = MutableLiveData()
    val user: LiveData<SignUpUserEntity> get() = _user


    init {
        _emailService.value = emailServices
        serUserEntity()
    }

    /**
     * 회원 정보와 이메일 서비스(Spinner)를 선택합니다.
     */
    private fun serUserEntity() {
        userEntity?.let { entity ->
            val index = emailServices.indexOf(entity.emailService)
            _user.value = entity.copy(
                emailPosition = if (index < 0) {
                    emailServices.lastIndex
                } else {
                    index
                }
            )
        }
    }

    fun onItemSelectedEmailService(position: Int) {
        _event.value = SignUpEvent.VisibleEmailService(
            position == emailServices.lastIndex
        )
    }

    fun onClickSignUp() {

    }
}

class SignUpViewModelFactory(
    private val context: Context,
    private val entryType: SignUpEntryType,
    private val userEntity: SignUpUserEntity?
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignUpViewModel::class.java)) {
            return SignUpViewModel(
                ResourceProviderImpl(context),
                entryType,
                userEntity
            ) as T
        } else {
            throw IllegalArgumentException("Not found ViewModel class.")
        }
    }
}