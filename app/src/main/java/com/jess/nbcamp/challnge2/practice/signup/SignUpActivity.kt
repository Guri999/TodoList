package com.jess.nbcamp.challnge2.practice.signup

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.jess.nbcamp.challnge2.databinding.SignUpActivityBinding
import com.jess.nbcamp.challnge2.practice.signup.SignUpValidExtension.includeAt
import com.jess.nbcamp.challnge2.practice.signup.SignUpValidExtension.includeSpecialCharacters
import com.jess.nbcamp.challnge2.practice.signup.SignUpValidExtension.includeUpperCase
import com.jess.nbcamp.challnge2.practice.signup.SignUpValidExtension.validEmailServiceProvider

class SignUpActivity : AppCompatActivity() {

    companion object {

        const val EXTRA_ENTRY_TYPE = "extra_entry_type"
        const val EXTRA_USER_ENTITY = "extra_user_entity"

        fun newIntent(
            context: Context,
            entryType: SignUpEntryType,
            entity: SignUpUserEntity
        ): Intent = Intent(context, SignUpActivity()::class.java).apply {
            putExtra(EXTRA_ENTRY_TYPE, entryType.ordinal)
            putExtra(EXTRA_USER_ENTITY, entity)
        }
    }

    private val binding: SignUpActivityBinding by lazy {
        SignUpActivityBinding.inflate(layoutInflater)
    }

    private val entryType: SignUpEntryType by lazy {
        SignUpEntryType.getEntryType(
            intent?.getIntExtra(EXTRA_ENTRY_TYPE, 0)
        )
    }

    private val userEntity: SignUpUserEntity? by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent?.getParcelableExtra(EXTRA_USER_ENTITY, SignUpUserEntity::class.java)
        } else {
            intent?.getParcelableExtra(EXTRA_USER_ENTITY)
        }
    }

    private val viewModel: SignUpViewModel by viewModels {
        SignUpViewModelFactory(
            context = this@SignUpActivity,
            entryType = entryType,
            userEntity = userEntity
        )
    }

    private val editTexts
        get() = with(binding) {
            listOf(
                etName,
                etEmail,
                etEmailService,
                etPassword,
                etPasswordConfirm
            )
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()
        initViewModel()
    }

    private fun initView() = with(binding) {

        // text changed 
        setTextChangedListener()

        // focus out 처리
        setOnFocusChangedListener()

        serviceProvider.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) = Unit

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                viewModel.onItemSelectedEmailService(position)
            }
        }

        btConfirm.setOnClickListener {
            if (isConfirmButtonEnable()) {
                viewModel.onClickSignUp()
            }
        }
    }

    private fun initViewModel() = with(viewModel) {
        event.observe(this@SignUpActivity) {
            when (it) {
                is SignUpEvent.VisibleEmailService -> {
                    binding.etEmailService.isVisible = it.visible
                }
            }
        }

        uiState.observe(this@SignUpActivity) {
            with(binding) {
                etName.setText(it.name)
                etEmail.setText(it.email)
                etEmailService.setText(it.emailService)
                serviceProvider.setSelection(it.emailPosition)
                btConfirm.setText(it.button)
                serviceProvider.adapter = ArrayAdapter(
                    this@SignUpActivity,
                    android.R.layout.simple_spinner_dropdown_item,
                    it.emailServices
                )
            }
        }
    }

    private fun setTextChangedListener() = with(binding) {
        editTexts.forEach { editText ->
            editText.addTextChangedListener {
                editText.setErrorMessage()
                btConfirm.isEnabled = isConfirmButtonEnable()
            }
        }
    }

    private fun setOnFocusChangedListener() = with(binding) {
        editTexts.forEach { editText ->
            editText.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus.not()) {
                    editText.setErrorMessage()
                    btConfirm.isEnabled = isConfirmButtonEnable()
                }
            }
        }
    }

    private fun EditText.setErrorMessage() = with(binding) {
        when (this@setErrorMessage) {
            etName -> tvNameError.text = getMessageValidName()
            etEmail -> tvEmailError.text = getMessageValidEmail()
            etEmailService -> tvEmailError.text = getMessageValidEmailProvider()
            etPassword -> {
                with(tvPasswordError) {
                    isEnabled = etPassword.text.toString().isNotBlank()
                    text = if (etPassword.text.toString().isBlank()) {
                        getString(SignUpErrorMessage.PASSWORD_HINT.message)
                    } else {
                        getMessageValidPassword()
                    }
                }
            }

            etPasswordConfirm -> tvPasswordConfirmError.text = getMessageValidPasswordConfirm()

            else -> Unit
        }
    }

    private fun getMessageValidName(): String = getString(
        if (binding.etName.text.toString().isBlank()) {
            SignUpErrorMessage.NAME
        } else {
            SignUpErrorMessage.PASS
        }.message
    )

    private fun getMessageValidEmail(): String {
        val text = binding.etEmail.text.toString()
        return getString(
            when {
                text.isBlank() -> SignUpErrorMessage.EMAIL_BLANK
                text.includeAt() -> SignUpErrorMessage.EMAIL_AT
                else -> SignUpErrorMessage.PASS
            }.message
        )
    }

    private fun getMessageValidEmailProvider(): String {
        val text = binding.etEmailService.text.toString()
        return if (
            binding.etEmailService.isVisible
            && (binding.etEmailService.text.toString().isBlank()
                    || text.validEmailServiceProvider().not())
        ) {
            getString(SignUpErrorMessage.EMAIL_SERVICE_PROVIDER.message)
        } else {
            getMessageValidEmail()
        }
    }

    private fun getMessageValidPassword(): String {
        val text = binding.etPassword.text.toString()
        return getString(
            when {
                text.length < 10 -> SignUpErrorMessage.PASSWORD_LENGTH

                text.includeSpecialCharacters()
                    .not() -> SignUpErrorMessage.PASSWORD_SPECIAL_CHARACTERS

                text.includeUpperCase().not() -> SignUpErrorMessage.PASSWORD_UPPER_CASE

                else -> SignUpErrorMessage.PASS
            }.message
        )
    }

    private fun getMessageValidPasswordConfirm(): String =
        getString(
            if (binding.etPassword.text.toString() != binding.etPasswordConfirm.text.toString()) {
                SignUpErrorMessage.PASSWORD_PASSWORD
            } else {
                SignUpErrorMessage.PASS
            }.message
        )

    private fun isConfirmButtonEnable() = getMessageValidName().isBlank()
            && getMessageValidEmail().isBlank()
            && getMessageValidEmailProvider().isBlank()
            && getMessageValidPassword().isBlank()
            && getMessageValidPasswordConfirm().isBlank()
}