package com.jess.nbcamp.challnge2.practice.signup

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jess.nbcamp.challnge2.R
import com.jess.nbcamp.challnge2.practice.signup.SignUpValidExtension.includeAt
import com.jess.nbcamp.challnge2.practice.signup.SignUpValidExtension.includeSpecialCharacters
import com.jess.nbcamp.challnge2.practice.signup.SignUpValidExtension.includeUpperCase
import com.jess.nbcamp.challnge2.practice.signup.SignUpValidExtension.validEmailServiceProvider
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

    // user
    private val _uiState: MutableLiveData<SignUpUiState> = MutableLiveData()
    val uiState: LiveData<SignUpUiState> get() = _uiState

    // error
    private val _errorUiState: MutableLiveData<SignUpErrorUiState?> = MutableLiveData()
    val errorUiState: LiveData<SignUpErrorUiState?> get() = _errorUiState

    // user
    private val _buttonEnable: MutableLiveData<Boolean> = MutableLiveData()
    val buttonEnable: LiveData<Boolean> get() = _buttonEnable

    init {
        initUiState()
        _errorUiState.value = SignUpErrorUiState.init()
    }

    private fun initUiState() {
        val index = emailServices.indexOf(userEntity?.emailService)
        _uiState.value = SignUpUiState(
            name = userEntity?.name,
            email = userEntity?.email,
            emailService = userEntity?.emailService,
            emailPosition = if (index < 0) {
                emailServices.lastIndex
            } else {
                index
            },
            emailServices = emailServices,
            button = if (entryType == SignUpEntryType.UPDATE) {
                R.string.sign_up_update
            } else {
                R.string.sign_up_confirm
            },
            buttonEnable = false
        )
    }

    fun onItemSelectedEmailService(position: Int) {
        _event.value = SignUpEvent.VisibleEmailService(
            position == emailServices.lastIndex
        )
    }

    fun checkValidName(text: String) {
        _errorUiState.value = errorUiState.value?.copy(
            name = getMessageValidName(text)
        )
    }

    fun checkValidEmail(text: String) {
        _errorUiState.value = errorUiState.value?.copy(
            email = getMessageValidEmail(text)
        )
    }

    fun checkValidEmailService(
        text: String,
        isVisible: Boolean
    ) {
        _errorUiState.value = errorUiState.value?.copy(
            emailService = getMessageValidEmailService(text, isVisible)
        )
    }

    fun checkValidPassword(text: String) {
        _errorUiState.value = errorUiState.value?.copy(
            password = getMessageValidPassword(text)
        )
    }

    fun checkValidPasswordConfirm(
        text: String,
        confirm: String
    ) {
        _errorUiState.value = errorUiState.value?.copy(
            passwordConfirm = getMessageValidPasswordConfirm(text, confirm)
        )
    }

    private fun getMessageValidName(text: String) = if (text.isBlank()) {
        SignUpErrorMessage.NAME
    } else {
        SignUpErrorMessage.PASS
    }

    private fun getMessageValidEmail(text: String) = when {
        text.isBlank() -> SignUpErrorMessage.EMAIL_BLANK
        text.includeAt() -> SignUpErrorMessage.EMAIL_AT
        else -> SignUpErrorMessage.PASS
    }

    private fun getMessageValidEmailService(
        text: String,
        isVisible: Boolean
    ): SignUpErrorMessage =
        if (isVisible &&
            (text.isBlank() || text.validEmailServiceProvider().not())
        ) {
            SignUpErrorMessage.EMAIL_SERVICE_PROVIDER
        } else {
            getMessageValidEmail(text)
        }

    private fun getMessageValidPassword(text: String) = when {
        text.length < 10 -> SignUpErrorMessage.PASSWORD_LENGTH

        text.includeSpecialCharacters()
            .not() -> SignUpErrorMessage.PASSWORD_SPECIAL_CHARACTERS

        text.includeUpperCase().not() -> SignUpErrorMessage.PASSWORD_UPPER_CASE

        else -> SignUpErrorMessage.PASS
    }

    private fun getMessageValidPasswordConfirm(
        password: String,
        confirm: String
    ): SignUpErrorMessage = if (password != confirm) {
        SignUpErrorMessage.PASSWORD_PASSWORD
    } else {
        SignUpErrorMessage.PASS
    }

    fun checkConfirmButtonEnable() {
        _buttonEnable.value = isConfirmButtonEnable()
    }

    fun onClickSignUp() {
        if (isConfirmButtonEnable()) {
            _event.value = SignUpEvent.ClickConfirmButton
        }
    }

    private fun isConfirmButtonEnable() = errorUiState.value?.let { state ->
        state.name == SignUpErrorMessage.PASS
                && state.email == SignUpErrorMessage.PASS
                && state.emailService == SignUpErrorMessage.PASS
                && state.password == SignUpErrorMessage.PASS
                && state.passwordConfirm == SignUpErrorMessage.PASS
    } ?: false

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